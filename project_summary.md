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
- Fixed Firestore mapper warnings by aligning models (`User`: influencer alias, likedEvents, engagementScore, lastRankUpdate; `Event`: likedBy, ratings)
- Enabled Android 13 back handling in `AndroidManifest.xml`
- Upgraded build stack (AGP/Kotlin/Compose), configured local Android SDK, accepted licenses
- Phase 2: Added E.164 normalization service, search lowercase index fields and queries, chat search debounce/cancel, contacts normalization service, improved Places error handling
- Phase 3: Observability foundation (Timber + Crashlytics tree + breadcrumbs), Contacts DataStore cache with hash/TTL, Places key/billing verification used in Upload flow, chat empty-state UX

## Current Phase (Build-Focused – Phase 1)
- Auth
  - Anonymous-first launch; link account from Settings
  - Phone auth primary; Google sign-in secondary
- Contacts
  - Normalize to E.164; cache contacts hash; empty-state UX
- Places/Map
  - Verify Places billing/key; robust error handling
- Observability
  - Structured logs for auth/chat; Crashlytics breadcrumbs via Timber tree

## Build & Install
- Build: `./gradlew assembleDebug`
- Install (emulator): `adb connect 127.0.0.1:6555 && adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`

## Next Phase Candidates
- Notifications: channels, inbox foundations
- Tickets/Payments: end-to-end sandbox purchase, wallet, QR validation
- Events: creation wizard with templates and waitlist