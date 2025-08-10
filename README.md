# 🎉 LittleGig - Event Discovery & Social Platform

A sophisticated event discovery and social platform built with **Jetpack Compose** and **Firebase**, featuring advanced design systems, real-time features, and seamless payment integration.

## ✨ **Features**

### 🎨 **Advanced Design System**
- **Liquid Glass Bottom Navigation** - Floating, translucent navigation with blur effects
- **Sophisticated Neumorphism** - Soft shadows and depth for modern UI
- **Advanced Glassmorphism** - Frosted glass panels with real-time refraction
- **Haptic Feedback** - Touch vibration with graceful error handling
- **Dark/Light Mode** - Adaptive theming with custom color palettes
- **12dp Corner Radius** - Consistent rounded corners throughout the app

### 🔥 **Core Features**
- **Events**: TikTok-style feed with auto-paging, Event Details with actions, recent recaps preview
- **Payments**: Flutterwave hosted checkout, Custom Tabs, deep link verification `littlegig://payment/verify?ref=...`, wallet auto-refresh
- **Tickets**: Wallet carousel UI, QR ready, receipts/history endpoint available via Cloud Function
- **Chat**: Real-time messages, media uploads, replies, ticket share/redeem, delivery/read ticks, typing indicator, pinned chats, search, reactions, mentions
- **Recaps**: Stories-like viewer, upload entry with proximity check and permission prompt
- **Notifications**: FCM topics per chat/event, in-app inbox with unread badge
- **Active Now**: Location-based discovery scaffolding
- **Ranking**: Daily engagement-based ranks
- **Profiles**: Account, settings, linking, and analytics

### 📱 **Authentication**
- Anonymous-first session bootstrap
- Link Phone, Google, or Email to upgrade account
- Inline validation for email/password linking
- Profile image upload to Firebase Storage

### 🎫 **Event Management**
- **Event Creation** - Rich event forms with location autocomplete
- **Ticket Purchases** - Real Flutterwave payment processing
- **Event Details** - Comprehensive event information with organizer profiles
- **Event Categories** - Music, Sports, Business, Education, etc.

## 🚀 **Technical Stack**

### **Frontend**
- **Jetpack Compose** - Modern Android UI toolkit
- **Material Design 3** - Latest Material Design components
- **Kotlin Coroutines** - Asynchronous programming
- **Hilt** - Dependency injection
- **Coil** - Image loading

### **Backend**
- **Firebase Firestore** - Real-time NoSQL database
- **Firebase Authentication** - User management
- **Firebase Storage** - Media file storage
- **Firebase Cloud Functions** - Serverless backend logic
- **Firebase Cloud Messaging** - Push notifications

### **Third-party Services**
- **Flutterwave** - Payment processing
- **Google Places API** - Location autocomplete
- **Google Maps API** - Map integration

## 📦 **Setup Instructions**

### **Prerequisites**
- Android Studio Arctic Fox or later
- JDK 11 or higher
- Firebase project with billing enabled
- Google Maps API key
- Flutterwave account

### **1. Clone the Repository**
```bash
git clone https://github.com/yourusername/LittleGig.git
cd LittleGig
```

### **2. Firebase Setup**
1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Enable Authentication, Firestore, Storage, and Cloud Functions
3. Download `google-services.json` and place it in `app/`
4. Enable billing for Cloud Functions

### **3. API Keys Configuration**
Update `app/src/main/res/values/strings.xml`:
```xml
<string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY</string>
<string name="google_places_key">YOUR_GOOGLE_PLACES_API_KEY</string>
```

Also set your Google Sign-In client ID if used by your project.

### **4. Flutterwave Configuration**
Set your Flutterwave secret key in Firebase Functions:
```bash
firebase functions:config:set flutterwave.secret_key="YOUR_FLUTTERWAVE_SECRET_KEY"
```

### **5. Deploy Cloud Functions**
```bash
cd functions
npm install
npm run build
cd ..
firebase deploy --only functions
```

### **6. Build and Run**
```bash
./gradlew assembleDebug
```

### **7. Deep Link Verification (Android)**
The app registers a deep link for payment verification:
```
Scheme: littlegig
Host: payment
Path: /verify
Query: ref=<paymentReference>
```
On return, the app verifies the reference and refreshes the ticket wallet with a success snackbar.

## 🎯 **Deployed Cloud Functions**

All 8 functions are live and working:

1. **`processTicketPurchase`** - Payment processing with Flutterwave
2. **`verifyPayment`** - Payment verification with secret key
3. **`getPaymentHistory`** - Payment records
4. **`calculateUserRanks`** - Daily ranking updates
5. **`updateUserLocation`** - Active now system
6. **`getActiveUsersNearby`** - Location discovery
7. **`sendPushNotification`** - FCM notifications
8. **`cleanupOldData`** - Weekly data cleanup

> Tip: deploy with `firebase deploy --only functions` after `npm run build` in `functions/`.

## 📱 **App Screenshots**

### **Main Features**
- **Events Feed** - TikTok-style event discovery
- **Event Details** - Rich event information with actions
- **Upload Screen** - Beautiful event creation form
- **Account Dashboard** - User analytics and profile management
- **Chat System** - Real-time messaging
- **Recaps Viewer** - Stories-like media experience

### **Design Elements**
- **Glass Panels** - Frosted glass effects with blur
- **Neumorphic Cards** - Soft shadows and depth
- **Haptic Feedback** - Touch vibration
- **Smooth Animations** - Physics-based animations
- **Adaptive Theming** - Dark/light mode support

## 🔧 **Architecture**

### **MVVM Pattern**
- **ViewModels** - Business logic and state management
- **Repositories** - Data access layer
- **Use Cases** - Feature-specific logic
- **UI Components** - Reusable Compose components

### **Data Flow**
1. **UI Events** → ViewModel
2. **ViewModel** → Repository
3. **Repository** → Firebase/API
4. **Response** → UI State Update

### **Offline Support**
- **Local Caching** - SharedPreferences and in-memory cache
- **Network Resilience** - Retry logic and offline fallbacks
- **Background Sync** - Automatic data synchronization

## 🎨 **Design System**

### **Color Palette**
```kotlin
// Primary Colors
LittleGigPrimary = Color(0xFF6366F1)
LittleGigSecondary = Color(0xFF8B5CF6)

// Glass Effects
GlassBackground = Color(0x80FFFFFF)
GlassBorder = Color(0x40FFFFFF)

// Neumorphic Shadows
LightShadow = Color(0xFFFFFFFF)
DarkShadow = Color(0xFF000000)
```

### **Typography**
- **Headlines** - Large, bold text for titles
- **Body** - Readable text for content
- **Captions** - Small text for metadata
- **Buttons** - Medium weight for actions

### **Spacing**
- **8dp** - Small spacing
- **16dp** - Medium spacing
- **24dp** - Large spacing
- **32dp** - Extra large spacing

## 🚀 **Performance Optimizations**

### **Image Loading**
- **Coil** - Efficient image caching
- **Lazy Loading** - Progressive image loading
- **Compression** - Optimized image sizes

### **Network**
- **Retry Logic** - Automatic retry on failures
- **Caching** - Offline data persistence
- **Background Sync** - Seamless data updates

### **UI Performance**
- **Lazy Lists** - Efficient scrolling
- **Compose Optimization** - Minimal recomposition
- **Memory Management** - Proper resource cleanup

### **Paging**
- Auto-paging using `rememberLazyListState` + `snapshotFlow` for Events, Users, and Chats; no "Load More" buttons.

## 🔒 **Security**

### **Authentication**
- **Firebase Auth** - Secure user authentication
- **Token Management** - Automatic token refresh
- **Session Management** - Secure session handling

### **Data Protection**
- Firestore Rules (relaxed in dev per README notes)
- Storage Rules (relaxed in dev per README notes)
- API Security via Firebase Functions IAM

## 📊 **Analytics & Monitoring**

### **User Analytics**
- **Event Engagement** - Track user interactions
- **Payment Analytics** - Monitor transaction success
- **User Growth** - Track user acquisition

### **Performance Monitoring**
- **Crashlytics** - Automatic crash reporting
- **Performance Monitoring** - App performance tracking
- **Custom Events** - Business-specific analytics

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 **Acknowledgments**

- **Firebase** - Backend infrastructure
- **Jetpack Compose** - Modern UI toolkit
- **Material Design** - Design system
- **Flutterwave** - Payment processing
- **Google Maps** - Location services

---

**Built with ❤️ for the event discovery community**