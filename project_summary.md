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
- Phase 2: E.164 normalization, lowercase index fields and queries, chat search debounce/cancel, contacts normalization, improved Places error handling
- Phase 3: Observability (Timber + Crashlytics breadcrumbs), Contacts DataStore cache with hash/TTL, Places key/billing verification in Upload flow, chat empty-state UX
- Phase 4 (in progress): Modern chat UI (neumorphic input/bubbles), ticket share hooks, chat callable functions exported; upcoming: media uploads, replies, redeem UI

## Current Phase (Build-Focused – Phase 1)
- Auth: anonymous-first + linking flows (phone primary), Google/email secondary
- Chat: typing indicators in place; building media/replies/ticket share end-to-end
- Tickets: integrate chat share and redeem; notifications upon purchase and share
- Observability: structured logs via Timber + Crashlytics

## Build & Install
- Build: `./gradlew assembleDebug`
- Install (emulator): `adb connect 127.0.0.1:6555 && adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`

## Next Phase Candidates
- Notifications: channels, inbox foundations
- Tickets/Payments: end-to-end sandbox purchase, wallet, QR validation
- Events: creation wizard with templates and waitlist