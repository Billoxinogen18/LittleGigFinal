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

## UI Changes to Match Reference Images (2025-08-11)

- Chat visibility and layout
  - Fixed `AdvancedNeumorphicCard` inner container from `fillMaxSize()` to `fillMaxWidth()` to prevent it from covering content below, eliminating blank screen overlays in `ChatScreen` and `MainScreen` headers.
  - Restored `ChatScreen` list and search UI; removed all temporary debug backgrounds and `println` logs.
  - Ensured no loading overlays block content; loading/error state handling verified in `ChatViewModel`/`ChatDetailsViewModel`.

- Dark mode glass/neumorph tuning
  - `AdvancedNeumorphicCard`: tuned `color` and low-alpha `border` for dark theme to avoid grey cut-offs and preserve depth.
  - `AdvancedGlassmorphicCard`: adjusted base tint and border for a cleaner frosted effect in dark mode; reduced unintended purple hues on tabs/nav.

- Chat Details screen
  - Message list uses `weight(1f)` instead of `fillMaxSize()` to keep the composer visible.
  - Composer floats above the bottom navigation with sufficient bottom padding; visual offset approximates a ~3dp gap to the nav bar.
  - Reduced inter-bubble vertical spacing for denser, chat-like rhythm.
  - Removed sending shimmer/clock visuals; messages appear sent immediately with backend-confirmed status via receipts.

- Replies and jump behavior
  - Reply preview chip renders within the bubble; click scrolls to the referenced message using `LazyListState.animateScrollToItem`.
  - Added hooks to surface the referenced message content in-line (preview) with graceful fallback text while loading.

- Search parity for anonymous users
  - Anonymous users now indexed with `username_lower`, `email_lower`, and `displayName_lower` for case-insensitive search.

Notes
- Event Details Screen and Event Feed Screen visuals remain unchanged as requested; only chat-related UI was modified.

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
  - Chat: reactions (per-user), mention parsing and highlighting; reply preview enrichment restored on send; double-tap like + quick reactions bar

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

### P0 Chat — Detailed Restoration Plan (Extremely Detailed)

Owner: Chat squad. Scope: restore all previously implemented chat features with design parity to reference images, ensure stability and test coverage. Non-goals: do not modify Event Feed or Event Details content sections (only bugfixes if regressions are observed).

1) Message composer and input bar
- Behavior
  - Always visible above bottom nav; never obscured by `LazyColumn`.
  - Height auto-grows up to 4 lines; then scrolls internally.
  - Trailing actions: attach (media), ticket share, send.
- Design
  - Glass container with soft inner shadow; 3dp visual gap to nav; consistent corner radius with design tokens.
  - Icon touch targets 48dp min; hit slop considered.
- Technical
  - `LazyColumn` uses `weight(1f)`; content padding bottom >= composer height + nav height + 8dp.
  - Haptics on send; optimistic append to list with temporary localId.
- Acceptance criteria
  - Composer never overlaps with nav; no layout jump when keyboard opens.
  - Long text wraps; send enabled only when trimmed text non-empty or attachment present.
  - TalkBack reads placeholder, action labels.
- QA
  - Rotate, light/dark, small/large font sizes; emulator and device.

2) Replies: preview + jump-to
- Behavior
  - Long-press → Reply; shows compact preview (sender, snippet) above composer.
  - Bubble displays reply chip at top; clicking jumps to original message, highlights for 800–1200ms.
- Design
  - Subtle glass chip with Reply icon; 12sp sender label, 13–14sp content snippet, 1 line ellipsis.
- Technical
  - `replyToMessageId` field on message; VM resolves referenced message from in-memory cache else repository.
  - Smooth scroll using `animateScrollToItem(index)`; post-scroll highlight via transient state keyed by id.
- Acceptance criteria
  - Reply chip appears for replies; missing/orphaned references show “Message not available”.
  - Jump works across pages of messages, both directions.
- QA
  - Rapid taps; jumping while list animating; dark mode contrast of highlight ring.

3) Delivery and read receipts
- Behavior
  - Single tick: delivered to server; double ticks: delivered to recipient; filled/colored: read.
- Technical
  - Message model: `deliveredAt`, `readAt`, `readBy` (group scope).
  - Observe receipts via repository stream; debounce UI updates to avoid flicker.
  - Remove “clock” placeholders; rely on optimistic UI + server ack.
- Acceptance criteria
  - Status updates within 1–2s under normal network; no regressions offline.
  - Group chats aggregate per-recipient reads in tooltip or detail panel (phase 2 if needed).

4) Typing indicator
- Behavior
  - Shows “User is typing…” with animated dots for active counterpart(s), hides after inactivity timeout.
- Technical
  - Repo publishes typing presence; VM holds `Set<userId>` typing; 3s idle timeout.
- Acceptance criteria
  - Indicator does not persist after leaving the thread; no flapping when user sends quickly.

5) In-thread search
- Behavior
  - Search icon opens search bar; supports query, next/prev navigation, hit count.
- Technical
  - Use indexed lowercased fields; page older segments as needed; highlight matches.
- Acceptance criteria
  - Jump navigation is smooth; selection preserved when keyboard hides.

6) Pinned messages and announcements
- Behavior
  - Pin/unpin by privileged roles; pinned banner at top of thread; announcements via backend function.
- Technical
  - Cloud Functions: `pinMessage`, `setAnnouncement`; update local pin state via snapshot listener.
- Acceptance criteria
  - Pin survives refresh; pin/unpin events tracked.

7) Ticket share and redeem
- Behavior
  - Insert `TicketShareBubble` with event/ticket metadata; tap reveals actions; owner can redeem.
- Technical
  - Function `redeemTicket(ticketId)`; UI shows redeemed state and prevents dup.
- Acceptance criteria
  - Redeem success updates bubble immediately; failure shows actionable error.

8) Media attachments (phase-able)
- Behavior
  - Gallery picker, camera capture; show sending thumbnail; tap to open viewer.
- Technical
  - Upload pipeline with retries; thumbnail generation; progress UI.
- Acceptance criteria
  - Cancel upload removes placeholder; failures retry or present error.

9) Performance and memory guardrails
- Targets
  - First paint < 250ms after data ready; scroll jank < 2%; memory < 250MB.
- Techniques
  - Keyed `LazyColumn` items; image caching; limit overlapping blur surfaces; prefetch next page.
- Acceptance criteria
  - Jank metrics within threshold on mid-range device.

10) Offline and retry semantics
- Behavior
  - Messages queued offline with local ids; auto-resend on connectivity.
- Technical
  - Local persistence (Room/DataStore) for pending outbox; id reconciliation on ack.
- Acceptance criteria
  - No duplicates on reconnection; queued sends visible and tappable to cancel.

11) Observability and analytics
- Events
  - `chat_send`, `chat_reply`, `chat_jump_to_ref`, `chat_pin`, `chat_announcement_set`, `chat_ticket_redeem_attempt/success/failure`, `chat_typing_seen`.
- Traces
  - Send pipeline latency; message list render time; image decode cost.
- Acceptance criteria
  - All events include `chatId`, `messageId` (where applicable), and userId.

12) Accessibility and theming
- Checks
  - Contrast AA; TalkBack labels for all icons; large-text rendering; hit targets.
- Acceptance criteria
  - VoiceOver reads reply relationships (“replied to Alice: …”).

13) Test plan
- Unit: VM mapping, receipts state machine, reply resolution.
- UI: compose tests for jump-to, highlight, composer growth, send enablement.
- Integration: offline queue → ack reconciliation; ticket redeem end-to-end against emulator/functions.
- Device matrix: API 26/28/31/34, light/dark, en/long text locales.

14) Guardrails and non-goals
- Do not modify Event Feed or Event Details content areas during this work.
- Avoid introducing new global themes; confine design tokens to design system files.

Definition of Done
- All acceptance criteria met; no regressions in Events or Tickets flows; analytics verified; docs updated here.

- P0 Chat Visibility and Search
  - [DONE] Unstack lists in `ChatScreen.kt`; use a single viewport with `weight(1f)` so users/results/chats render correctly
  - [DONE] Preload `allUsers` on entry to avoid empty lists by default
  - [DONE] Split "LittleGig Contacts" into a separate discovery screen with its own empty states and glass panels
  - [TODO] Strong empty states across Chat (no users, no results, no chats) with clear CTAs
  - [TODO] Full Chat UI overhaul per references (glass panels, gradients, soft neumorphic accents, pill input, modern chips)

- P0 Auth Flow and Splash
  - [DONE] Silent anonymous bootstrap to prevent auth screen flash (attempt anon sign-in on app start)
  - [TODO] Keep anon-by-default; move account linking emphasis to Account/Settings
  - [TODO] Phone number linking as the primary path; OTP UX polish

- P0 Design System – Glassmorphism + Neumorphism
  - [DONE] Real frosted bottom navigation with shared haze state blurring content behind it; main content blur re-enabled
  - [DONE] Gradient refraction-like border fallback applied to glass pill and panels
  - [NEXT] AGSL runtime refraction shader (deferred; fallback active and artefact-free)
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
  - [DONE] Profile route/screen/viewmodel; mentions navigate to `profile/{username}`; follow/unfollow + stats
  - [DONE] Inbox glass header + unread badge + empty state
  - [DONE] Tickets: ticket card with perforation + barcode glass slot
  - [NEXT] Upload/Recaps step panels polish
  - [DONE] Account: integrate phone linking (OTP) in-sheet via `AccountLinkingScreen`; glass panels refined

- Phase 4 — Accessibility, Performance, and Polish
  - [TODO] Contrast, TalkBack labels, large-text tests
  - [TODO] Blur budget and shader capability gating; precompose heavy panels
  - [TODO] Spacing/typography/icon consistency pass

- Phase 5 — QA, Docs, and Release Prep
  - [TODO] Snapshot tests dark/light; device matrix (26/28/31/34)
  - [TODO] Update README/DESIGN_SYSTEM with final assets
  - [TODO] Release checklist and store assets