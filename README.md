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
- **Real-time Event Discovery** - Browse and search events with advanced filtering
- **Payment Processing** - Flutterwave integration for ticket purchases
- **User Ranking System** - Automatic engagement-based ranking (NOVICE → SUPERSTAR)
- **Active Now System** - Location-based user discovery (3km radius)
- **Push Notifications** - FCM integration for real-time updates
- **Chat System** - Real-time messaging between users
- **Event Recaps** - Instagram Stories-like media uploads with location verification
- **Profile Management** - Complete user profiles with analytics

### 📱 **Authentication**
- **Email/Password Auth** - Traditional Firebase authentication
- **Phone Number Auth** - SMS verification (ready for Firebase Phone Auth)
- **Auto-generated Usernames** - Unique usernames from email/phone
- **Profile Picture Upload** - Firebase Storage integration

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

## 🔒 **Security**

### **Authentication**
- **Firebase Auth** - Secure user authentication
- **Token Management** - Automatic token refresh
- **Session Management** - Secure session handling

### **Data Protection**
- **Firestore Rules** - Database security rules
- **Storage Rules** - File access control
- **API Security** - Secure API endpoints

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