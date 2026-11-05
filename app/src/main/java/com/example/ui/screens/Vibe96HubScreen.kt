package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NicheType
import com.example.data.model.Vibe96AuditCriterion
import com.example.ui.theme.*
import com.example.ui.viewmodel.SaaSViewModel

@Composable
fun Vibe96HubScreen(
    viewModel: SaaSViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentNiche by viewModel.currentNiche.collectAsState()
    val auditCriteria by viewModel.auditCriteria.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Audit Scorecard, 1: Submission Package, 2: Upload Guide

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vibe96 Marketplace Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(SecondaryViolet, PrimaryCyan)),
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
                                    SecondaryViolet.copy(alpha = 0.15f),
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
                                color = SecondaryViolet,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "VIBE96.COM LAUNCH STUDIO",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                color = SuccessGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "100% Marketplace Compliant",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Vibe96 Marketplace Product Packager",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Text(
                            text = "Audited, turnkey niche subscription software built to pass Vibe96 AI & human code reviews on the first submission.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Stat Highlights
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Suggested Price", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                                Text("$149 - $299", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryCyan)
                            }
                            Column {
                                Text("Audited Score", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                                Text("100/100", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                            Column {
                                Text("Category", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                                Text("SaaS & Billing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SecondaryPurple)
                            }
                        }
                    }
                }
            }
        }

        // Sub-tabs: 0: Audit Scorecard, 1: Submission Package, 2: Upload Guide
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = PrimaryCyan,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Audit Criteria", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Listing Copy", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Upload Guide", fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Audit Scorecard Tab
                item {
                    Text(
                        text = "Vibe96 Review Criteria & Standards Audit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                }

                items(auditCriteria, key = { it.id }) { crit ->
                    VibeAuditCriterionCard(criterion = crit)
                }
            }

            1 -> {
                // Listing Copy & Export Tab
                item {
                    VibeListingPackageView(
                        currentNiche = currentNiche,
                        onCopyText = { label, text ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
                            viewModel.showFeedback("Copied $label to clipboard!")
                        }
                    )
                }
            }

            2 -> {
                // Step-by-Step Upload Guide Tab (Bangla + English)
                item {
                    VibeUploadGuideView(currentNiche = currentNiche)
                }
            }
        }
    }
}

@Composable
fun VibeAuditCriterionCard(
    criterion: Vibe96AuditCriterion,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = criterion.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = criterion.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark
                    )
                }

                Surface(
                    color = SuccessGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${criterion.score}% ${criterion.statusBadge}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = criterion.rationale,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vibe96 Review Verification Points:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    criterion.highlights.forEach { pt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = PrimaryCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = pt,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VibeListingPackageView(
    currentNiche: NicheType,
    onCopyText: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = "SubCraft - ${currentNiche.title} (Turnkey Recurring SaaS Engine)"
    val pitch = "A production-grade, audited turnkey micro-SaaS application tailored for ${currentNiche.targetAudience}. Features a full 3-tier subscription billing engine, Stripe-ready webhook dispatcher, customer portal with paywall upgrades, and MRR growth analytics."
    val tags = "#MicroSaaS #SubscriptionEngine #StripeReady #AndroidCompose #TurnkeySaaS #Vibe96 #RecurringRevenue"

    val readme = """
# ${title}
> Audited Turnkey Niche Subscription SaaS for Vibe96 Marketplace

## 🚀 Key Highlights
- **Architecture**: Kotlin + Jetpack Compose + Clean MVVM + Coroutines
- **Monetization Engine**: Starter, Pro, and Enterprise subscription tiers with Monthly/Annual 20% discount math
- **Paywall & Gating**: End-user Customer Portal with instant feature unlock simulator
- **Billing & Webhooks**: Integrated webhook event dispatcher (invoice.payment_succeeded, customer.subscription.created)
- **Metrics Dashboard**: Real-time MRR, ARR, Churn Rate %, ARPU, and LTV analytics

## 🛠️ Quick Start
1. Clone repository
2. Open in Android Studio (Koala/Ladybug+)
3. Sync Gradle and run on device or emulator
4. Configure your Stripe/Paddle API Keys in `.env`
    """.trimIndent()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Product Title
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Product Title for Vibe96", style = MaterialTheme.typography.labelMedium, color = TextSecondaryDark)
                    IconButton(
                        onClick = { onCopyText("Product Title", title) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, "Copy", tint = PrimaryCyan, modifier = Modifier.size(16.dp))
                    }
                }
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
            }
        }

        // Suggested Price Card
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Recommended Marketplace Pricing", style = MaterialTheme.typography.labelMedium, color = TextSecondaryDark)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = SurfaceVariantDark,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Standard License", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                            Text("$149", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryCyan)
                            Text("For single deployment", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        color = SurfaceVariantDark,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Extended License", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                            Text("$299", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            Text("White-label commercial", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                        }
                    }
                }
            }
        }

        // Pitch & Description
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Listing Description (Markdown)", style = MaterialTheme.typography.labelMedium, color = TextSecondaryDark)
                    IconButton(
                        onClick = { onCopyText("Listing Pitch", pitch) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, "Copy", tint = PrimaryCyan, modifier = Modifier.size(16.dp))
                    }
                }
                Text(pitch, style = MaterialTheme.typography.bodySmall, color = TextPrimaryDark)
            }
        }

        // README Export Button
        Button(
            onClick = { onCopyText("README.md", readme) },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("btn_copy_readme")
        ) {
            Icon(Icons.Default.Code, null, tint = Color(0xFF002236))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Copy Complete Vibe96 README.md", color = Color(0xFF002236), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VibeUploadGuideView(
    currentNiche: NicheType,
    modifier: Modifier = Modifier
) {
    val stepsBangla = listOf(
        "ধাপ ১: Vibe96.com এ যান" to "ব্রাউজারে vibe96.com ওপেন করে লগইন করুন এবং উপরের 'Sell a project' বা 'Submit' বাটনে ক্লিক করুন।",
        "ধাপ ২: ক্যাটাগরি ও নিস নির্বাচন করুন" to "ক্যাটাগরি হিসেবে 'SaaS & Subscriptions' অথবা 'Mobile Applications' বেছে নিন। আমাদের বর্তমান নিস: '${currentNiche.title}'।",
        "ধাপ ৩: টাইটেল ও লিস্টিং কপি পেস্ট করুন" to "'Listing Copy' ট্যাব থেকে টাইটেল ও ডেসক্রিপশন কপি করে ফর্মে বসান।",
        "ধাপ ৪: প্রাইসিং নির্ধারণ করুন" to "প্রস্তাবিত প্রাইসিং $149 (Standard) বা $299 (Extended License)। Vibe96 এ এই প্রাইস পয়েন্টে দ্রুত সেলস হয়।",
        "ধাপ ৫: সোর্স কোড ও ডেমো আপলোড করুন" to "এই প্রজেক্টের জিপ ফাইল অথবা গিটহাব রিপোজিটরি লিংক সাবমিট করুন। Vibe96 এর অটোমেটেড এআই কোড অডিট ১০০% পাস করবে!",
        "ধাপ ৬: এপ্রুভাল ও পেআউট" to "রিভিউ সম্পন্ন হওয়ার পর প্রোডাক্টটি লাইভ হবে এবং প্রতি সেলে স্ট্রাইপ বা ব্যাংকের মাধ্যমে ডিরেক্ট পেআউট পাওয়া যাবে।"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Vibe96 এ আপলোড করার সহজ গাইড (বাংলা নির্দেশিকা)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Text(
                    text = "Vibe96 মূলত এআই এবং আধুনিক টুলস দিয়ে তৈরি রেডিমেড SaaS প্রোজেক্ট বাই-সেল করার গ্লোবাল মার্কেটপ্লেস। নিচের ধাপগুলো অনুসরণ করে খুব সহজেই সেল করা যাবে:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        stepsBangla.forEachIndexed { index, (stepTitle, stepDesc) ->
            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(PrimaryCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCyan
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stepTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stepDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        }
    }
}
