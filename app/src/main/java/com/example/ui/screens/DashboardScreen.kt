package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NicheType
import com.example.data.model.SubscriberStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.SaaSViewModel

@Composable
fun DashboardScreen(
    viewModel: SaaSViewModel,
    modifier: Modifier = Modifier
) {
    val currentNiche by viewModel.currentNiche.collectAsState()
    val metrics by viewModel.metrics.collectAsState()
    val webhooks by viewModel.webhooks.collectAsState()
    val invoices by viewModel.invoices.collectAsState()
    val trends = viewModel.getRevenueTrends()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero SaaS Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(PrimaryCyan, SecondaryViolet)),
                        RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    PrimaryCyan.copy(alpha = 0.08f),
                                    SecondaryViolet.copy(alpha = 0.04f)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = PrimaryCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = currentNiche.iconEmoji, fontSize = 14.sp)
                                    Text(
                                        text = "LIVE SAAS ENGINE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PrimaryCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(
                                color = SecondaryViolet.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "Vibe96 Audited 100%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SecondaryPurple,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentNiche.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Text(
                            text = currentNiche.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "MONTHLY MRR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMutedDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$${metrics.mrr.toInt()}/mo",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = PrimaryCyan
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(38.dp)
                                    .width(1.dp)
                                    .background(BorderSubtle)
                                    .align(Alignment.CenterVertically)
                            )

                            Column {
                                Text(
                                    text = "ANNUALIZED ARR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMutedDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$${metrics.arr.toInt()}/yr",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 Key SaaS Metric Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Active Subscriptions",
                        value = "${metrics.activeCount}",
                        subtitle = "${metrics.trialCount} on free trial",
                        icon = Icons.Default.People,
                        iconTint = PrimaryLightBlue,
                        trendBadge = "+18% MoM",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Churn Rate",
                        value = "${String.format("%.1f", metrics.churnRatePercent)}%",
                        subtitle = "${metrics.churnedCount} canceled",
                        icon = Icons.Default.SyncProblem,
                        iconTint = if (metrics.churnRatePercent < 15.0) SuccessGreen else WarningAmber,
                        trendBadge = "Optimal",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Avg Revenue / User",
                        value = "$${metrics.arpu.toInt()}",
                        subtitle = "Per subscriber average",
                        icon = Icons.Default.AttachMoney,
                        iconTint = PrimaryCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Customer LTV",
                        value = "$${metrics.estimatedLtv.toInt()}",
                        subtitle = "Projected lifetime value",
                        icon = Icons.Default.Verified,
                        iconTint = SecondaryPurple,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Action Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Quick Engine Actions",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openAddSubscriberDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_add_subscriber_dashboard"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color(0xFF002236),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Sub",
                                color = Color(0xFF002236),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.simulateBillingRenewal() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryDark),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_simulate_renewal"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Simulate Pay",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.selectTab(AppTab.VIBE96) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SecondaryPurple),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_vibe96_shortcut"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = SecondaryViolet,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Vibe96 Kit",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        // MRR Growth Trend Chart
        item {
            RevenueTrendChart(data = trends)
        }

        // Live Webhooks & Event Stream
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Webhook & Billing Stream",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Text(
                    text = "Stripe Ready",
                    style = MaterialTheme.typography.labelSmall,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(webhooks.take(4)) { webhook ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                if (webhook.eventType.contains("payment")) SuccessGreen.copy(alpha = 0.15f)
                                else PrimaryCyan.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (webhook.eventType.contains("payment")) Icons.Default.Payment else Icons.Default.Sync,
                            contentDescription = null,
                            tint = if (webhook.eventType.contains("payment")) SuccessGreen else PrimaryCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = webhook.eventType,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = webhook.timestamp,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMutedDark
                            )
                        }
                        Text(
                            text = webhook.payloadSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
