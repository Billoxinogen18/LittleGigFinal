import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// Recaps System Cloud Functions
export const createRecap = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { eventId, mediaUrls, caption, location } = data;
    
    try {
        // Verify user is within 3km of event location
        const eventDoc = await admin.firestore()
            .collection('events')
            .doc(eventId)
            .get();
        
        if (!eventDoc.exists) {
            throw new functions.https.HttpsError('not-found', 'Event not found');
        }
        
        const event = eventDoc.data();
        const eventLocation = event?.location;
        
        // Calculate distance between user and event
        const distance = calculateDistance(
            location.latitude,
            location.longitude,
            eventLocation.latitude,
            eventLocation.longitude
        );
        
        if (distance > 3.0) { // 3km limit
            throw new functions.https.HttpsError(
                'permission-denied',
                'You must be within 3km of the event to post a recap'
            );
        }
        
        // Check if event has started
        const now = Date.now();
        if (event && event.dateTime > now) {
            throw new functions.https.HttpsError(
                'permission-denied',
                'Event has not started yet'
            );
        }
        
        // Get user data
        const userDoc = await admin.firestore()
            .collection('users')
            .doc(userId)
            .get();
        
        const user = userDoc.data();
        
        // Create recap
        const recapData = {
            eventId,
            userId,
            userName: user?.displayName || 'Anonymous',
            userImageUrl: user?.profileImageUrl || '',
            mediaUrls,
            caption,
            location,
            likes: 0,
            views: 0,
            shares: 0,
            createdAt: admin.firestore.FieldValue.serverTimestamp(),
            isActive: true
        };
        
        const recapRef = await admin.firestore()
            .collection('recaps')
            .add(recapData);
        
        // Update event recap count
        await admin.firestore()
            .collection('events')
            .doc(eventId)
            .update({
                recapCount: admin.firestore.FieldValue.increment(1)
            });
        
        return { success: true, recapId: recapRef.id };
    } catch (error) {
        console.error('Error creating recap:', error);
        throw new functions.https.HttpsError('internal', 'Failed to create recap');
    }
});

export const likeRecap = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { recapId } = data;
    
    try {
        const recapRef = admin.firestore().collection('recaps').doc(recapId);
        
        // Check if user already liked
        const likeDoc = await admin.firestore()
            .collection('recapLikes')
            .doc(`${recapId}_${userId}`)
            .get();
        
        if (likeDoc.exists) {
            // Unlike
            await likeDoc.ref.delete();
            await recapRef.update({
                likes: admin.firestore.FieldValue.increment(-1)
            });
            return { success: true, liked: false };
        } else {
            // Like
            await admin.firestore()
                .collection('recapLikes')
                .doc(`${recapId}_${userId}`)
                .set({
                    recapId,
                    userId,
                    createdAt: admin.firestore.FieldValue.serverTimestamp()
                });
            
            await recapRef.update({
                likes: admin.firestore.FieldValue.increment(1)
            });
            
            return { success: true, liked: true };
        }
    } catch (error) {
        console.error('Error liking recap:', error);
        throw new functions.https.HttpsError('internal', 'Failed to like recap');
    }
});

export const viewRecap = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { recapId } = data;
    
    try {
        // Check if user already viewed
        const viewDoc = await admin.firestore()
            .collection('recapViews')
            .doc(`${recapId}_${userId}`)
            .get();
        
        if (!viewDoc.exists) {
            // Record view
            await admin.firestore()
                .collection('recapViews')
                .doc(`${recapId}_${userId}`)
                .set({
                    recapId,
                    userId,
                    viewedAt: admin.firestore.FieldValue.serverTimestamp()
                });
            
            // Increment view count
            await admin.firestore()
                .collection('recaps')
                .doc(recapId)
                .update({
                    views: admin.firestore.FieldValue.increment(1)
                });
        }
        
        return { success: true };
    } catch (error) {
        console.error('Error viewing recap:', error);
        throw new functions.https.HttpsError('internal', 'Failed to record view');
    }
});

export const getEventRecaps = functions.https.onCall(async (data, context) => {
    const { eventId, limit = 20 } = data;
    
    try {
        const recapsSnapshot = await admin.firestore()
            .collection('recaps')
            .where('eventId', '==', eventId)
            .where('isActive', '==', true)
            .orderBy('likes', 'desc')
            .orderBy('views', 'desc')
            .orderBy('createdAt', 'desc')
            .limit(limit)
            .get();
        
        const recaps = recapsSnapshot.docs.map(doc => ({
            id: doc.id,
            ...doc.data()
        }));
        
        return { success: true, recaps };
    } catch (error) {
        console.error('Error getting event recaps:', error);
        throw new functions.https.HttpsError('internal', 'Failed to get recaps');
    }
});

// Calculate distance between two points using Haversine formula
function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371; // Earth's radius in kilometers
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
} 