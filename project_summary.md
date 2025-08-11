# LittleGig – Project Summary

## Build Status
- Current: Green (assembleDebug succeeded)
- Artifact: `app/build/outputs/apk/debug/app-debug.apk`

## Toolchain
- Android Gradle Plugin: 8.4.2
- Kotlin: 1.9.24
- Java: 17 (target/source)
- Compose Compiler: 1.5.14
- compileSdk: 34, targetSdk: 34, minSdk: 24
- Firebase BoM: 33.1.2

## Recent Engineering Updates (2025-08-10)
- Models aligned (`User`, `Event`), Android 13 back handling, build stack upgraded
- Search/indexing: lowercase fields, debounce & cancel, contacts E.164 and DataStore caching
- Places key/billing verification, upload flow error surfacing
- Observability: Timber + Crashlytics breadcrumbs, Firebase Performance traces
- Chat: neumorphic UI, media uploads, replies, ticket share/redeem UI, delivery/read ticks; pinned chats; typing indicator; local chat search
- Inbox: in-app inbox with unread badge on bottom nav
- Recaps: stories-like viewer and route
- Payments: Flutterwave checkout, open via Custom Tabs

## Two-Phase Delivery (Complete)
- Phase 1
  - Email linking validation (inline errors)
  - Pinned chats UI and state sync
  - Recaps viewer + navigation entry points from Event Details
  - Payments: open `paymentUrl` via Custom Tabs; deep link skeleton
- Phase 2
  - Deep link verification: `littlegig://payment/verify?ref=...` → verifies and updates wallet
  - Tickets wallet auto-refresh + success snackbar and quick navigate to Tickets
  - Event Details: recent recaps preview row; subscribe/unsubscribe to event topic
  - Recap upload: route wired, permission prompt, proximity verification hook
  - Chat: reactions (per-user), mention parsing and highlighting

## Build & Install
- Build: `./gradlew assembleDebug`
- Install (emulator): `adb connect 127.0.0.1:6555 && adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`

## Cloud Functions (Deploy)
- Configure key: `firebase functions:config:set flutterwave.secret_key="YOUR_FLUTTERWAVE_SECRET_KEY"`
- Build & deploy:
  - From `functions/`: `npm install && npm run build`
  - Deploy all: `firebase deploy --only functions`

## Core Feature Matrix (High-level)
- Auth: anonymous-first, phone/email/Google linking
- Contacts: E.164 normalization + cache
- Search: case-insensitive, debounce/cancel
- Chat: messages, media, replies, ticket share/redeem, delivery/read ticks, typing, pinned chats, reactions, mentions, search
- Events: feed paging, event details, recaps viewer/preview, recap upload entry
- Tickets/Payments: Flutterwave checkout, deep link verify, wallet refresh, receipts endpoint
- Notifications: FCM topics (chat/event), in-app inbox badge
- Perf/Obs: custom traces, Crashlytics breadcrumbs

## Next Polishing Targets
- Payments: dedicated ticket details/QR, full receipts UI
- Recaps: actual device location (FusedLocationProvider)
- Chat: clickable mentions → profile, emoji picker for reactions
- UI: refine micro-animations and accessibility labels

---

## Consolidated TODO (Living List)

- P0 Chat Visibility and Search
  - [DONE] Unstack lists in `ChatScreen.kt`; use a single viewport with `weight(1f)` so users/results/chats render correctly
  - [DONE] Preload `allUsers` on entry to avoid empty lists by default
  - [DONE] Split "LittleGig Contacts" into a separate discovery screen with its own empty states and glass panels
  - [TODO] Strong empty states across Chat (no users, no results, no chats) with clear CTAs

- P0 Auth Flow and Splash
  - [DONE] Silent anonymous bootstrap to prevent auth screen flash (attempt anon sign-in on app start)
  - [TODO] Keep anon-by-default; move account linking emphasis to Account/Settings
  - [TODO] Phone number linking as the primary path; OTP UX polish

- P0 Design System – Glassmorphism + Neumorphism
  - [DONE] Real frosted bottom navigation with shared haze state blurring content behind it
  - [TODO] Add AGSL refraction border on supported devices; gradient border fallback on older SDKs
  - [TODO] Tokenize light/dark glass tints, shadows, radii, motion presets
  - [TODO] Convert major panels to `LiquidGlassPanel` primitives as a reusable component

- P1 Screen Overhaul (no functional removal)
  - Events Feed: keep current visual treatment; small edge polish only
  - Event Details: uplift design to liquid-glass panels, consistent chip styles, subtle neumorphic accents
  - Account: unify with new glass panels and soft-neumorphic controls
  - Inbox: convert to glass panels with category badges
  - Tickets/Receipts: ticket card with perforation and barcode/QR glass slot; receipt rows with embossed separators
  - Map: glass filter sheet with blur; floating soft-icon buttons
  - Upload/Recaps: step panels as glass cards; progress with inner sweep

- P1 Empty States, Skeletons, Micro-interactions
  - [TODO] Standard `GlassEmptyState(icon, title, message, primaryAction)` used across all modules
  - [TODO] Replace generic spinners with shimmer/pulse where appropriate

- P1 Accessibility & Theming
  - [TODO] Ensure contrast thresholds; TalkBack labels for icon-only actions
  - [TODO] Tune dark-theme shadows/borders to avoid bloom

- P1 Performance Guardrails
  - [TODO] Cap simultaneous blur surfaces; precompose heavy panels
  - [TODO] Shader capability checks with graceful fallbacks

- Cloud Functions & CLI
  - [INFO] CLI authenticated; functions project configured in `firebase.json`
  - [TODO] For any functions change: `npm --prefix functions run build` then `firebase deploy --only functions`

- Process
  - [ALWAYS] Build with `./gradlew assembleDebug` locally when possible and push to `main`
  - [ALWAYS] Update this TODO after each change with [DONE]/[TODO] markers

---

## 5-Phase Delivery Plan (Ambitious)

- Phase 1 — Foundations and Critical Fixes [IN PROGRESS]
  - [DONE] Silent anonymous bootstrap (no auth flash)
  - [DONE] Chat viewport fix (single list area + preload users)
  - [DONE] Real frosted bottom nav with shared blur
  - [DONE] Auth UI glass redesign (no logic change)
  - [TODO] Contacts discovery as separate screen

- Phase 2 — Design System Hardening
  - [DONE] Tokenize radii & motion; initial glass utilities
  - [DONE] Refraction border fallback (AGSL-ready placeholder)
  - [DONE] Standard `GlassEmptyState` with shimmer/pulse
  - [NEXT] Promote tokens into all screens; introduce AGSL runtime shader when supported

- Phase 3 — Screen Overhauls (No functional removal)
  - [DONE] Event Details header uplift (scrim + glass overlay)
  - [NEXT] Event Details content polish (chips, organizer/actions cohesion)
  - [TODO] Account unification (glass panels + soft controls)
  - [DONE] Map filter sheet with glass + category chips
  - [TODO] Inbox glass and badges; Tickets/Receipts ticket card styles; Upload/Recaps step panels

- Phase 4 — Accessibility, Performance, and Polish
  - [TODO] Contrast, TalkBack labels, large-text tests
  - [TODO] Blur budget and shader capability gating; precompose heavy panels
  - [TODO] Spacing/typography/icon consistency pass

- Phase 5 — QA, Docs, and Release Prep
  - [TODO] Snapshot tests dark/light; device matrix (26/28/31/34)
  - [TODO] Update README/DESIGN_SYSTEM with final assets
  - [TODO] Release checklist and store assets