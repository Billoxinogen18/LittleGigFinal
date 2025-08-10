# LittleGig – Comprehensive TODO and Runbook

## Build, Install, and Test
- Assemble debug build: `./gradlew assembleDebug`
- Install on emulator/device: `adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`
- Launch and verify logs via `adb logcat | grep -i littlegig`

## Cloud Functions Deployment
- From `functions/`: `npm install && npm run build`
- Deploy all: `firebase deploy --only functions`
- Specific functions: `firebase deploy --only functions:updateUserLocation,functions:getActiveUsersNearby`

## Immediate Fixes (Now)
- Chat
  - Ensure “+” loads both: contacts and all users (done)
  - Add logs for counts (done)
  - Seed test users in Firestore for empty datasets (pending)
- Places Autocomplete
  - Use Google Places REST (done)
  - Verify key and billing; handle API errors (pending)
- Profile/Business
  - Immediate cache refresh after update/upgrade (done)
  - Snackbars for success/error (done)

## Short-Term (1–3 days)
- Contacts
  - Normalize phone numbers to E.164 using libphonenumber
  - Cache contacts hash to avoid repeated loads
  - Show empty-state guidance if zero contacts found
- Search
  - Debounce queries; cancel in-flight; min length threshold
  - Index usernames/displayNames; add lowercase fields for case-insensitive server search
- Auth
  - Make phone auth primary; Google/email secondary
  - Anonymous-by-default flow; “Link account” in settings

## Core Features Completion (1–2 weeks)
- Chat
  - Typing indicators; delivery/read receipts
  - Group moderation; 1–1 privacy controls; block/report
- Notifications
  - FCM topics per chat; foreground service channel; rate limiting
- Ticketing/Payments
  - Full purchase flow; QR validation; receipts; history
  - Flutterwave webhooks handling and status reconciliation
- Events
  - Creation wizard; templates; recurring events; waitlist

## Security & Rules
- Lock down Firestore and Storage rules (currently permissive for testing)
- Add rules for:
  - Users: self-write only; selected public fields readable
  - Chats: participants-only read/write
  - Tickets: owner-only read; organizers read for validation

## Performance
- Image pipeline: memory/disk cache; placeholders; downsampling
- Paging for lists (events, users, chats)
- Avoid full collection scans; add composite indexes where needed

## Observability
- Add structured logs for key flows (auth, chat, payments)
- Crashlytics breadcrumbs for navigation and actions
- Perf Monitoring for slow traces (chat load, event fetch)

## Testing
- Unit tests for repositories and viewmodels
- UI tests for auth, chat start, event create, purchase
- E2E smoke using a seeded Firebase project

## Release Checklist
- Shrink/Proguard tuned; remove debug logs
- App icons, versioning, signing config
- Play Console listing; privacy policy

---

## Troubleshooting
- Empty All Users: ensure Firestore `users` has data; seed if needed
- Empty Contacts: emulator has no contacts; add contacts or test on device
- Places shows nothing: verify `google_places_key` and billing enabled


