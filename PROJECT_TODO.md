# LittleGig – Comprehensive TODO and Runbook

## Changelog (2025-08-10)
- Fixed Firestore mapping warnings by aligning models:
  - `User`: added `likedEvents`, `engagementScore`, `lastRankUpdate`, and legacy alias `influencer` with `isInfluencer` defaulted accordingly.
  - `Event`: added `likedBy` and `ratings` fields used by repositories.
- Enabled back callback: set `android:enableOnBackInvokedCallback="true"` in `app/src/main/AndroidManifest.xml`.
- Build system: upgraded AGP to 8.4.2 and Kotlin to 1.9.24; set Java 17 and Compose Compiler 1.5.14 to support JDK 21; configured local Android SDK and accepted licenses.
- Verified `./gradlew assembleDebug` builds successfully; APK at `app/build/outputs/apk/debug/app-debug.apk`.
- Note: Device install pending (no emulator connected). Run: `adb connect 127.0.0.1:6555 && adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`.
- Phase 2: Implemented search/indexing improvements (lowercase fields), E.164 phone normalization, chat search cancellation/debounce, Places error handling, and contacts normalization service. Build remains green and changes pushed to main.
- Phase 3: Observability foundation (Timber + Crashlytics tree + breadcrumbs), Contacts DataStore cache with hash/TTL, Places key/billing verification in `UploadViewModel`, chat empty-state guidance. Build remains green and pushed.
- Phase 4 (in progress): Chat UI upgraded (neumorphic input/bubbles), ticket share hooks, chat cloud functions exported; next: media upload, replies, ticket redeem UI. Build green and pushed.

## Current Phase (Build-Focused – Phase 1)
- Keep assembleDebug green after each change and push to main
- Prioritize Immediate Fixes and Short-Term items in this order:
  1) Places: verify billing/key + robust error handling (done)
  2) Contacts: E.164 normalization and cached hash (done)
  3) Search: debounce + cancel in-flight + min length (done)
  4) Auth: anonymous-first + link account + phone primary (in progress)
  5) Observability: structured logs and breadcrumbs (phase foundation done)

## Build, Install, and Test
- Assemble debug build: `./gradlew assembleDebug`
- Install on emulator/device: `adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`
- Launch and verify logs via `adb logcat | grep -i littlegig`

## Cloud Functions Deployment
- From `functions/`: `npm install && npm run build`
- Deploy all: `firebase deploy --only functions`
- Specific functions: `firebase deploy --only functions:updateUserLocation,functions:getActiveUsersNearby,functions:sendMessage,functions:shareTicket,functions:redeemTicket`

## Immediate Fixes (Now)
- Chat
  - Ensure “+” loads both: contacts and all users (done)
  - Add logs for counts (done)
  - Seed test users in Firestore for empty datasets (pending)
- Places Autocomplete
  - Use Google Places REST (done)
  - Verify key and billing; handle API errors (done)
- Profile/Business
  - Immediate cache refresh after update/upgrade (done)
  - Snackbars for success/error (done)

## Short-Term (1–3 days)
- Contacts
  - Normalize phone numbers to E.164 using libphonenumber (done)
  - Cache contacts hash to avoid repeated loads (done)
  - Show empty-state guidance if zero contacts found (done)
- Search
  - Debounce queries; cancel in-flight; min length threshold (done)
  - Index usernames/displayNames; add lowercase fields for case-insensitive server search (done)
- Auth
  - Make phone auth primary; Google/email secondary (in progress)
  - Anonymous-by-default flow; “Link account” in settings (in progress)

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
- Add structured logs for key flows (auth, chat, payments) (foundation done)
- Crashlytics breadcrumbs for navigation and actions (added Timber tree)
- Perf Monitoring for slow traces (chat load, event fetch) (pending)

## Testing
- Deprioritized per product request; keep only critical smoke checks for builds

## Release Checklist
- Shrink/Proguard tuned; remove debug logs
- App icons, versioning, signing config
- Play Console listing; privacy policy

---

## Troubleshooting
- Empty All Users: ensure Firestore `users` has data; seed if needed
- Empty Contacts: emulator has no contacts; add contacts or test on device
- Places shows nothing: verify `google_places_key` and billing enabled

---

# Feature Backlog (User-Facing End-to-End)

## Onboarding & Auth
- Phone number auth as default: instant country code detect, OTP autofill, smart retry
- Anonymous-first (TikTok-style): explore immediately; “Link account” CTA in Settings and guarded areas
- Google Sign-In as secondary; smooth one-tap when available
- Account linking merge: preserve chats, likes, follows, rank, and purchases
- Session restore: silent re-auth; graceful offline mode

## Contacts & Social Graph
- Contacts sync: E.164 normalization, opt-in consent, periodic refresh, delta sync
- People you may know: contacts + mutual follows + location proximity (opt-in)
- Block/report users; shadow-ban for abusers; spam detection heuristics

## Chat & Messaging
- Real-time typing indicators; presence/“Active now” with timeout
- Delivery/read receipts, last seen, per-chat mute/archive
- Group chats: roles (owner/mod/admin), bans, invite links, join requests
- Media in chat: images/video/documents with upload progress and previews
- Replies, mentions, message reactions; message edit/delete (windowed)
- Search in chats (by user, keyword); pinned conversations

## Events (Create, Discover, Participate)
- Creation wizard: steps (Basics → Media → Location → Pricing → Publish)
- Draft autosave; preview; scheduled publish; recurring events (rules)
- Capacity tiers (Early Bird, VIP); promo codes; bundles
- Rich location: Google Places autocomplete (live), map picker, indoor notes
- Waitlist with auto-promo when capacity frees up

## Discovery & Map
- Smart home feed: time-aware + location-aware ranking; “For You” vs “Nearby” tabs
- Map clustering, live heat overlay (Active users/events), route preview
- Category and time filters (Tonight, Weekend, Free, Family)
- Deep links and share links for events and chats

## Tickets & Payments
- Add/Save payment methods; quick buy; Apple/Google Pay where available
- Ticket wallet; QR/Barcode; transfer to contacts; resale with organizer fees
- Refund policies flows (organizer-configurable); partial refunds
- Receipts and invoices; email delivery

## Business/Organizer Tools
- Organizer profiles; verification; team members and roles
- Event dashboard: sales funnel, revenue, check-in stats, demographics (privacy-safe)
- Marketing: featured placements, boosts, referral codes, UTM tracking
- Payouts: settlement accounts, multi-currency support, fees breakdown

## Recaps & Content
- Stories-like recaps per event; hashtagging; mentions; location tags
- Moderation queue: AI-assisted NSFW/abuse detection with human override
- Sharing to external platforms (IG/TikTok/Twitter) with attribution

## Notifications & Inbox
- Push categories: chat, tickets, event changes, recommendations
- Smart delivery (quiet hours, batching); rich actions (Reply, View Ticket)
- In-app inbox with read states and bulk actions

## Profiles & Social
- Public profiles with badges, rank, attended events, media
- Follow system improvements: suggestions, friend-of-friend
- Achievements: event streaks, host supporter, community builder

## Settings & Privacy
- Privacy dashboard: visibility (active now, profile), block list, data export/delete
- Notification preferences per category; email/push toggles
- Data consent for contacts/location; easy revoke

## Internationalization & Accessibility
- Multi-language strings framework; RTL support
- Large text modes; screen reader labels; high-contrast themes

## Performance & Offline
- Full list virtualization and paging across events/users/chats
- Aggressive image optimization: downsampling, modern formats, CDNs
- Offline drafts (events/messages); background sync and conflict handling

## Growth & Marketing
- Referrals with deep links; invite flows; bonus rewards
- App Indexing; app links; QR posters for events
- ASO: keywords, screenshots, localized store listings

## Analytics (Product-Focused)
- Funnel tracking for onboarding, purchase, chat start
- Recommendation feedback loops (like, follow, share, dwell)
- Crash-free users metric as a guardrail

---

# How We Test Quickly (Manual E2E)
- Build & install: `./gradlew assembleDebug` then `adb -s 127.0.0.1:6555 install -r app/build/outputs/apk/debug/app-debug.apk`
- Seed data (recommended): create 5–10 users & 5 events in Firestore for UI validation
- Verify flows:
  - Auth: anonymous start → link email/phone → refresh profile
  - Chat: tap “+” → All users populated → start 1-1 → exchange messages
  - Contacts: grant permission → see contacts that match Firestore phone numbers
  - Events: create with Places autocomplete → appears in feed and map
  - Tickets: purchase (sandbox) → ticket shows in wallet → QR validate
  - Notifications: send test FCM (functions) → device receives

---

Source control and CI
- Main branch: protected; PRs required for risky features
- Fast dev loop: keep `assembleDebug` green; ship small, test often

Repository: [LittleGigFinal](https://github.com/Billoxinogen18/LittleGigFinal.git)


