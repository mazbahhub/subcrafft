package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.SaaSRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val iconName: String) {
    DASHBOARD("Dashboard", "dashboard"),
    SUBSCRIBERS("Subscribers", "people"),
    PLANS("Pricing Plans", "sell"),
    PORTAL("End-User Demo", "preview"),
    VIBE96("Vibe96 Studio", "rocket_launch")
}

data class SaaSMetrics(
    val mrr: Double,
    val arr: Double,
    val activeCount: Int,
    val trialCount: Int,
    val pastDueCount: Int,
    val churnedCount: Int,
    val churnRatePercent: Double,
    val arpu: Double,
    val estimatedLtv: Double
)

class SaaSViewModel(
    private val repository: SaaSRepository = SaaSRepository()
) : ViewModel() {

    val currentNiche: StateFlow<NicheType> = repository.currentNiche
    val plans: StateFlow<List<SubscriptionPlan>> = repository.plans
    val subscribers: StateFlow<List<Subscriber>> = repository.subscribers
    val invoices: StateFlow<List<Invoice>> = repository.invoices
    val webhooks: StateFlow<List<WebhookEvent>> = repository.webhooks
    val gatedFeatures: StateFlow<List<GatedToolFeature>> = repository.gatedFeatures
    val customerSelectedPlan: StateFlow<String> = repository.customerSelectedPlan
    val auditCriteria: StateFlow<List<Vibe96AuditCriterion>> = repository.auditCriteria

    private val _selectedTab = MutableStateFlow(AppTab.DASHBOARD)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _isAnnualBilling = MutableStateFlow(false)
    val isAnnualBilling: StateFlow<Boolean> = _isAnnualBilling.asStateFlow()

    private val _subscriberFilter = MutableStateFlow("ALL")
    val subscriberFilter: StateFlow<String> = _subscriberFilter.asStateFlow()

    private val _subscriberSearchQuery = MutableStateFlow("")
    val subscriberSearchQuery: StateFlow<String> = _subscriberSearchQuery.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    // Dialogs
    private val _showAddSubscriberDialog = MutableStateFlow(false)
    val showAddSubscriberDialog: StateFlow<Boolean> = _showAddSubscriberDialog.asStateFlow()

    private val _editingPlan = MutableStateFlow<SubscriptionPlan?>(null)
    val editingPlan: StateFlow<SubscriptionPlan?> = _editingPlan.asStateFlow()

    private val _showPaywallModal = MutableStateFlow(false)
    val showPaywallModal: StateFlow<Boolean> = _showPaywallModal.asStateFlow()

    private val _paywallTargetFeature = MutableStateFlow<GatedToolFeature?>(null)
    val paywallTargetFeature: StateFlow<GatedToolFeature?> = _paywallTargetFeature.asStateFlow()

    private val _showVibe96ExportDialog = MutableStateFlow(false)
    val showVibe96ExportDialog: StateFlow<Boolean> = _showVibe96ExportDialog.asStateFlow()

    // Derived Metrics
    val metrics: StateFlow<SaaSMetrics> = subscribers.map { list ->
        val active = list.filter { it.status == SubscriberStatus.ACTIVE }
        val activeCount = active.size
        val mrr = active.sumOf { it.mrrAmount }
        val arr = mrr * 12.0
        val trialCount = list.count { it.status == SubscriberStatus.TRIALING }
        val pastDueCount = list.count { it.status == SubscriberStatus.PAST_DUE }
        val churnedCount = list.count { it.status == SubscriberStatus.CANCELED }
        val total = list.size.coerceAtLeast(1)
        val churnRate = (churnedCount.toDouble() / total) * 100.0
        val arpu = if (activeCount > 0) mrr / activeCount else 0.0
        val estimatedLtv = if (churnRate > 0) (arpu / (churnRate / 100.0)) else (arpu * 24.0)

        SaaSMetrics(
            mrr = mrr,
            arr = arr,
            activeCount = activeCount,
            trialCount = trialCount,
            pastDueCount = pastDueCount,
            churnedCount = churnedCount,
            churnRatePercent = churnRate,
            arpu = arpu,
            estimatedLtv = estimatedLtv
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SaaSMetrics(0.0, 0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0)
    )

    // Filtered subscribers
    val filteredSubscribers: StateFlow<List<Subscriber>> = combine(
        subscribers,
        subscriberFilter,
        subscriberSearchQuery
    ) { subs, filter, query ->
        subs.filter { sub ->
            val matchesFilter = when (filter) {
                "ACTIVE" -> sub.status == SubscriberStatus.ACTIVE
                "TRIALING" -> sub.status == SubscriberStatus.TRIALING
                "PAST_DUE" -> sub.status == SubscriberStatus.PAST_DUE
                "CANCELED" -> sub.status == SubscriberStatus.CANCELED
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    sub.name.contains(query, ignoreCase = true) ||
                    sub.company.contains(query, ignoreCase = true) ||
                    sub.email.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun switchNiche(niche: NicheType) {
        repository.switchNiche(niche)
        showFeedback("Switched niche to ${niche.title}")
    }

    fun toggleBillingCycle() {
        _isAnnualBilling.value = !_isAnnualBilling.value
    }

    fun setSubscriberFilter(filter: String) {
        _subscriberFilter.value = filter
    }

    fun setSubscriberSearchQuery(query: String) {
        _subscriberSearchQuery.value = query
    }

    fun openAddSubscriberDialog() {
        _showAddSubscriberDialog.value = true
    }

    fun closeAddSubscriberDialog() {
        _showAddSubscriberDialog.value = false
    }

    fun addSubscriber(name: String, company: String, email: String, plan: SubscriptionPlan) {
        repository.addSubscriber(name, company, email, plan)
        closeAddSubscriberDialog()
        showFeedback("Added subscriber $name ($${plan.monthlyPrice}/mo MRR)")
    }

    fun toggleSubscriberStatus(subscriberId: String, newStatus: SubscriberStatus) {
        repository.toggleSubscriberStatus(subscriberId, newStatus)
        showFeedback("Subscriber status updated to ${newStatus.label}")
    }

    fun startEditPlan(plan: SubscriptionPlan) {
        _editingPlan.value = plan
    }

    fun closeEditPlan() {
        _editingPlan.value = null
    }

    fun updatePlanPrice(planId: String, newPrice: Double) {
        repository.updatePlanPrice(planId, newPrice)
        closeEditPlan()
        showFeedback("Plan updated to $$newPrice/mo")
    }

    fun simulateBillingRenewal() {
        repository.simulateBillingRenewal()
        showFeedback("Triggered renewal event & auto-invoiced")
    }

    fun openPaywallForFeature(feature: GatedToolFeature) {
        _paywallTargetFeature.value = feature
        _showPaywallModal.value = true
    }

    fun closePaywall() {
        _showPaywallModal.value = false
        _paywallTargetFeature.value = null
    }

    fun upgradeCustomerPlan(newPlanName: String) {
        repository.upgradeCustomerPlan(newPlanName)
        closePaywall()
        showFeedback("Successfully upgraded to $newPlanName! Feature unlocked.")
    }

    fun openVibe96ExportDialog() {
        _showVibe96ExportDialog.value = true
    }

    fun closeVibe96ExportDialog() {
        _showVibe96ExportDialog.value = false
    }

    fun showFeedback(msg: String) {
        _userFeedbackMessage.value = msg
    }

    fun clearFeedback() {
        _userFeedbackMessage.value = null
    }

    fun getRevenueTrends(): List<RevenueMonthlyData> {
        return repository.getRevenueTrends()
    }
}
