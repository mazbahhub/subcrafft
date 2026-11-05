package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.SaaSViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SubCraftApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCraftApp(
    viewModel: SaaSViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentNiche by viewModel.currentNiche.collectAsState()
    val plans by viewModel.plans.collectAsState()
    val feedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    // Dialog states
    val showAddSubscriberDialog by viewModel.showAddSubscriberDialog.collectAsState()
    val editingPlan by viewModel.editingPlan.collectAsState()
    val showPaywallModal by viewModel.showPaywallModal.collectAsState()
    val paywallTargetFeature by viewModel.paywallTargetFeature.collectAsState()

    // Auto-dismiss feedback snackbar
    LaunchedEffect(feedbackMessage) {
        if (feedbackMessage != null) {
            delay(2800)
            viewModel.clearFeedback()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .statusBarsPadding()
            ) {
                // Main Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = "SubCraft",
                                tint = Color(0xFF002236),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "SubCraft",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimaryDark
                                )
                                Surface(
                                    color = SecondaryViolet.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "VIBE96",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryPurple,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Niche Subscription SaaS Engine",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Vibe96 Quick Launch Score Badge
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.selectTab(AppTab.VIBE96) }
                            .testTag("top_vibe_score_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                            Text(
                                text = "Audit 100%",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Horizontal Niche Category Switcher
                NicheSelectorChipRow(
                    selectedNiche = currentNiche,
                    onSelectNiche = { viewModel.switchNiche(it) }
                )

                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextPrimaryDark,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val icon = when (tab) {
                        AppTab.DASHBOARD -> Icons.Default.Dashboard
                        AppTab.SUBSCRIBERS -> Icons.Default.People
                        AppTab.PLANS -> Icons.Default.Sell
                        AppTab.PORTAL -> Icons.Default.Visibility
                        AppTab.VIBE96 -> Icons.Default.RocketLaunch
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF002236),
                            selectedTextColor = PrimaryCyan,
                            indicatorColor = PrimaryCyan,
                            unselectedIconColor = TextSecondaryDark,
                            unselectedTextColor = TextSecondaryDark
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                AppTab.SUBSCRIBERS -> SubscribersScreen(viewModel = viewModel)
                AppTab.PLANS -> PlansScreen(viewModel = viewModel)
                AppTab.PORTAL -> CustomerPortalScreen(viewModel = viewModel)
                AppTab.VIBE96 -> Vibe96HubScreen(viewModel = viewModel)
            }

            // Floating feedback toast
            AnimatedVisibility(
                visible = feedbackMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(1.dp, PrimaryCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrimaryCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = feedbackMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Add Subscriber Dialog
    if (showAddSubscriberDialog) {
        AddSubscriberDialog(
            plans = plans,
            onDismiss = { viewModel.closeAddSubscriberDialog() },
            onConfirm = { name, company, email, plan ->
                viewModel.addSubscriber(name, company, email, plan)
            }
        )
    }

    // Edit Plan Price Dialog
    editingPlan?.let { plan ->
        EditPlanPriceDialog(
            plan = plan,
            onDismiss = { viewModel.closeEditPlan() },
            onConfirm = { newPrice ->
                viewModel.updatePlanPrice(plan.id, newPrice)
            }
        )
    }

    // Customer Paywall Modal Dialog
    if (showPaywallModal && paywallTargetFeature != null) {
        PaywallModalDialog(
            feature = paywallTargetFeature!!,
            onDismiss = { viewModel.closePaywall() },
            onUpgrade = { targetPlan ->
                viewModel.upgradeCustomerPlan(targetPlan)
            }
        )
    }
}
