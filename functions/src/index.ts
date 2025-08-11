import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import axios from 'axios';

admin.initializeApp();

const db = admin.firestore();

// Payment Processing with Flutterwave
export const processTicketPurchase = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
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
    const flutterwaveUrl = `https://checkout.flutterwave.com/v3/hosted/pay/${paymentReference}?amount=${amount}&currency=${currency}&tx_ref=${paymentReference}&redirect_url=${encodeURIComponent('littlegig://payment/verify?ref=' + paymentReference)}&meta[eventId]=${eventId}&meta[userId]=${userId}`;

    return {
      success: true,
      paymentUrl: flutterwaveUrl,
      paymentReference
    };
  } catch (error) {
    console.error('Payment processing error:', error);
    throw new functions.https.HttpsError('internal', 'Payment processing failed');
  }
});

// Verify Payment
export const verifyPayment = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
  try {
    const { paymentReference } = data;
    
    if (!context.auth) {
      throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }

    // Verify with Flutterwave API
    const response = await axios.get(`https://api.flutterwave.com/v3/transactions/${paymentReference}/verify`, {
      headers: {
        'Authorization': `Bearer ${functions.config().flutterwave.secret_key}`,
        'Content-Type': 'application/json'
      }
    });
    const result: any = response.data;

    if ((result as any).status === 'success' && (result as any).data.status === 'successful') {
      // Update payment status
      const paymentQuery = await db.collection('payments')
        .where('paymentReference', '==', paymentReference)
        .get();

      if (!paymentQuery.empty) {
        const paymentDoc = paymentQuery.docs[0];
        await paymentDoc.ref.update({
          status: 'completed',
          flutterwaveTransactionId: (result as any).data.id,
          updatedAt: admin.firestore.FieldValue.serverTimestamp()
        });

        // Create ticket
        const paymentData = paymentDoc.data();
        await createTicket(paymentData.eventId, paymentData.userId, paymentData.amount);
      }

      return { success: true };
    } else {
      return { success: false };
    }
  } catch (error) {
    console.error('Payment verification error:', error);
    throw new functions.https.HttpsError('internal', 'Payment verification failed');
  }
});

// Business Account Upgrade
export const upgradeToBusinessAccount = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 60, minInstances: 1 }).https.onCall(async (data, context) => {
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
    const flutterwaveUrl = `https://checkout.flutterwave.com/v3/hosted/pay/${paymentReference}?amount=${amount}&currency=${currency}&tx_ref=${paymentReference}&redirect_url=${encodeURIComponent('littlegig://payment/verify?ref=' + paymentReference)}&meta[userId]=${userId}&meta[type]=${type}`;

    return {
      success: true,
      paymentUrl: flutterwaveUrl,
      paymentReference
    };
  } catch (error) {
    console.error('Business upgrade payment error:', error);
    throw new functions.https.HttpsError('internal', 'Business upgrade payment failed');
  }
});

// Create Ticket
async function createTicket(eventId: string, userId: string, amount: number) {
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
export const getPaymentHistory = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
  try {
    const { userId } = data;
    
    if (!context.auth) {
      throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }

    const paymentsSnapshot = await db.collection('payments')
      .where('userId', '==', userId)
      .orderBy('createdAt', 'desc')
      .get();

    const payments = paymentsSnapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));

    return { payments };
  } catch (error) {
    console.error('Get payment history error:', error);
    throw new functions.https.HttpsError('internal', 'Failed to get payment history');
  }
});

// Ranking System - Calculate User Ranks
export const calculateUserRanks = functions.region('us-central1').runWith({ memory: '512MB', timeoutSeconds: 540 }).pubsub.schedule('every 24 hours').onRun(async (context) => {
  try {
    const usersSnapshot = await db.collection('users').get();
    const users = usersSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

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
  } catch (error) {
    console.error('Rank calculation error:', error);
  }
});

// Calculate Engagement Score
async function calculateEngagementScore(userId: string): Promise<number> {
  const [
    eventsCreated,
    eventsAttended,
    recapsCreated,
    totalLikes,
    totalViews
  ] = await Promise.all([
    getEventsCreated(userId),
    getEventsAttended(userId),
    getRecapsCreated(userId),
    getTotalLikes(userId),
    getTotalViews(userId)
  ]);

  // Weighted scoring system
  const score = (
    eventsCreated * 10 +      // 40% weight
    eventsAttended * 5 +      // 15% weight
    recapsCreated * 8 +       // 35% weight
    totalLikes * 2 +          // 5% weight
    totalViews * 0.1          // 5% weight
  );

  return Math.round(score);
}

async function getEventsCreated(userId: string): Promise<number> {
  const snapshot = await db.collection('events')
    .where('organizerId', '==', userId)
    .get();
  return snapshot.size;
}

async function getEventsAttended(userId: string): Promise<number> {
  const snapshot = await db.collection('tickets')
    .where('userId', '==', userId)
    .where('status', '==', 'active')
    .get();
  return snapshot.size;
}

async function getRecapsCreated(userId: string): Promise<number> {
  const snapshot = await db.collection('recaps')
    .where('userId', '==', userId)
    .get();
  return snapshot.size;
}

async function getTotalLikes(userId: string): Promise<number> {
  const snapshot = await db.collection('recaps')
    .where('userId', '==', userId)
    .get();
  
  let totalLikes = 0;
  snapshot.docs.forEach(doc => {
    totalLikes += doc.data().likes || 0;
  });
  
  return totalLikes;
}

async function getTotalViews(userId: string): Promise<number> {
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
function determineUserRank(engagementScore: number): string {
  if (engagementScore >= 1000) return 'SUPERSTAR';
  if (engagementScore >= 500) return 'ROCKSTAR';
  if (engagementScore >= 250) return 'FAMOUS';
  if (engagementScore >= 100) return 'INFLUENCER';
  if (engagementScore >= 50) return 'POPULAR';
  if (engagementScore >= 25) return 'PARTY_POPPER';
  if (engagementScore >= 10) return 'BEGINNER';
  return 'NOVICE';
}

// Active Now System - Update User Location
export const updateUserLocation = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
  try {
    const { latitude, longitude, isActive, userId: explicitUserId } = data as { latitude: number; longitude: number; isActive: boolean; userId?: string };
    const userId = context.auth?.uid || explicitUserId;
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
  } catch (error) {
    console.error('Update location error:', error);
    throw new functions.https.HttpsError('internal', 'Failed to update location');
  }
});

// Get Active Users Near Location
export const getActiveUsersNearby = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30 }).https.onCall(async (data, context) => {
  try {
    const { latitude, longitude, radius = 5 } = data; // radius in km
    
    if (!context.auth) {
      throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }

    const usersSnapshot = await db.collection('users')
      .where('isActiveNow', '==', true)
      .get();

    const nearbyUsers = usersSnapshot.docs
      .map(doc => ({ id: doc.id, ...doc.data() }))
      .filter(user => {
        const userData = user as any;
        if (!userData.location) return false;
        
        const distance = calculateDistance(
          latitude, longitude,
          userData.location.latitude, userData.location.longitude
        );
        
        return distance <= radius;
      });

    return { users: nearbyUsers };
  } catch (error) {
    console.error('Get nearby users error:', error);
    throw new functions.https.HttpsError('internal', 'Failed to get nearby users');
  }
});

// Calculate Distance between two points
function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
  const R = 6371; // Earth's radius in km
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
    Math.sin(dLon/2) * Math.sin(dLon/2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  return R * c;
}

// Send Push Notification
export const sendPushNotification = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 30, minInstances: 1 }).https.onCall(async (data, context) => {
  try {
    const { userId, title, body, data: notificationData } = data;
    
    if (!context.auth) {
      throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }

    const userDoc = await db.collection('users').doc(userId).get();
    const fcmToken = userDoc.data()?.fcmToken;

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
        priority: 'high' as const,
        notification: {
          sound: 'default',
          channelId: 'littlegig_channel'
        }
      }
    };

    const response = await admin.messaging().send(message);
    return { success: true, messageId: response };
  } catch (error) {
    console.error('Send notification error:', error);
    throw new functions.https.HttpsError('internal', 'Failed to send notification');
  }
});

// Cleanup Old Data
export const cleanupOldData = functions.region('us-central1').runWith({ memory: '256MB', timeoutSeconds: 540 }).pubsub.schedule('0 0 * * 0').onRun(async (context) => {
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
  } catch (error) {
    console.error('Cleanup error:', error);
  }
});

// Export chat-related callable functions
export * from './chat';

export const seedDemoUsers = functions.region('us-central1').https.onCall(async (data, context) => {
  const count: number = Math.max(1, Math.min(20, (data && data.count) || 10));
  const batch = db.batch();
  for (let i = 0; i < count; i++) {
    const id = `demo_${Date.now()}_${Math.random().toString(36).slice(2,8)}`;
    const userRef = db.collection('users').doc(id);
    batch.set(userRef, {
      // Basic user info
      displayName: `Demo User ${i+1}`,
      username: `demo${i+1}`,
      email: `demo${i+1}@example.com`,
      name: `Demo User ${i+1}`,
      phoneNumber: `+254700000${i.toString().padStart(3, '0')}`,
      profileImageUrl: '',
      profilePictureUrl: '',
      
      // User type and rank
      userType: 'REGULAR',
      rank: 'NOVICE',
      
      // Lists
      followers: [],
      following: [],
      pinnedChats: [],
      likedEvents: [],
      
      // Analytics and timestamps
      engagementScore: 0.0,
      lastRankUpdate: admin.firestore.FieldValue.serverTimestamp(),
      bio: `This is demo user ${i+1}`,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
      
      // Lowercase search fields
      username_lower: `demo${i+1}`,
      email_lower: `demo${i+1}@example.com`,
      displayName_lower: `demo user ${i+1}`,
      
      // Status fields
      lastSeen: Date.now(),
      online: false,
      
      // Legacy field mapping
      influencer: false,
      isInfluencer: false
    });
  }
  await batch.commit();
  return { success: true, created: count };
});