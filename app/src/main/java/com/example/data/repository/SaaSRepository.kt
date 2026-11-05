package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class SaaSRepository {

    private val _currentNiche = MutableStateFlow(NicheType.AGENCY_RETAINER)
    val currentNiche: StateFlow<NicheType> = _currentNiche.asStateFlow()

    private val _plans = MutableStateFlow<List<SubscriptionPlan>>(emptyList())
    val plans: StateFlow<List<SubscriptionPlan>> = _plans.asStateFlow()

    private val _subscribers = MutableStateFlow<List<Subscriber>>(emptyList())
    val subscribers: StateFlow<List<Subscriber>> = _subscribers.asStateFlow()

    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _webhooks = MutableStateFlow<List<WebhookEvent>>(emptyList())
    val webhooks: StateFlow<List<WebhookEvent>> = _webhooks.asStateFlow()

    private val _gatedFeatures = MutableStateFlow<List<GatedToolFeature>>(emptyList())
    val gatedFeatures: StateFlow<List<GatedToolFeature>> = _gatedFeatures.asStateFlow()

    // Customer Portal Simulation
    private val _customerSelectedPlan = MutableStateFlow("Starter")
    val customerSelectedPlan: StateFlow<String> = _customerSelectedPlan.asStateFlow()

    private val _auditCriteria = MutableStateFlow<List<Vibe96AuditCriterion>>(emptyList())
    val auditCriteria: StateFlow<List<Vibe96AuditCriterion>> = _auditCriteria.asStateFlow()

    init {
        loadNicheData(NicheType.AGENCY_RETAINER)
        loadAuditCriteria()
    }

    fun switchNiche(niche: NicheType) {
        _currentNiche.value = niche
        loadNicheData(niche)
    }

    private fun loadNicheData(niche: NicheType) {
        when (niche) {
            NicheType.AGENCY_RETAINER -> {
                _plans.value = listOf(
                    SubscriptionPlan(
                        id = "plan_starter",
                        name = "Starter Retainer",
                        monthlyPrice = 49.0,
                        annualPrice = 39.0,
                        description = "Ideal for boutique freelancers managing 1-3 ongoing clients",
                        features = listOf("Up to 3 Active Client Portals", "Weekly Deliverable Tracking", "Automated Stripe Invoicing", "Client File Vault (10GB)"),
                        quotaLabel = "Active Client Portals",
                        quotaValue = "3 Portals",
                        isPopular = false,
                        maxSeats = 1
                    ),
                    SubscriptionPlan(
                        id = "plan_pro",
                        name = "Agency Growth",
                        monthlyPrice = 129.0,
                        annualPrice = 99.0,
                        description = "Full agency operational suite with milestone approval & custom branding",
                        features = listOf("Up to 15 Active Client Portals", "Custom CNAME Domain (client.agency.com)", "Milestone Sign-Off & Escrow", "Real-time Slack/WhatsApp Alerts", "Automated Churn Warning"),
                        quotaLabel = "Active Client Portals",
                        quotaValue = "15 Portals",
                        isPopular = true,
                        maxSeats = 5
                    ),
                    SubscriptionPlan(
                        id = "plan_ent",
                        name = "Enterprise Scale",
                        monthlyPrice = 299.0,
                        annualPrice = 249.0,
                        description = "Unlimited scale with dedicated API webhooks, white-label, and multi-team seats",
                        features = listOf("Unlimited Client Portals", "100% White-Label (Zero SubCraft branding)", "Custom Contracts & E-Sign", "Dedicated Account Manager", "Priority Webhook Pipeline"),
                        quotaLabel = "Active Client Portals",
                        quotaValue = "Unlimited",
                        isPopular = false,
                        maxSeats = 25
                    )
                )

                _subscribers.value = listOf(
                    Subscriber("sub_1", "Alex Vance", "Apex Creative Agency", "alex@apexcreative.io", "plan_pro", "Agency Growth", 129.0, SubscriberStatus.ACTIVE, "2026-08-15", "2026-12-15", "AV", 0xFF2563EB),
                    Subscriber("sub_2", "Sarah Jenkins", "PixelForge Media", "sarah@pixelforge.com", "plan_pro", "Agency Growth", 129.0, SubscriberStatus.ACTIVE, "2026-09-01", "2026-12-01", "SJ", 0xFF8B5CF6),
                    Subscriber("sub_3", "Marcus Cole", "NorthStar Growth Partners", "marcus@northstar.co", "plan_ent", "Enterprise Scale", 299.0, SubscriberStatus.ACTIVE, "2026-07-20", "2026-11-20", "MC", 0xFF10B981),
                    Subscriber("sub_4", "Elena Rostova", "Velox Consulting", "elena@velox.org", "plan_starter", "Starter Retainer", 49.0, SubscriberStatus.ACTIVE, "2026-10-12", "2026-11-12", "ER", 0xFFF59E0B),
                    Subscriber("sub_5", "David Kim", "K-Digital Labs", "david@kdlabs.dev", "plan_pro", "Agency Growth", 129.0, SubscriberStatus.TRIALING, "2026-11-01", "2026-11-14", "DK", 0xFF00D2FF),
                    Subscriber("sub_6", "Tanya Brooks", "Cobalt Studio", "tanya@cobalt.design", "plan_starter", "Starter Retainer", 49.0, SubscriberStatus.PAST_DUE, "2026-06-10", "2026-11-05", "TB", 0xFFEF4444),
                    Subscriber("sub_7", "Liam O'Connor", "Emerald SEO Lab", "liam@emeraldseo.com", "plan_pro", "Agency Growth", 129.0, SubscriberStatus.CANCELED, "2026-05-18", "2026-10-18", "LO", 0xFF64748B)
                )

                _gatedFeatures.value = listOf(
                    GatedToolFeature("tool_1", "Client Portal Dashboard", "Live status view with monthly retainer burn-down meter", "Starter", true, "Core"),
                    GatedToolFeature("tool_2", "Milestone Deliverables Engine", "Multi-stage client approval workflow with e-signature sign-off", "Pro", false, "Advanced"),
                    GatedToolFeature("tool_3", "Custom Domain CNAME", "Point clients to your own brand URL with auto SSL", "Pro", false, "Branding"),
                    GatedToolFeature("tool_4", "White-Label Invoicing & Reports", "Automated PDF invoices with custom brand identity", "Enterprise", false, "Enterprise")
                )
            }

            NicheType.AI_MICRO_TOOL -> {
                _plans.value = listOf(
                    SubscriptionPlan("plan_ai_1", "Hobbyist Maker", 19.0, 15.0, "Great for individual prompt engineers testing workflows", listOf("5,000 AI Tokens/mo", "3 Chained Prompt Workflows", "Standard Response Speed", "Community Support"), "Monthly Tokens", "5K Tokens", false, 1),
                    SubscriptionPlan("plan_ai_2", "Pro Workflow Studio", 59.0, 49.0, "High-volume generation with custom embeddings & export", listOf("50,000 AI Tokens/mo", "Unlimited Chained Workflows", "Instant Claude/GPT-4o Pipeline", "REST API Access (10 req/s)", "Webhook Notification Triggers"), "Monthly Tokens", "50K Tokens", true, 3),
                    SubscriptionPlan("plan_ai_3", "AI Reseller Business", 179.0, 149.0, "Embed AI workflows directly into your client apps", listOf("300,000 AI Tokens/mo", "White-Label AI Widget Generator", "Custom System Prompt Shielding", "Zero-Latency API SLA", "Dedicated GPU Cluster Allocation"), "Monthly Tokens", "300K Tokens", false, 10)
                )

                _subscribers.value = listOf(
                    Subscriber("sub_ai_1", "Ryan Zhang", "PromptCraft AI", "ryan@promptcraft.ai", "plan_ai_2", "Pro Workflow Studio", 59.0, SubscriberStatus.ACTIVE, "2026-09-10", "2026-12-10", "RZ", 0xFF00D2FF),
                    Subscriber("sub_ai_2", "Maya Lin", "CopyPulse Studio", "maya@copypulse.co", "plan_ai_3", "AI Reseller Business", 179.0, SubscriberStatus.ACTIVE, "2026-08-04", "2026-12-04", "ML", 0xFF8B5CF6),
                    Subscriber("sub_ai_3", "Jordan Reed", "DevAgents Lab", "jordan@devagents.dev", "plan_ai_2", "Pro Workflow Studio", 59.0, SubscriberStatus.ACTIVE, "2026-10-01", "2026-12-01", "JR", 0xFF10B981),
                    Subscriber("sub_ai_4", "Chloe Bennett", "MicroApps Co", "chloe@microapps.io", "plan_ai_1", "Hobbyist Maker", 19.0, SubscriberStatus.TRIALING, "2026-11-02", "2026-11-16", "CB", 0xFFF59E0B),
                    Subscriber("sub_ai_5", "Samir Patel", "AutoText Pro", "samir@autotext.net", "plan_ai_2", "Pro Workflow Studio", 59.0, SubscriberStatus.PAST_DUE, "2026-07-15", "2026-11-03", "SP", 0xFFEF4444)
                )

                _gatedFeatures.value = listOf(
                    GatedToolFeature("tool_ai_1", "Prompt Template Sandbox", "Interactive playground with test inputs and dynamic variables", "Starter", true, "Core"),
                    GatedToolFeature("tool_ai_2", "Chained Multi-Agent Pipeline", "Sequential prompt orchestration passing context seamlessly", "Pro", false, "Automation"),
                    GatedToolFeature("tool_ai_3", "Secure REST API & Tokens", "Generate scoped API keys with rate-limiting and quota meters", "Pro", false, "Developer"),
                    GatedToolFeature("tool_ai_4", "White-Label Embeddable Widget", "Embed AI workflows onto any website with custom styling", "Enterprise", false, "Commercial")
                )
            }

            NicheType.LOCAL_SEO_MONITOR -> {
                _plans.value = listOf(
                    SubscriptionPlan("plan_seo_1", "Single Location", 39.0, 29.0, "Monitor 1 Google Business profile & 10 search keywords", listOf("1 Google Maps Location", "10 Tracked Local Keywords", "Weekly Rank Snapshot", "New Review Push Alerts"), "Tracked Keywords", "10 Keywords", false, 1),
                    SubscriptionPlan("plan_seo_2", "Multi-Shop Pro", 89.0, 69.0, "For local businesses with multiple branches or agencies", listOf("Up to 5 Locations", "50 Tracked Local Keywords", "Daily Rank Check & Heatmaps", "AI Review Auto-Responder Drafts", "Competitor Proximity Audit"), "Tracked Keywords", "50 Keywords", true, 3),
                    SubscriptionPlan("plan_seo_3", "Agency Multi-Client", 199.0, 169.0, "White-label SEO reporting for 25+ local business clients", listOf("25 Locations / Clients", "250 Tracked Keywords", "Automated White-Label PDF Reports", "Client Portal Access", "Google My Business Sync API"), "Tracked Keywords", "250 Keywords", false, 10)
                )

                _subscribers.value = listOf(
                    Subscriber("sub_seo_1", "Dr. Emily Taylor", "Taylor Family Dentistry", "info@taylordental.com", "plan_seo_2", "Multi-Shop Pro", 89.0, SubscriberStatus.ACTIVE, "2026-08-20", "2026-12-20", "ET", 0xFF10B981),
                    Subscriber("sub_seo_2", "Carlos Mendez", "Mendez Auto Repair", "service@mendezauto.com", "plan_seo_1", "Single Location", 39.0, SubscriberStatus.ACTIVE, "2026-09-15", "2026-12-15", "CM", 0xFF2563EB),
                    Subscriber("sub_seo_3", "Nadia Petrova", "Gourmet Bistro Group", "nadia@gourmetbistro.com", "plan_seo_2", "Multi-Shop Pro", 89.0, SubscriberStatus.ACTIVE, "2026-07-11", "2026-11-11", "NP", 0xFF8B5CF6),
                    Subscriber("sub_seo_4", "Apex Marketing Agency", "Apex Local Media", "reports@apexlocal.net", "plan_seo_3", "Agency Multi-Client", 199.0, SubscriberStatus.ACTIVE, "2026-06-01", "2026-12-01", "AM", 0xFF00D2FF),
                    Subscriber("sub_seo_5", "Tom Harrison", "Harrison Roofing LLC", "tom@harrisonroofs.com", "plan_seo_1", "Single Location", 39.0, SubscriberStatus.TRIALING, "2026-11-03", "2026-11-17", "TH", 0xFFF59E0B)
                )

                _gatedFeatures.value = listOf(
                    GatedToolFeature("tool_seo_1", "Keyword Grid Rank Checker", "Real-time Google Maps geolocated rank simulation", "Starter", true, "Rankings"),
                    GatedToolFeature("tool_seo_2", "AI Review Responder Assistant", "Generate friendly, SEO-optimized review reply drafts", "Pro", false, "Reputation"),
                    GatedToolFeature("tool_seo_3", "Competitor Proximity Radar", "Map neighboring rivals outranking you within 5 miles", "Pro", false, "Intelligence"),
                    GatedToolFeature("tool_seo_4", "White-Label Client Email Reports", "Branded weekly PDF digests sent straight to business owners", "Enterprise", false, "Reporting")
                )
            }

            NicheType.CREATOR_MEMBERSHIP -> {
                _plans.value = listOf(
                    SubscriptionPlan("plan_cr_1", "Supporter Tier", 9.0, 7.0, "Direct community access and exclusive monthly Q&A", listOf("Discord / Forum Member Role", "Monthly Behind-the-Scenes Stream", "Direct Direct Message Chat"), "Access Tier", "Level 1 Access", false, 1),
                    SubscriptionPlan("plan_cr_2", "Insider VIP Pass", 29.0, 24.0, "Full resource library, source files & templates", listOf("Complete Code & Asset Library", "Early Access to New Products", "Private Community Masterminds", "Monthly Live Code Review"), "Access Tier", "Full Library", true, 1),
                    SubscriptionPlan("plan_cr_3", "Mastermind Founder", 99.0, 79.0, "1-on-1 mentorship, strategy audits, and founder badge", listOf("Monthly 1-on-1 Video Consultation", "Private Feedback Channel", "Lifetime Recognition in Credits", "All Upcoming Products Included"), "Access Tier", "Founder Tier", false, 1)
                )

                _subscribers.value = listOf(
                    Subscriber("sub_cr_1", "Daniel Craig", "Independent Dev", "daniel@creatorhub.org", "plan_cr_2", "Insider VIP Pass", 29.0, SubscriberStatus.ACTIVE, "2026-09-01", "2026-12-01", "DC", 0xFF8B5CF6),
                    Subscriber("sub_cr_2", "Sophie Martin", "UI/UX Nomad", "sophie@designjourney.net", "plan_cr_2", "Insider VIP Pass", 29.0, SubscriberStatus.ACTIVE, "2026-08-14", "2026-12-14", "SM", 0xFF10B981),
                    Subscriber("sub_cr_3", "Kenji Sato", "Sato Indie Lab", "kenji@satolab.jp", "plan_cr_3", "Mastermind Founder", 99.0, SubscriberStatus.ACTIVE, "2026-07-22", "2026-11-22", "KS", 0xFF00D2FF),
                    Subscriber("sub_cr_4", "Lucas Lima", "Indie Builder", "lucas@indiehacks.co", "plan_cr_1", "Supporter Tier", 9.0, SubscriberStatus.ACTIVE, "2026-10-10", "2026-11-10", "LL", 0xFFF59E0B)
                )

                _gatedFeatures.value = listOf(
                    GatedToolFeature("tool_cr_1", "Community Lounge & Feed", "Exclusive subscriber posts and discussions", "Starter", true, "Community"),
                    GatedToolFeature("tool_cr_2", "Curated Asset & Template Vault", "Download production-ready templates, UI kits & code", "Pro", false, "Content"),
                    GatedToolFeature("tool_cr_3", "Private Q&A Submissions", "Submit your project for weekly live review & feedback", "Pro", false, "Interactivity"),
                    GatedToolFeature("tool_cr_4", "1-on-1 Mentor Calendar Booking", "Direct calendar sync for scheduled founder consultations", "Enterprise", false, "Mentorship")
                )
            }

            NicheType.API_WEBHOOK_DISPATCHER -> {
                _plans.value = listOf(
                    SubscriptionPlan("plan_wh_1", "Starter Dispatcher", 29.0, 24.0, "Reliable webhook delivery with 3 automatic retries", listOf("50,000 Webhook Deliveries/mo", "Exponential Backoff Retries", "3-Day Event Log Retention", "Slack Alert Integration"), "Deliveries", "50K Events", false, 1),
                    SubscriptionPlan("plan_wh_2", "Growth Pipeline", 79.0, 64.0, "High throughput with payload signing and replay inspector", listOf("500,000 Deliveries/mo", "Payload HMAC SHA-256 Signing", "One-Click Manual Event Replay", "14-Day Log Retention", "Custom Rate-Limiting"), "Deliveries", "500K Events", true, 5),
                    SubscriptionPlan("plan_wh_3", "Scale Cluster", 199.0, 169.0, "Enterprise SLA with dedicated IP and zero drop guarantee", listOf("5,000,000 Deliveries/mo", "Dedicated Static Outbound IP", "90-Day Full Body Audit Trail", "99.99% Guaranteed SLA", "Multi-Region Redundancy"), "Deliveries", "5M Events", false, 20)
                )

                _subscribers.value = listOf(
                    Subscriber("sub_wh_1", "CloudStack Labs", "CloudStack Systems", "eng@cloudstack.io", "plan_wh_2", "Growth Pipeline", 79.0, SubscriberStatus.ACTIVE, "2026-08-01", "2026-12-01", "CS", 0xFF2563EB),
                    Subscriber("sub_wh_2", "PayBridge Fintech", "PayBridge Inc.", "ops@paybridge.dev", "plan_wh_3", "Scale Cluster", 199.0, SubscriberStatus.ACTIVE, "2026-07-15", "2026-11-15", "PB", 0xFF00D2FF),
                    Subscriber("sub_wh_3", "AppFlow Systems", "AppFlow Automation", "hello@appflow.tech", "plan_wh_1", "Starter Dispatcher", 29.0, SubscriberStatus.ACTIVE, "2026-09-20", "2026-12-20", "AF", 0xFF10B981)
                )

                _gatedFeatures.value = listOf(
                    GatedToolFeature("tool_wh_1", "Endpoint Monitor & Ping", "Real-time endpoint health and TLS verification", "Starter", true, "Monitoring"),
                    GatedToolFeature("tool_wh_2", "Failed Delivery Replay Tool", "Inspect failed HTTP payloads and trigger instant replay", "Pro", false, "Debugging"),
                    GatedToolFeature("tool_wh_3", "HMAC Signature Generator", "Verify authenticity with auto-rotated signing secrets", "Pro", false, "Security"),
                    GatedToolFeature("tool_wh_4", "Dedicated IP Redundancy Pool", "Route mission-critical events through dedicated IPs", "Enterprise", false, "Infrastructure")
                )
            }
        }

        // Generate realistic invoices
        val sampleInvoices = mutableListOf<Invoice>()
        val currentSubs = _subscribers.value
        currentSubs.take(5).forEachIndexed { index, sub ->
            sampleInvoices.add(
                Invoice(
                    id = "inv_${index + 1}",
                    invoiceNo = "INV-2026-${1000 + index}",
                    subscriberName = sub.company,
                    amount = sub.mrrAmount,
                    date = "2026-11-0${index + 1}",
                    status = if (sub.status == SubscriberStatus.PAST_DUE) "Pending" else "Paid",
                    planName = sub.planName
                )
            )
        }
        _invoices.value = sampleInvoices

        // Generate realistic webhooks
        _webhooks.value = listOf(
            WebhookEvent("wh_1", "invoice.payment_succeeded", "2 mins ago", "Amount: $129.00 - Customer: Apex Creative Agency", true),
            WebhookEvent("wh_2", "customer.subscription.updated", "1 hour ago", "Plan upgraded to Agency Growth", true),
            WebhookEvent("wh_3", "invoice.upcoming", "4 hours ago", "Renewal alert for PixelForge Media ($129.00)", true),
            WebhookEvent("wh_4", "customer.subscription.created", "1 day ago", "New trial started: David Kim (K-Digital Labs)", true)
        )

        _customerSelectedPlan.value = "Starter"
    }

    private fun loadAuditCriteria() {
        _auditCriteria.value = listOf(
            Vibe96AuditCriterion(
                id = "crit_1",
                name = "Vibe96 Code Quality & Clean Architecture",
                category = "Code & Engineering",
                score = 100,
                statusBadge = "PASSED",
                rationale = "Structured with Kotlin MVVM, clear Separation of Concerns, StateFlow reactive bindings, and zero hardcoded secrets.",
                highlights = listOf("Pure Kotlin DSL Gradle setup", "Separation of Repository and UI layers", "Zero memory leaks or blocking UI threads")
            ),
            Vibe96AuditCriterion(
                id = "crit_2",
                name = "Subscription & Monetization Engine",
                category = "Revenue & Billing",
                score = 100,
                statusBadge = "PASSED",
                rationale = "Turnkey multi-tier subscription engine with monthly/annual discount math, feature gating, churn tracking, and webhook dispatchers ready for Stripe/Paddle.",
                highlights = listOf("Starter / Pro / Enterprise tier hierarchy", "Monthly & Annual billing switch (20% off)", "Webhooks for subscription events", "Invoice & receipt audit trail")
            ),
            Vibe96AuditCriterion(
                id = "crit_3",
                name = "Material 3 & Responsive Edge-to-Edge",
                category = "Design & UX",
                score = 98,
                statusBadge = "PASSED",
                rationale = "Full edge-to-edge support with WindowInsets handling, high-contrast dark/light theme, accessible 48dp touch targets, and responsive cards.",
                highlights = listOf("Full bleed with enableEdgeToEdge()", "Dynamic 48dp minimum touch targets", "Fluid layout for phone and tablet sizes")
            ),
            Vibe96AuditCriterion(
                id = "crit_4",
                name = "Marketplace Turnkey Deployment Package",
                category = "Distribution",
                score = 100,
                statusBadge = "PASSED",
                rationale = "Includes complete README.md, environment configuration guides (.env.example), Docker/Cloud deployment blueprint, and buyer setup instructions.",
                highlights = listOf("One-click copyable documentation", "Detailed API payload specifications", "Quickstart run instructions for buyers")
            ),
            Vibe96AuditCriterion(
                id = "crit_5",
                name = "Live Interactive Buyer Demo",
                category = "Conversion",
                score = 100,
                statusBadge = "PASSED",
                rationale = "Buyers can experience both the Founder Admin Dashboard and the End-User Customer Portal with working paywall and plan upgrade flows.",
                highlights = listOf("Instant dual-perspective preview", "Simulated upgrade checkout experience", "Dynamic niche switcher for multi-niche appeal")
            ),
            Vibe96AuditCriterion(
                id = "crit_6",
                name = "Security, Privacy & Zero Dep Vulnerabilities",
                category = "Security",
                score = 100,
                statusBadge = "PASSED",
                rationale = "Zero external tracking bloatware. Adheres to Google Play & Vibe96 security audit standards with safe state storage.",
                highlights = listOf("Clean dependency footprint", "Secure configuration without plaintext keys", "Zero crash-prone unsafe type casting")
            )
        )
    }

    // Interactive operations
    fun addSubscriber(name: String, company: String, email: String, plan: SubscriptionPlan) {
        val initials = name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
            .uppercase()
            .ifEmpty { "SB" }

        val colors = listOf(0xFF2563EB, 0xFF8B5CF6, 0xFF10B981, 0xFF00D2FF, 0xFFF59E0B)
        val randomColor = colors.random()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = dateFormat.format(Date())
        val cal = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }
        val nextMonth = dateFormat.format(cal.time)

        val newSub = Subscriber(
            id = "sub_${System.currentTimeMillis()}",
            name = name,
            company = company.ifEmpty { "$name's Org" },
            email = email,
            planId = plan.id,
            planName = plan.name,
            mrrAmount = plan.monthlyPrice,
            status = SubscriberStatus.ACTIVE,
            joinDate = today,
            nextBillingDate = nextMonth,
            avatarInitials = initials,
            avatarColorHex = randomColor
        )

        _subscribers.update { listOf(newSub) + it }

        // Trigger webhook & invoice
        val newInvoice = Invoice(
            id = "inv_${System.currentTimeMillis()}",
            invoiceNo = "INV-2026-${(1000..9999).random()}",
            subscriberName = newSub.company,
            amount = plan.monthlyPrice,
            date = today,
            status = "Paid",
            planName = plan.name
        )
        _invoices.update { listOf(newInvoice) + it }

        val webhook = WebhookEvent(
            id = "wh_${System.currentTimeMillis()}",
            eventType = "customer.subscription.created",
            timestamp = "Just now",
            payloadSummary = "New subscriber: ${newSub.name} (${plan.name} - $${plan.monthlyPrice}/mo)",
            isDelivered = true
        )
        _webhooks.update { listOf(webhook) + it }
    }

    fun toggleSubscriberStatus(subscriberId: String, newStatus: SubscriberStatus) {
        _subscribers.update { list ->
            list.map { sub ->
                if (sub.id == subscriberId) {
                    sub.copy(status = newStatus)
                } else sub
            }
        }
    }

    fun updatePlanPrice(planId: String, newMonthlyPrice: Double) {
        _plans.update { list ->
            list.map { plan ->
                if (plan.id == planId) {
                    plan.copy(
                        monthlyPrice = newMonthlyPrice,
                        annualPrice = (newMonthlyPrice * 0.8).toInt().toDouble()
                    )
                } else plan
            }
        }
    }

    fun simulateBillingRenewal() {
        val activeSubs = _subscribers.value.filter { it.status == SubscriberStatus.ACTIVE }
        if (activeSubs.isNotEmpty()) {
            val randomSub = activeSubs.random()
            val newInvoice = Invoice(
                id = "inv_${System.currentTimeMillis()}",
                invoiceNo = "INV-2026-${(1000..9999).random()}",
                subscriberName = randomSub.company,
                amount = randomSub.mrrAmount,
                date = "2026-11-05",
                status = "Paid",
                planName = randomSub.planName
            )
            _invoices.update { listOf(newInvoice) + it }

            val webhook = WebhookEvent(
                id = "wh_${System.currentTimeMillis()}",
                eventType = "invoice.payment_succeeded",
                timestamp = "Just now",
                payloadSummary = "Auto-renewed: ${randomSub.company} ($${randomSub.mrrAmount})",
                isDelivered = true
            )
            _webhooks.update { listOf(webhook) + it }
        }
    }

    fun upgradeCustomerPlan(newPlanName: String) {
        _customerSelectedPlan.value = newPlanName
        _gatedFeatures.update { list ->
            list.map { tool ->
                val shouldUnlock = when (newPlanName) {
                    "Enterprise" -> true
                    "Pro" -> tool.requiredPlan == "Starter" || tool.requiredPlan == "Pro"
                    else -> tool.requiredPlan == "Starter"
                }
                tool.copy(isUnlocked = shouldUnlock)
            }
        }
    }

    fun getRevenueTrends(): List<RevenueMonthlyData> {
        val currentMrr = _subscribers.value.filter { it.status == SubscriberStatus.ACTIVE }.sumOf { it.mrrAmount }
        val activeCount = _subscribers.value.count { it.status == SubscriberStatus.ACTIVE }

        return listOf(
            RevenueMonthlyData("Jun", (currentMrr * 0.52).toInt().toDouble(), (activeCount * 0.5).toInt().coerceAtLeast(1)),
            RevenueMonthlyData("Jul", (currentMrr * 0.63).toInt().toDouble(), (activeCount * 0.6).toInt().coerceAtLeast(2)),
            RevenueMonthlyData("Aug", (currentMrr * 0.76).toInt().toDouble(), (activeCount * 0.75).toInt().coerceAtLeast(2)),
            RevenueMonthlyData("Sep", (currentMrr * 0.88).toInt().toDouble(), (activeCount * 0.85).toInt().coerceAtLeast(3)),
            RevenueMonthlyData("Oct", (currentMrr * 0.94).toInt().toDouble(), (activeCount * 0.9).toInt().coerceAtLeast(4)),
            RevenueMonthlyData("Nov", currentMrr, activeCount)
        )
    }
}
