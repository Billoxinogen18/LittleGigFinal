"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createDynamicLink = exports.sendEventReminders = exports.notifyPriceDrop = exports.notifyWaitlistOnCapacity = exports.cleanupOldData = exports.sendPushNotification = exports.getActiveUsersNearby = exports.updateUserLocation = exports.calculateUserRanks = exports.getPaymentHistory = exports.upgradeToBusinessAccount = exports.verifyPayment = exports.processTicketPurchase = void 0;
const functions = require("firebase-functions");
const admin = require("firebase-admin");
const axios_1 = require("axios");
admin.initializeApp();
const db = admin.firestore();
// Payment Processing with Flutterwave
exports.processTicketPurchase = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
    try {
        const { eventId, userId, amount, eventTitle, currency = 'KES' } = data;
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        // Generate unique payment reference
        const paymentReference = `LG_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
        // Create payment record
        const paymentData = {
            eventId,
            userId,
            amount,
            currency,
            status: 'pending',
            paymentReference,
            eventTitle,
            createdAt: admin.firestore.FieldValue.serverTimestamp(),
            updatedAt: admin.firestore.FieldValue.serverTimestamp()
        };
        await db.collection('payments').add(paymentData);
        // Generate Flutterwave payment URL
        const flutterwaveUrl = `https://checkout.flutterwave.com/v3/hosted/pay/${paymentReference}?amount=${amount}&currency=${currency}&tx_ref=${paymentReference}&redirect_url=${encodeURIComponent('https://littlegig.app/payment/callback')}&meta[eventId]=${eventId}&meta[userId]=${userId}`;
        return {
            success: true,
            paymentUrl: flutterwaveUrl,
            paymentReference
        };
    }
    catch (error) {
        console.error('Payment processing error:', error);
        throw new functions.https.HttpsError('internal', 'Payment processing failed');
    }
});
// Verify Payment
exports.verifyPayment = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
    try {
        const { paymentReference } = data;
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        // Verify with Flutterwave API
        const response = await axios_1.default.get(`https://api.flutterwave.com/v3/transactions/${paymentReference}/verify`, {
            headers: {
                'Authorization': `Bearer ${functions.config().flutterwave.secret_key}`,
                'Content-Type': 'application/json'
            }
        });
        const result = response.data;
        if (result.status === 'success' && result.data.status === 'successful') {
            // Update payment status
            const paymentQuery = await db.collection('payments')
                .where('paymentReference', '==', paymentReference)
                .get();
            if (!paymentQuery.empty) {
                const paymentDoc = paymentQuery.docs[0];
                await paymentDoc.ref.update({
                    status: 'completed',
                    flutterwaveTransactionId: result.data.id,
                    updatedAt: admin.firestore.FieldValue.serverTimestamp()
                });
                // Create ticket
                const paymentData = paymentDoc.data();
                await createTicket(paymentData.eventId, paymentData.userId, paymentData.amount);
            }
            return { success: true };
        }
        else {
            return { success: false };
        }
    }
    catch (error) {
        console.error('Payment verification error:', error);
        throw new functions.https.HttpsError('internal', 'Payment verification failed');
    }
});
// Business Account Upgrade
exports.upgradeToBusinessAccount = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
    try {
        const { userId, amount, type, currency = 'KES' } = data;
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        // Generate unique payment reference
        const paymentReference = `LG_BUS_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
        // Create payment record for business upgrade
        const paymentData = {
            userId,
            amount,
            currency,
            type,
            status: 'pending',
            paymentReference,
            description: 'Business Account Upgrade',
            createdAt: admin.firestore.FieldValue.serverTimestamp(),
            updatedAt: admin.firestore.FieldValue.serverTimestamp()
        };
        await db.collection('payments').add(paymentData);
        // Generate Flutterwave payment URL
        const flutterwaveUrl = `https://checkout.flutterwave.com/v3/hosted/pay/${paymentReference}?amount=${amount}&currency=${currency}&tx_ref=${paymentReference}&redirect_url=${encodeURIComponent('https://littlegig.app/payment/callback')}&meta[userId]=${userId}&meta[type]=${type}`;
        return {
            success: true,
            paymentUrl: flutterwaveUrl,
            paymentReference
        };
    }
    catch (error) {
        console.error('Business upgrade payment error:', error);
        throw new functions.https.HttpsError('internal', 'Business upgrade payment failed');
    }
});
// Create Ticket
async function createTicket(eventId, userId, amount) {
    const ticketData = {
        eventId,
        userId,
        amount,
        status: 'active',
        createdAt: admin.firestore.FieldValue.serverTimestamp(),
        ticketCode: `TKT_${Date.now()}_${Math.random().toString(36).substr(2, 6)}`
    };
    await db.collection('tickets').add(ticketData);
    // Update event ticket count
    const eventRef = db.collection('events').doc(eventId);
    await eventRef.update({
        ticketsSold: admin.firestore.FieldValue.increment(1)
    });
}
// Get Payment History
exports.getPaymentHistory = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
    try {
        const { userId } = data;
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        const paymentsSnapshot = await db.collection('payments')
            .where('userId', '==', userId)
            .orderBy('createdAt', 'desc')
            .get();
        const payments = paymentsSnapshot.docs.map(doc => (Object.assign({ id: doc.id }, doc.data())));
        return { payments };
    }
    catch (error) {
        console.error('Get payment history error:', error);
        throw new functions.https.HttpsError('internal', 'Failed to get payment history');
    }
});
// Ranking System - Calculate User Ranks
exports.calculateUserRanks = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 540 }).pubsub.schedule('every 24 hours').onRun(async (context) => {
    try {
        const usersSnapshot = await db.collection('users').get();
        const users = usersSnapshot.docs.map(doc => (Object.assign({ id: doc.id }, doc.data())));
        // Calculate engagement scores for each user
        for (const user of users) {
            const engagementScore = await calculateEngagementScore(user.id);
            const rank = determineUserRank(engagementScore);
            await db.collection('users').doc(user.id).update({
                rank,
                engagementScore,
                lastRankUpdate: admin.firestore.FieldValue.serverTimestamp()
            });
        }
        console.log('User ranks updated successfully');
    }
    catch (error) {
        console.error('Rank calculation error:', error);
    }
});
// Calculate Engagement Score
async function calculateEngagementScore(userId) {
    const [eventsCreated, eventsAttended, recapsCreated, totalLikes, totalViews] = await Promise.all([
        getEventsCreated(userId),
        getEventsAttended(userId),
        getRecapsCreated(userId),
        getTotalLikes(userId),
        getTotalViews(userId)
    ]);
    // Weighted scoring system
    const score = (eventsCreated * 10 + // 40% weight
        eventsAttended * 5 + // 15% weight
        recapsCreated * 8 + // 35% weight
        totalLikes * 2 + // 5% weight
        totalViews * 0.1 // 5% weight
    );
    return Math.round(score);
}
async function getEventsCreated(userId) {
    const snapshot = await db.collection('events')
        .where('organizerId', '==', userId)
        .get();
    return snapshot.size;
}
async function getEventsAttended(userId) {
    const snapshot = await db.collection('tickets')
        .where('userId', '==', userId)
        .where('status', '==', 'active')
        .get();
    return snapshot.size;
}
async function getRecapsCreated(userId) {
    const snapshot = await db.collection('recaps')
        .where('userId', '==', userId)
        .get();
    return snapshot.size;
}
async function getTotalLikes(userId) {
    const snapshot = await db.collection('recaps')
        .where('userId', '==', userId)
        .get();
    let totalLikes = 0;
    snapshot.docs.forEach(doc => {
        totalLikes += doc.data().likes || 0;
    });
    return totalLikes;
}
async function getTotalViews(userId) {
    const snapshot = await db.collection('recaps')
        .where('userId', '==', userId)
        .get();
    let totalViews = 0;
    snapshot.docs.forEach(doc => {
        totalViews += doc.data().views || 0;
    });
    return totalViews;
}
// Determine User Rank
function determineUserRank(engagementScore) {
    if (engagementScore >= 1000)
        return 'SUPERSTAR';
    if (engagementScore >= 500)
        return 'ROCKSTAR';
    if (engagementScore >= 250)
        return 'FAMOUS';
    if (engagementScore >= 100)
        return 'INFLUENCER';
    if (engagementScore >= 50)
        return 'POPULAR';
    if (engagementScore >= 25)
        return 'PARTY_POPPER';
    if (engagementScore >= 10)
        return 'BEGINNER';
    return 'NOVICE';
}
// Active Now System - Update User Location
exports.updateUserLocation = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
    var _a;
    try {
        const { latitude, longitude, isActive, userId: explicitUserId } = data;
        const userId = ((_a = context.auth) === null || _a === void 0 ? void 0 : _a.uid) || explicitUserId;
        if (!userId) {
            throw new functions.https.HttpsError('unauthenticated', 'Missing userId');
        }
        await db.collection('users').doc(userId).set({
            location: {
                latitude,
                longitude,
                updatedAt: admin.firestore.FieldValue.serverTimestamp()
            },
            isActiveNow: isActive
        }, { merge: true });
        return { success: true };
    }
    catch (error) {
        console.error('Update location error:', error);
        throw new functions.https.HttpsError('internal', 'Failed to update location');
    }
});
// Get Active Users Near Location
exports.getActiveUsersNearby = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
    try {
        const { latitude, longitude, radius = 5 } = data; // radius in km
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        const usersSnapshot = await db.collection('users')
            .where('isActiveNow', '==', true)
            .get();
        const nearbyUsers = usersSnapshot.docs
            .map(doc => (Object.assign({ id: doc.id }, doc.data())))
            .filter(user => {
            const userData = user;
            if (!userData.location)
                return false;
            const distance = calculateDistance(latitude, longitude, userData.location.latitude, userData.location.longitude);
            return distance <= radius;
        });
        return { users: nearbyUsers };
    }
    catch (error) {
        console.error('Get nearby users error:', error);
        throw new functions.https.HttpsError('internal', 'Failed to get nearby users');
    }
});
// Calculate Distance between two points
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // Earth's radius in km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}
// Send Push Notification
exports.sendPushNotification = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30, minInstances: 1 }).https.onCall(async (data, context) => {
    var _a;
    try {
        const { userId, title, body, data: notificationData } = data;
        if (!context.auth) {
            throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
        }
        const userDoc = await db.collection('users').doc(userId).get();
        const fcmToken = (_a = userDoc.data()) === null || _a === void 0 ? void 0 : _a.fcmToken;
        if (!fcmToken) {
            throw new functions.https.HttpsError('not-found', 'User FCM token not found');
        }
        const message = {
            token: fcmToken,
            notification: {
                title,
                body
            },
            data: notificationData || {},
            android: {
                priority: 'high',
                notification: {
                    sound: 'default',
                    channelId: 'littlegig_channel'
                }
            }
        };
        const response = await admin.messaging().send(message);
        return { success: true, messageId: response };
    }
    catch (error) {
        console.error('Send notification error:', error);
        throw new functions.https.HttpsError('internal', 'Failed to send notification');
    }
});
// Cleanup Old Data
exports.cleanupOldData = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 540 }).pubsub.schedule('0 0 * * 0').onRun(async (context) => {
    try {
        const oneMonthAgo = new Date();
        oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
        // Clean up old recaps
        const oldRecapsSnapshot = await db.collection('recaps')
            .where('createdAt', '<', oneMonthAgo)
            .get();
        const batch = db.batch();
        oldRecapsSnapshot.docs.forEach(doc => {
            batch.delete(doc.ref);
        });
        await batch.commit();
        console.log(`Cleaned up ${oldRecapsSnapshot.size} old recaps`);
    }
    catch (error) {
        console.error('Cleanup error:', error);
    }
});
// --- Growth Features: Waitlist and Price Drop Alerts ---
exports.notifyWaitlistOnCapacity = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 60 }).firestore
    .document('events/{eventId}')
    .onUpdate(async (change, context) => {
    const before = change.before.data();
    const after = change.after.data();
    const eventId = context.params.eventId;
    try {
        // If tickets sold decreased or capacity increased -> new slots
        const beforeRemaining = (before.capacity || 0) - (before.ticketsSold || 0);
        const afterRemaining = (after.capacity || 0) - (after.ticketsSold || 0);
        if (afterRemaining > beforeRemaining && afterRemaining > 0) {
            const waitlistSnap = await db
                .collection('events')
                .doc(eventId)
                .collection('waitlist')
                .orderBy('createdAt', 'asc')
                .limit(afterRemaining)
                .get();
            const notifications = [];
            for (const doc of waitlistSnap.docs) {
                const userId = doc.data().userId;
                notifications.push(sendEventNotification(userId, after.title || 'Event', 'Spots just opened up! Tap to buy now', { type: 'waitlist', eventId }));
            }
            await Promise.all(notifications);
        }
        return null;
    }
    catch (error) {
        console.error('notifyWaitlistOnCapacity error', error);
        return null;
    }
});
exports.notifyPriceDrop = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 60 }).firestore
    .document('events/{eventId}')
    .onUpdate(async (change, context) => {
    const before = change.before.data();
    const after = change.after.data();
    const eventId = context.params.eventId;
    try {
        if ((before.price || 0) > (after.price || 0)) {
            const alertsSnap = await db
                .collection('events')
                .doc(eventId)
                .collection('priceAlerts')
                .get();
            const notifications = [];
            for (const doc of alertsSnap.docs) {
                const userId = doc.data().userId;
                notifications.push(sendEventNotification(userId, after.title || 'Event', 'Price just dropped! Don’t miss out', { type: 'price_drop', eventId }));
            }
            await Promise.all(notifications);
        }
        return null;
    }
    catch (error) {
        console.error('notifyPriceDrop error', error);
        return null;
    }
});
async function sendEventNotification(userId, title, body, data) {
    var _a;
    try {
        const userDoc = await db.collection('users').doc(userId).get();
        const fcmToken = (_a = userDoc.data()) === null || _a === void 0 ? void 0 : _a.fcmToken;
        if (!fcmToken)
            return null;
        const message = {
            token: fcmToken,
            notification: { title, body },
            data,
            android: { priority: 'high' }
        };
        return admin.messaging().send(message);
    }
    catch (e) {
        console.error('sendEventNotification error', e);
        return null;
    }
}
// Event reminders for RSVPs (run hourly to find events starting soon)
exports.sendEventReminders = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 540 }).pubsub.schedule('every 60 minutes').onRun(async () => {
    try {
        const now = Date.now();
        const soon = now + 2 * 60 * 60 * 1000; // 2 hours
        const eventsSnap = await db.collection('events')
            .where('isActive', '==', true)
            .get();
        const tasks = [];
        for (const doc of eventsSnap.docs) {
            const event = doc.data();
            const dateTime = (event.dateTime && typeof event.dateTime.toMillis === 'function') ? event.dateTime.toMillis() : (event.dateTime || 0);
            if (dateTime >= now && dateTime <= soon) {
                const rsvps = await db.collection('events').doc(doc.id).collection('rsvps').get();
                for (const rsvp of rsvps.docs) {
                    const userId = rsvp.data().userId;
                    tasks.push(sendEventNotification(userId, event.title || 'Event starting soon', 'Happening in a couple of hours. See you there!', { type: 'event_reminder', eventId: doc.id }));
                }
            }
        }
        await Promise.all(tasks);
        return null;
    }
    catch (e) {
        console.error('sendEventReminders error', e);
        return null;
    }
});
// Create Dynamic Link for sharing (requires config: dynamiclinks.domain, dynamiclinks.android_package, dynamiclinks.api_key)
exports.createDynamicLink = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30, minInstances: 1 }).https.onCall(async (data, context) => {
    var _a, _b, _c;
    try {
        const { link, title, imageUrl } = data;
        const domain = (_a = functions.config().dynamiclinks) === null || _a === void 0 ? void 0 : _a.domain;
        const androidPackage = (_b = functions.config().dynamiclinks) === null || _b === void 0 ? void 0 : _b.android_package;
        const apiKey = (_c = functions.config().dynamiclinks) === null || _c === void 0 ? void 0 : _c.api_key;
        if (!domain || !androidPackage || !apiKey) {
            throw new functions.https.HttpsError('failed-precondition', 'Dynamic Links not configured');
        }
        const payload = {
            dynamicLinkInfo: {
                domainUriPrefix: domain,
                link,
                androidInfo: { androidPackageName: androidPackage },
                socialMetaTagInfo: {
                    socialTitle: title || 'LittleGig',
                    socialDescription: 'Discover amazing events',
                    socialImageLink: imageUrl || ''
                }
            },
            suffix: { option: 'SHORT' }
        };
        const resp = await axios_1.default.post(`https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=${apiKey}`, payload, {
            headers: { 'Content-Type': 'application/json' }
        });
        const json = resp.data;
        if (json.error) {
            console.error('Dynamic link error', json.error);
            throw new functions.https.HttpsError('internal', 'Failed to create dynamic link');
        }
        return { shortLink: json.shortLink };
    }
    catch (e) {
        console.error('createDynamicLink error', e);
        throw new functions.https.HttpsError('internal', 'Failed to create dynamic link');
    }
});
//# sourceMappingURL=index.js.map