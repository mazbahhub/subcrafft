package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubscriptionPlan
import com.example.ui.theme.*
import com.example.ui.viewmodel.SaaSViewModel

@Composable
fun PlansScreen(
    viewModel: SaaSViewModel,
    modifier: Modifier = Modifier
) {
    val plans by viewModel.plans.collectAsState()
    val isAnnual by viewModel.isAnnualBilling.collectAsState()
    val currentNiche by viewModel.currentNiche.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = PrimaryCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "RECURRING TIERS • ${currentNiche.title.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Subscription & Pricing Engine",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Text(
                    text = "Multi-tier pricing architecture ready for Stripe Checkout & Customer Portal",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Monthly / Annual Switch
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (!isAnnual) PrimaryCyan else Color.Transparent,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { if (isAnnual) viewModel.toggleBillingCycle() }
                                .testTag("btn_billing_monthly")
                        ) {
                            Text(
                                text = "Monthly Billing",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (!isAnnual) Color(0xFF002236) else TextSecondaryDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        Surface(
                            color = if (isAnnual) PrimaryCyan else Color.Transparent,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { if (!isAnnual) viewModel.toggleBillingCycle() }
                                .testTag("btn_billing_annual")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Annual",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isAnnual) Color(0xFF002236) else TextSecondaryDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = SuccessGreen,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "SAVE 20%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF003922),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Plan Cards
        items(plans, key = { it.id }) { plan ->
            PlanItemCard(
                plan = plan,
                isAnnual = isAnnual,
                onEditPrice = { viewModel.startEditPlan(plan) }
            )
        }

        // Vibe96 Monetization Tip
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Vibe96 Monetization Benchmark",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "Buyers on Vibe96 favor micro-SaaS templates with pre-configured 3-tier pricing (Free/Starter, Pro, Enterprise) and clear usage entitlement gating. This guarantees instant conversion upon launch.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanItemCard(
    plan: SubscriptionPlan,
    isAnnual: Boolean,
    onEditPrice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayPrice = if (isAnnual) plan.annualPrice else plan.monthlyPrice
    val borderModifier = if (plan.isPopular) {
        Modifier.border(
            1.5.dp,
            Brush.horizontalGradient(listOf(PrimaryCyan, SecondaryViolet)),
            RoundedCornerShape(20.dp)
        )
    } else {
        Modifier.border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier)
            .testTag("plan_card_${plan.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                if (plan.isPopular) {
                    Surface(
                        color = SecondaryViolet,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "MOST POPULAR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$${displayPrice.toInt()}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = PrimaryCyan
                )
                Text(
                    text = if (isAnnual) " /month (billed annually)" else " /month",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMutedDark,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quota Badge
            Surface(
                color = SurfaceVariantDark,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = PrimaryLightBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${plan.quotaLabel}: ${plan.quotaValue}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(14.dp))

            // Features Checklist
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimaryDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditPrice,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_edit_plan_${plan.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Price Tier", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
