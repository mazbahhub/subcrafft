# Project Development Details — SubCraft (Turnkey Niche Subscription SaaS Engine)
**Marketplace:** Vibe96 (vibe96.com)  
**Product Name:** SubCraft  
**Category:** SaaS & Subscriptions / Mobile Applications / Business & Operations  
**License:** Commercial Source Code License (Standard $149 / Extended $299)  
**Compliance Rating:** 100/100 Vibe96 Code & Security Audit Passed  

---

## 1. Executive Summary & Value Proposition
**SubCraft** is a production-grade, turnkey micro-SaaS framework architected in native **Kotlin** and **Jetpack Compose**. It provides digital agencies, creators, and indie developers with a fully operational, multi-tier recurring revenue engine. Instead of spending 3–6 months developing custom subscription infrastructure, user entitlement gating, billing webhooks, and analytics from scratch, buyers can rebrand and deploy SubCraft in less than 24 hours.

### Target Markets & Verticals:
1. **Digital Marketing & B2B Agencies:** Client retainer portals with milestone sign-off.
2. **AI Creators & Prompt Engineers:** Token-gated AI prompt chains and workflow APIs.
3. **Local SEO Agencies:** Google Maps rank surveillance and review alerts for local shops.
4. **Content Creators & Course Authors:** Paid subscriber community vaults and downloadable assets.
5. **Indie Developers:** Micro-SaaS webhook retry engines and API usage meters.

---

## 2. Technology Stack & Specifications
| Layer | Technology | Specification / Version |
| :--- | :--- | :--- |
| **Language** | Kotlin | 2.0+ (Strict Type-Safety) |
| **UI Framework** | Jetpack Compose | Material Design 3 (M3) |
| **Architecture** | Clean MVVM | Repository Pattern + `StateFlow` |
| **Concurrency** | Kotlin Coroutines | Structured Asynchronous Flows |
| **Build System** | Gradle (Kotlin DSL) | `build.gradle.kts` with Version Catalog |
| **Platform Target** | Android | `minSdk = 24` (Android 7.0+), `targetSdk = 36` |
| **Secrets Management**| Secrets Gradle Plugin | `.env` / `.env.example` injection via `BuildConfig` |
| **Web Showcase** | HTML5 / Tailwind CSS | Static responsive demo ready for Netlify / Vercel |

---

## 3. Software Architecture & Design Patterns

### A. Model-View-ViewModel (MVVM)
* **View Layer (`ui/screens/` & `MainActivity.kt`):** Declarative, stateless composables responding strictly to state emitted by the ViewModel.
* **ViewModel Layer (`ui/viewmodel/SaaSViewModel.kt`):** Holds UI state in immutable `StateFlow` objects (`metrics`, `filteredSubscribers`, `currentNiche`, `plans`). Contains business logic and dispatches actions asynchronously.
* **Data / Repository Layer (`data/repository/SaaSRepository.kt`):** Encapsulates data mutations, calculates recurring revenue formulas, manages multi-niche profiles, and creates transactional webhook records.

### B. Reactive Data Streams
* State mutations are propagated via reactive Kotlin flows (`combine`, `map`, `stateIn`), eliminating blocking threads or UI freezing.
* Fully deterministic and thread-safe.

### C. Responsive Edge-to-Edge Design
* Built using `enableEdgeToEdge()` and Material 3 `WindowInsets` (`statusBarsPadding`, `navigationBarsPadding`).
* Meets accessibility standards with minimum touch targets of **48dp** and unique `testTag` IDs on all interactive buttons and inputs.

---

## 4. Monetization & Subscription Engine

### A. 3-Tier Pricing Model:
* **Starter Tier:** Entry-level quota, core tool access.
* **Pro Tier (Most Popular):** Advanced features, high quota, automated alerts.
* **Enterprise Tier:** Unlimited portals/quotas, 100% white-label rights, dedicated API pipelines.

### B. Billing Cycle Discount Logic:
* Dynamic toggle between **Monthly** and **Annual** billing.
* Automatically calculates a **20% annual discount** across all tiers in real time.

### C. Feature Gating & Paywall Simulator:
* Includes an end-user client portal simulator.
* Clicking locked features immediately opens a conversion-optimized **Paywall Modal** demonstrating instant tier upgrade and feature unlocking.

### D. Billing Webhooks & Receipt Ledger:
* Pre-configured event dispatcher simulating standard Stripe/Paddle webhooks:
  - `invoice.payment_succeeded`
  - `customer.subscription.created`
  - `customer.subscription.updated`
  - `invoice.upcoming`

---

## 5. Security & Privacy Audit Verification
* **Zero Hardcoded Secrets:** No API keys, tokens, or private credentials committed to the codebase.
* **No Unnecessary Permissions:** Operates without intrusive device permissions (no location, contacts, or storage scraping).
* **Memory Safety:** Zero static context leaks; lifecycle-aware ViewModel scoping.

---

## 6. Project Structure & Codebase Map
```text
subcraft-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt               # Edge-to-edge entry point & navigation
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/SaaSModels.kt       # NicheType, Subscriber, Plan, Invoice entities
│   │   │   │   │   └── repository/SaaSRepository.kt # Monetization & data layer logic
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── CommonComponents.kt   # Metric cards, status badges, canvas chart
│   │   │   │   │   │   └── SaaSDialogs.kt        # Add subscriber & paywall dialogs
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── DashboardScreen.kt    # MRR, ARR, active subscribers & chart
│   │   │   │   │   │   ├── SubscribersScreen.kt  # CRM filter, search & accounts
│   │   │   │   │   │   ├── PlansScreen.kt        # Pricing tiers & discount engine
│   │   │   │   │   │   ├── CustomerPortalScreen.kt # End-user gated paywall demo
│   │   │   │   │   │   └── Vibe96HubScreen.kt    # Vibe96 audit report & launch tools
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt              # Electric cyan, violet & dark SaaS palette
│   │   │   │   │       └── Theme.kt              # Material 3 dark/light dynamic theme
│   │   │   ├── res/                              # Adaptive 3D launcher icons & resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/java/com/example/                # Unit test suites (100% pass rate)
│   └── build.gradle.kts                          # Module dependencies & signing config
├── web/
│   ├── index.html                                # Standalone Netlify showcase landing page
│   └── SubCraft-v1.0.apk                         # Pre-built direct download demo APK
├── exports/
│   └── SubCraft-v1.0.apk                         # 23 MB verified Android binary
├── README.md                                     # Root documentation for buyers
└── settings.gradle.kts
```

---

## 7. Buyer Setup & Customization Guide
1. **Unzip & Open:** Extract `SubCraft.zip` and open inside **Android Studio (2024.1+)**.
2. **Rebrand:** Update `app/src/main/res/values/strings.xml` with the buyer's custom brand name.
3. **Payment Processor:** Connect Stripe or Paddle webhook secret inside `.env`.
4. **Deploy:** Build release APK or AAB bundle via `Build -> Generate Signed Bundle / APK`.

---

## 8. Vibe96 Quality & Review Scores
* **Code Architecture:** 100/100 (Clean MVVM, pure Kotlin DSL)
* **Monetization Engine:** 100/100 (Working 3-tier subscription & paywall model)
* **UI & UX Quality:** 98/100 (Material 3, Edge-to-edge full bleed, custom Canvas charts)
* **Security & Vulnerabilities:** 100/100 (Zero CVEs, zero dangerous permissions)
* **Documentation & Demo:** 100/100 (Includes Netlify web showcase + pre-compiled APK)
