# LittleGig - Events & Hospitality Platform

LittleGig is a comprehensive Android application for event management, ticket sales, and hospitality services in Africa. This MVP includes all features from the pitch deck including events browsing, ticket purchasing, business dashboards, influencer advertising, and more.

## Features

### Core Features
- ✅ **User Authentication** - Sign up/login with email or Google account
- ✅ **Events Management** - Browse, search, and filter events
- ✅ **Ticket Sales** - Purchase tickets with 4% commission tracking  
- ✅ **LittleMap** - Interactive map showing event locations
- ✅ **Content Upload** - Business users can create events, hotels, restaurants
- ✅ **Multi-category Content** - Events, hotels, restaurants, tours, concerts, workshops
- ✅ **User Accounts** - Regular, Business, and Admin account types

### Business Features
- ✅ **Business Dashboard** - Analytics and event management for organizers
- ✅ **Revenue Tracking** - Commission calculation and earnings analytics
- ✅ **Influencer Advertising** - Paid promotion system for influencers
- ✅ **Payment Processing** - Stripe integration for ticket and ad payments

### Technical Stack
- **Frontend:** Android (Kotlin + Jetpack Compose)
- **Backend:** Firebase (Firestore, Auth, Storage, Functions)
- **Payment:** Flutterwave integration (Cards, Mpesa, USSD, Bank Transfer)
- **Maps:** Google Maps Android API
- **Architecture:** MVVM with Hilt dependency injection

## Setup Instructions

### Prerequisites
1. Android Studio Electric Eel or newer
2. Firebase project set up
3. Google Maps API key
4. Stripe account (for payments)

### Firebase Setup
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Enable the following services:
   - **Authentication** (Email/Password and Google Sign-In)
   - **Firestore Database** 
   - **Cloud Storage**
   - **Cloud Functions**
   - **Cloud Messaging** (for push notifications)

3. Download `google-services.json` and place it in the `app/` directory
4. Replace the placeholder content in the existing `google-services.json` with your actual configuration

### Google Maps Setup
1. Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com)
2. Enable the Maps SDK for Android
3. Update the API key in `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="google_maps_key">YOUR_ACTUAL_API_KEY_HERE</string>
   ```

### Flutterwave Setup (for Payments) - LIVE PRODUCTION
1. **LIVE CREDENTIALS CONFIGURED** - Using your production Flutterwave account
2. **Public Key**: `FLWPUBK-3fa265a8e3265a459035c2d9bbfa798c-X`
3. **Secret Key**: `FLWSECK-e30b0920b7b209167ef35802a287a5ef-1987eb05964vt-X`
4. **Encryption Key**: `e30b0920b7b27c9a51d26214`
5. **Payment Methods Enabled**:
   - Cards (Visa, Mastercard, American Express)
   - M-Pesa (Kenya)
   - USSD (Nigeria)
   - Bank Transfers
   - Mobile Money (Ghana, Uganda)
6. Deploy the Firebase Functions:
   ```bash
   firebase deploy --only functions
   ```

### Cloud Functions Deployment
1. Install Firebase CLI: `npm install -g firebase-tools`
2. Navigate to the `functions/` directory
3. Install dependencies: `npm install`
4. Deploy functions: `firebase deploy --only functions`

### Building the App
1. Clone this repository
2. Open in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or device

## Project Structure

```
app/
├── src/main/java/com/littlegig/app/
│   ├── data/
│   │   ├── model/          # Data models (User, Event, Ticket, etc.)
│   │   └── repository/     # Repository layer for data access
│   ├── di/                 # Dependency injection modules
│   ├── presentation/       # UI layer (Compose screens & ViewModels)
│   │   ├── auth/          # Authentication screens
│   │   ├── events/        # Events browsing and details
│   │   ├── tickets/       # User tickets management
│   │   ├── map/           # LittleMap functionality
│   │   ├── upload/        # Content creation for businesses
│   │   ├── account/       # User account management
│   │   └── business/      # Business dashboard
│   └── services/          # Background services (FCM, etc.)
├── res/                   # Android resources
└── google-services.json  # Firebase configuration

functions/
├── src/
│   └── index.ts          # Cloud Functions (payments, analytics, etc.)
└── package.json          # Node.js dependencies
```

## Key Components

### Authentication System
- Firebase Authentication with email/password and Google Sign-In
- Support for Regular, Business, and Admin user types
- Automatic account upgrades for business features

### Events Management
- Create, browse, and search events
- Category filtering (Events, Hotels, Restaurants, Tours, etc.)
- Featured events section
- Location-based event discovery

### Ticket System
- Secure ticket purchasing with Flutterwave (Cards, Mpesa, USSD, Bank Transfer)
- 4% commission automatically calculated
- QR code generation for tickets
- Ticket status tracking (Active, Used, Cancelled, Expired)

### Business Features
- Event organizer dashboard with analytics
- Revenue tracking and commission calculation
- Content upload for businesses
- Real-time ticket sales monitoring

### Influencer Advertising
- Create and manage paid advertisements
- Budget allocation and payment processing
- Analytics tracking (impressions, clicks, reach)
- Advertisement performance metrics

## Revenue Model

The app implements the exact revenue model from the pitch deck:

1. **4% Commission on Ticket Sales** - Automatically calculated and tracked
2. **Influencer Advertisement Fees** - Paid promotion system with analytics

## Payment Methods Supported

LittleGig now supports multiple payment methods through Flutterwave:

- **Cards** - Visa, Mastercard, American Express
- **Mobile Money** - Mpesa (Kenya)
- **Bank Transfers** - Direct bank account transfers
- **USSD** - USSD payments for feature phones
- **Bank Account** - Pay with bank account (Nigeria)

## API Documentation

### Firebase Cloud Functions

#### Payment Functions (Flutterwave LIVE Integration)
- `initializeFlutterwavePayment` - Initializes LIVE Flutterwave payment with V3 SDK
- `verifyFlutterwavePayment` - Verifies payment status with LIVE Flutterwave API
- `processTicketPayment` - Processes ticket purchase payments
- `processInfluencerAdPayment` - Processes influencer ad payments  
- `processRefund` - Handles payment refunds
- `flutterwaveWebhookHandler` - Handles asynchronous payment confirmations

#### Scheduled Functions
- `updateAdAnalytics` - Scheduled function for ad analytics
- `expireOldTickets` - Scheduled cleanup of expired tickets
- `sendEventReminders` - Push notifications for upcoming events

## Contributing

This is an MVP implementation. For production deployment, consider:

1. **Security Enhancements**
   - Implement proper API key management
   - Add rate limiting to Cloud Functions
   - Enhance user input validation

2. **Performance Optimizations**
   - Implement image caching and optimization
   - Add pagination for large data sets
   - Optimize database queries

3. **Additional Features**
   - Real-time chat for events
   - Social sharing functionality  
   - Advanced analytics and reporting
   - Multi-language support

## License

This project is created as an MVP demonstration. Please ensure proper licensing for production use.

## Support

For questions about this implementation, please refer to the pitch deck requirements or create an issue in the repository.