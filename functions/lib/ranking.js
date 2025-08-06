"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.updateGlobalRankings = exports.calculateUserRanking = void 0;
const functions = __importStar(require("firebase-functions"));
const admin = __importStar(require("firebase-admin"));
// Ranking System Cloud Functions
exports.calculateUserRanking = functions.firestore
    .document('users/{userId}')
    .onWrite(async (change, context) => {
    const userId = context.params.userId;
    const userData = change.after.data();
    if (!userData)
        return null;
    try {
        // Get user engagement data
        const engagementData = await calculateUserEngagement(userId);
        // Update user with new ranking
        await admin.firestore()
            .collection('users')
            .doc(userId)
            .update({
            rank: engagementData.rank,
            rankPercentage: engagementData.rankPercentage,
            totalScore: engagementData.totalScore,
            lastRankingUpdate: admin.firestore.FieldValue.serverTimestamp()
        });
        return { success: true, ranking: engagementData };
    }
    catch (error) {
        console.error('Error calculating user ranking:', error);
        throw new functions.https.HttpsError('internal', 'Failed to calculate ranking');
    }
});
exports.updateGlobalRankings = functions.pubsub
    .schedule('every 1 hours')
    .onRun(async (context) => {
    try {
        const usersSnapshot = await admin.firestore()
            .collection('users')
            .where('isActive', '==', true)
            .get();
        const userEngagements = [];
        // Calculate engagement for all users
        for (const userDoc of usersSnapshot.docs) {
            const engagement = await calculateUserEngagement(userDoc.id);
            userEngagements.push(Object.assign({ userId: userDoc.id }, engagement));
        }
        // Sort by total score for percentage calculation
        userEngagements.sort((a, b) => b.totalScore - a.totalScore);
        // Update rankings with percentages
        for (let i = 0; i < userEngagements.length; i++) {
            const user = userEngagements[i];
            const percentage = ((i + 1) / userEngagements.length) * 100;
            await admin.firestore()
                .collection('users')
                .doc(user.userId)
                .update({
                rank: user.rank,
                rankPercentage: percentage,
                globalRank: i + 1,
                totalUsers: userEngagements.length
            });
        }
        console.log(`Updated rankings for ${userEngagements.length} users`);
        return { success: true, usersUpdated: userEngagements.length };
    }
    catch (error) {
        console.error('Error updating global rankings:', error);
        throw new functions.https.HttpsError('internal', 'Failed to update rankings');
    }
});
async function calculateUserEngagement(userId) {
    const db = admin.firestore();
    // Get user's events
    const eventsSnapshot = await db
        .collection('events')
        .where('organizerId', '==', userId)
        .where('isActive', '==', true)
        .get();
    // Get user's recaps
    const recapsSnapshot = await db
        .collection('recaps')
        .where('userId', '==', userId)
        .where('isActive', '==', true)
        .get();
    // Get user's attended events
    const attendedEventsSnapshot = await db
        .collection('eventAttendees')
        .where('userId', '==', userId)
        .where('status', '==', 'attended')
        .get();
    // Calculate event engagement
    let eventEngagement = 0;
    let totalAttendance = 0;
    let totalEventRating = 0;
    let totalEventShares = 0;
    eventsSnapshot.docs.forEach(doc => {
        const event = doc.data();
        totalAttendance += event.ticketsSold || 0;
        totalEventRating += event.averageRating || 0;
        totalEventShares += event.shares || 0;
    });
    const eventsCreated = eventsSnapshot.size;
    const averageRating = eventsCreated > 0 ? totalEventRating / eventsCreated : 0;
    eventEngagement = (eventsCreated * 10.0) +
        (totalAttendance * 0.5) +
        (averageRating * 20.0) +
        (totalEventShares * 2.0);
    // Calculate recap engagement
    let recapEngagement = 0;
    let totalViews = 0;
    let totalLikes = 0;
    let totalShares = 0;
    recapsSnapshot.docs.forEach(doc => {
        const recap = doc.data();
        totalViews += recap.views || 0;
        totalLikes += recap.likes || 0;
        totalShares += recap.shares || 0;
    });
    const recapsPosted = recapsSnapshot.size;
    recapEngagement = (recapsPosted * 5.0) +
        (totalViews * 0.1) +
        (totalLikes * 0.5) +
        (totalShares * 1.0);
    // Calculate events attended
    const eventsAttended = attendedEventsSnapshot.size;
    // Calculate platform activity (simplified)
    const platformActivity = (eventsCreated * 2.0) +
        (recapsPosted * 1.0) +
        (eventsAttended * 0.5);
    // Normalize scores
    const normalizedEventEngagement = Math.min(eventEngagement / 100.0, 1.0);
    const normalizedRecapEngagement = Math.min(recapEngagement / 50.0, 1.0);
    const normalizedEventsAttended = Math.min(eventsAttended / 20.0, 1.0);
    const normalizedPlatformActivity = Math.min(platformActivity / 30.0, 1.0);
    // Calculate total score with weights
    const totalScore = (normalizedEventEngagement * 0.40) +
        (normalizedRecapEngagement * 0.35) +
        (normalizedEventsAttended * 0.15) +
        (normalizedPlatformActivity * 0.10);
    // Determine rank
    const rank = getRankFromScore(totalScore);
    const rankPercentage = totalScore * 100.0;
    return {
        eventEngagement,
        recapEngagement,
        eventsAttended,
        platformActivity,
        totalScore,
        rank,
        rankPercentage
    };
}
function getRankFromScore(score) {
    if (score >= 0.95)
        return 'SUPERSTAR';
    if (score >= 0.85)
        return 'ROCKSTAR';
    if (score >= 0.70)
        return 'FAMOUS';
    if (score >= 0.55)
        return 'INFLUENCER';
    if (score >= 0.40)
        return 'POPULAR';
    if (score >= 0.25)
        return 'PARTY_POPPER';
    if (score >= 0.10)
        return 'BEGINNER';
    return 'NOVICE';
}
//# sourceMappingURL=ranking.js.map