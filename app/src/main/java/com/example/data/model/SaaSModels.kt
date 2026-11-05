package com.example.data.model

enum class NicheType(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val targetAudience: String,
    val valueProposition: String
) {
    AGENCY_RETAINER(
        title = "Agency Retainer & Client Hub",
        subtitle = "Automated recurring retainer billing & deliverables portal for B2B agencies",
        iconEmoji = "💼",
        targetAudience = "Digital Marketing Agencies, Freelance Consultants",
        valueProposition = "Lock in predictable MRR with auto-invoicing and client milestones"
    ),
    AI_MICRO_TOOL(
        title = "AI Prompt & Workflow Micro-SaaS",
        subtitle = "Monetize specialized AI prompt workflows, tokens & automation APIs",
        iconEmoji = "⚡",
        targetAudience = "Solopreneurs, Copywriters & AI Builders",
        valueProposition = "Subscription-gated AI prompt chains with token usage limits"
    ),
    LOCAL_SEO_MONITOR(
        title = "Local SEO & Review Monitor",
        subtitle = "Automated Google Maps rank tracking & review alert reports for local shops",
        iconEmoji = "📍",
        targetAudience = "Local SMBs, Dentists, Restaurants, Contractors",
        valueProposition = "Sell $79-$199/mo ongoing local search & reputation surveillance"
    ),
    CREATOR_MEMBERSHIP(
        title = "Creator Community & Perks Hub",
        subtitle = "Private community access, downloadable templates & recurring subscriber perks",
        iconEmoji = "🎨",
        targetAudience = "Course Creators, YouTubers, Newsletter Authors",
        valueProposition = "Direct monthly community membership without platform cuts"
    ),
    API_WEBHOOK_DISPATCHER(
        title = "Micro-SaaS Webhook & API Gateway",
        subtitle = "Reliable webhook retry engine, API usage metering & alert dispatcher",
        iconEmoji = "🔌",
        targetAudience = "SaaS Founders, Indie Hackers, Backend Devs",
        valueProposition = "Turnkey webhook infrastructure as a $49/mo subscription"
    )
}

enum class SubscriberStatus(val label: String, val colorHex: Long) {
    ACTIVE("Active", 0xFF10B981),
    TRIALING("In Trial", 0xFFF59E0B),
    PAST_DUE("Past Due", 0xFFEF4444),
    CANCELED("Canceled", 0xFF64748B)
}

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val monthlyPrice: Double,
    val annualPrice: Double,
    val description: String,
    val features: List<String>,
    val quotaLabel: String,
    val quotaValue: String,
    val isPopular: Boolean = false,
    val maxSeats: Int = 1
)

data class Subscriber(
    val id: String,
    val name: String,
    val company: String,
    val email: String,
    val planId: String,
    val planName: String,
    val mrrAmount: Double,
    val status: SubscriberStatus,
    val joinDate: String,
    val nextBillingDate: String,
    val avatarInitials: String,
    val avatarColorHex: Long
)

data class Invoice(
    val id: String,
    val invoiceNo: String,
    val subscriberName: String,
    val amount: Double,
    val date: String,
    val status: String, // "Paid", "Pending", "Refunded"
    val planName: String
)

data class WebhookEvent(
    val id: String,
    val eventType: String,
    val timestamp: String,
    val payloadSummary: String,
    val isDelivered: Boolean = true
)

data class GatedToolFeature(
    val id: String,
    val title: String,
    val description: String,
    val requiredPlan: String, // "Starter", "Pro", "Enterprise"
    val isUnlocked: Boolean,
    val category: String
)

data class Vibe96AuditCriterion(
    val id: String,
    val name: String,
    val category: String,
    val score: Int,
    val maxScore: Int = 100,
    val statusBadge: String,
    val rationale: String,
    val highlights: List<String>
)

data class RevenueMonthlyData(
    val monthName: String,
    val mrr: Double,
    val subscriberCount: Int
)
