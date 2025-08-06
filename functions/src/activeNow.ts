import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// Active Now System Cloud Functions
export const updateActiveStatus = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { isActive, location } = data;
    
    try {
        const userDoc = await admin.firestore()
            .collection('users')
            .doc(userId)
            .get();
        
        const user = userDoc.data();
        
        if (isActive) {
            // Update user as active with location
            await admin.firestore()
                .collection('users')
                .doc(userId)
                .update({
                    isActiveNow: true,
                    lastActiveLocation: location,
                    lastActiveTime: admin.firestore.FieldValue.serverTimestamp()
                });
            
            // Add to active users collection for map display
            await admin.firestore()
                .collection('activeUsers')
                .doc(userId)
                .set({
                    userId,
                    userName: user?.displayName || 'Anonymous',
                    userImageUrl: user?.profileImageUrl || '',
                    location,
                    rank: user?.rank || 'NOVICE',
                    lastActiveTime: admin.firestore.FieldValue.serverTimestamp()
                });
        } else {
            // Update user as inactive
            await admin.firestore()
                .collection('users')
                .doc(userId)
                .update({
                    isActiveNow: false,
                    lastActiveTime: admin.firestore.FieldValue.serverTimestamp()
                });
            
            // Remove from active users
            await admin.firestore()
                .collection('activeUsers')
                .doc(userId)
                .delete();
        }
        
        return { success: true };
    } catch (error) {
        console.error('Error updating active status:', error);
        throw new functions.https.HttpsError('internal', 'Failed to update active status');
    }
});

export const getActiveUsers = functions.https.onCall(async (data, context) => {
    const { latitude, longitude, radius = 10 } = data; // Default 10km radius
    
    try {
        const activeUsersSnapshot = await admin.firestore()
            .collection('activeUsers')
            .get();
        
        const activeUsers = activeUsersSnapshot.docs
            .map(doc => ({
                id: doc.id,
                ...doc.data()
            } as any))
            .filter((user: any) => {
                if (!user.location || !latitude || !longitude) return false;
                
                const distance = calculateDistance(
                    latitude,
                    longitude,
                    user.location.latitude,
                    user.location.longitude
                );
                
                return distance <= radius;
            })
            .sort((a: any, b: any) => {
                // Sort by distance, then by rank priority
                const distanceA = calculateDistance(
                    latitude,
                    longitude,
                    a.location.latitude,
                    a.location.longitude
                );
                const distanceB = calculateDistance(
                    latitude,
                    longitude,
                    b.location.latitude,
                    b.location.longitude
                );
                
                if (Math.abs(distanceA - distanceB) < 0.1) {
                    // If distances are similar, sort by rank
                    const rankPriority: { [key: string]: number } = {
                        'SUPERSTAR': 8,
                        'ROCKSTAR': 7,
                        'FAMOUS': 6,
                        'INFLUENCER': 5,
                        'POPULAR': 4,
                        'PARTY_POPPER': 3,
                        'BEGINNER': 2,
                        'NOVICE': 1
                    };
                    
                    return (rankPriority[b.rank] || 1) - (rankPriority[a.rank] || 1);
                }
                
                return distanceA - distanceB;
            });
        
        return { success: true, activeUsers };
    } catch (error) {
        console.error('Error getting active users:', error);
        throw new functions.https.HttpsError('internal', 'Failed to get active users');
    }
});

export const cleanupInactiveUsers = functions.pubsub
    .schedule('every 5 minutes')
    .onRun(async (context) => {
        try {
            const fiveMinutesAgo = new Date(Date.now() - 5 * 60 * 1000);
            
            const inactiveUsersSnapshot = await admin.firestore()
                .collection('activeUsers')
                .where('lastActiveTime', '<', fiveMinutesAgo)
                .get();
            
            const batch = admin.firestore().batch();
            
            inactiveUsersSnapshot.docs.forEach(doc => {
                batch.delete(doc.ref);
                
                // Update user as inactive
                batch.update(
                    admin.firestore().collection('users').doc(doc.id),
                    { isActiveNow: false }
                );
            });
            
            await batch.commit();
            
            console.log(`Cleaned up ${inactiveUsersSnapshot.size} inactive users`);
            return { success: true, cleanedUp: inactiveUsersSnapshot.size };
        } catch (error) {
            console.error('Error cleaning up inactive users:', error);
            throw new functions.https.HttpsError('internal', 'Failed to cleanup inactive users');
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