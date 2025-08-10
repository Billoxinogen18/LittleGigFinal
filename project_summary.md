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