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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GatedToolFeature
import com.example.ui.theme.*
import com.example.ui.viewmodel.SaaSViewModel

@Composable
fun CustomerPortalScreen(
    viewModel: SaaSViewModel,
    modifier: Modifier = Modifier
) {
    val currentNiche by viewModel.currentNiche.collectAsState()
    val gatedFeatures by viewModel.gatedFeatures.collectAsState()
    val customerSelectedPlan by viewModel.customerSelectedPlan.collectAsState()
    val invoices by viewModel.invoices.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Perspective Toggle Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    SecondaryViolet.copy(alpha = 0.12f),
                                    PrimaryCyan.copy(alpha = 0.05f)
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
                                color = SecondaryViolet.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = SecondaryPurple,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "END-USER CLIENT PERSPECTIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(
                                color = when (customerSelectedPlan) {
                                    "Enterprise" -> SuccessGreen.copy(alpha = 0.2f)
                                    "Pro" -> PrimaryCyan.copy(alpha = 0.2f)
                                    else -> WarningAmber.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$customerSelectedPlan Plan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when (customerSelectedPlan) {
                                        "Enterprise" -> SuccessGreen
                                        "Pro" -> PrimaryCyan
                                        else -> WarningAmber
                                    },
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Customer App & Paywall Experience",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Text(
                            text = "This simulates how your end customers interact with the ${currentNiche.title}. Click any locked feature below to experience the conversion paywall.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.upgradeCustomerPlan("Starter") },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (customerSelectedPlan == "Starter") PrimaryCyan.copy(alpha = 0.15f) else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Starter", style = MaterialTheme.typography.labelSmall)
                            }

                            OutlinedButton(
                                onClick = { viewModel.upgradeCustomerPlan("Pro") },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (customerSelectedPlan == "Pro") PrimaryCyan.copy(alpha = 0.15f) else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Pro", style = MaterialTheme.typography.labelSmall)
                            }

                            OutlinedButton(
                                onClick = { viewModel.upgradeCustomerPlan("Enterprise") },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (customerSelectedPlan == "Enterprise") PrimaryCyan.copy(alpha = 0.15f) else Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Enterprise", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }

        // Section: Niche Specific Gated Modules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Niche SaaS Modules & Entitlements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Text(
                    text = "Feature Gated",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryCyan
                )
            }
        }

        items(gatedFeatures, key = { it.id }) { tool ->
            GatedFeatureCard(
                feature = tool,
                onFeatureClick = {
                    if (!tool.isUnlocked) {
                        viewModel.openPaywallForFeature(tool)
                    } else {
                        viewModel.showFeedback("Module '${tool.title}' is active and operational!")
                    }
                }
            )
        }

        // End-User Invoices & Billing Receipt
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Customer Billing History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Text(
                    text = "Auto Receipts",
                    style = MaterialTheme.typography.labelSmall,
                    color = SuccessGreen
                )
            }
        }

        items(invoices.take(3)) { invoice ->
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
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = invoice.invoiceNo,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "${invoice.date} • ${invoice.planName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${invoice.amount.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                        Text(
                            text = "PAID",
                            style = MaterialTheme.typography.labelSmall,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GatedFeatureCard(
    feature: GatedToolFeature,
    onFeatureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (feature.isUnlocked) SuccessGreen.copy(alpha = 0.4f) else BorderSubtle

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onFeatureClick() }
            .testTag("gated_tool_${feature.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (feature.isUnlocked) SuccessGreen.copy(alpha = 0.15f)
                        else SurfaceVariantDark
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (feature.isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (feature.isUnlocked) SuccessGreen else TextMutedDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = feature.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Surface(
                        color = if (feature.isUnlocked) SuccessGreen.copy(alpha = 0.15f) else PrimaryBlue.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (feature.isUnlocked) "UNLOCKED" else "REQUIRES ${feature.requiredPlan.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (feature.isUnlocked) SuccessGreen else PrimaryLightBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark
                )
            }

            Icon(
                imageVector = if (feature.isUnlocked) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = if (feature.isUnlocked) SuccessGreen else PrimaryCyan,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
