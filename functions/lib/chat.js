"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.updateTypingStatus = exports.redeemTicket = exports.shareTicket = exports.setAnnouncement = exports.pinMessage = exports.sendMessage = exports.createChat = void 0;
const functions = require("firebase-functions");
const admin = require("firebase-admin");
// Chat System Cloud Functions
exports.createChat = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { name, type, participants, eventId } = data;
    try {
        // Verify user can create chat
        if (type === 'DIRECT' && participants.length !== 2) {
            throw new functions.https.HttpsError('invalid-argument', 'Direct chats must have exactly 2 participants');
        }
        // Check if participants exist and are public (for non-followers)
        for (const participantId of participants) {
            if (participantId !== userId) {
                const userDoc = await admin.firestore()
                    .collection('users')
                    .doc(participantId)
                    .get();
                if (!userDoc.exists) {
                    throw new functions.https.HttpsError('not-found', 'User not found');
                }
                const user = userDoc.data();
                if (!user || !user.isPublic) {
                    // Check if current user follows this user
                    const followDoc = await admin.firestore()
                        .collection('follows')
                        .doc(`${userId}_${participantId}`)
                        .get();
                    if (!followDoc.exists) {
                        throw new functions.https.HttpsError('permission-denied', 'Cannot start chat with private user');
                    }
                }
            }
        }
        // Create chat
        const chatData = {
            name,
            type,
            participants: [...participants, userId],
            admins: [userId],
            eventId: eventId || null,
            createdAt: admin.firestore.FieldValue.serverTimestamp(),
            isActive: true
        };
        const chatRef = await admin.firestore()
            .collection('chats')
            .add(chatData);
        return { success: true, chatId: chatRef.id };
    }
    catch (error) {
        console.error('Error creating chat:', error);
        throw new functions.https.HttpsError('internal', 'Failed to create chat');
    }
});
exports.sendMessage = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, content = '', messageType = 'TEXT', mediaUrls = [], sharedTicket = null } = data;
    try {
        // Verify chat and membership
        const chatDoc = await admin.firestore().collection('chats').doc(chatId).get();
        if (!chatDoc.exists)
            throw new functions.https.HttpsError('not-found', 'Chat not found');
        const chat = chatDoc.data();
        if (!(chat.participants || []).includes(userId)) {
            throw new functions.https.HttpsError('permission-denied', 'Not a member of this chat');
        }
        // Get sender data
        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        const user = userDoc.data();
        // Create nested message under chat
        const messageData = {
            chatId,
            senderId: userId,
            senderName: (user === null || user === void 0 ? void 0 : user.displayName) || 'Anonymous',
            senderImageUrl: (user === null || user === void 0 ? void 0 : user.profileImageUrl) || '',
            content,
            messageType,
            mediaUrls,
            sharedTicket,
            timestamp: admin.firestore.FieldValue.serverTimestamp(),
            isRead: false,
            readBy: []
        };
        const msgRef = await admin.firestore().collection('chats').doc(chatId).collection('messages').add(messageData);
        // Update chat summary
        await chatDoc.ref.update({
            lastMessage: content || (messageType === 'IMAGE' ? '📷 Photo' : messageType === 'VIDEO' ? '🎬 Video' : 'New message'),
            lastMessageTime: admin.firestore.FieldValue.serverTimestamp(),
            lastMessageSenderId: userId
        });
        // Push notifications to other participants
        const other = (chat.participants || []).filter((id) => id !== userId);
        for (const pid of other) {
            await sendChatNotification(pid, (chat === null || chat === void 0 ? void 0 : chat.name) || 'Chat', (user === null || user === void 0 ? void 0 : user.displayName) || 'Someone', content || messageType, chatId);
        }
        return { success: true, messageId: msgRef.id };
    }
    catch (error) {
        console.error('Error sending message:', error);
        throw new functions.https.HttpsError('internal', 'Failed to send message');
    }
});
exports.pinMessage = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, messageId } = data;
    try {
        const chatDoc = await admin.firestore().collection('chats').doc(chatId).get();
        if (!chatDoc.exists)
            throw new functions.https.HttpsError('not-found', 'Chat not found');
        const chat = chatDoc.data();
        if (!(chat.admins || []).includes(userId)) {
            throw new functions.https.HttpsError('permission-denied', 'Only admins can pin');
        }
        await chatDoc.ref.update({ pinnedMessageId: messageId, pinnedAt: admin.firestore.FieldValue.serverTimestamp() });
        return { success: true };
    }
    catch (e) {
        console.error('pinMessage error', e);
        throw new functions.https.HttpsError('internal', 'Failed to pin message');
    }
});
exports.setAnnouncement = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, text } = data;
    try {
        const chatRef = admin.firestore().collection('chats').doc(chatId);
        const chatDoc = await chatRef.get();
        if (!chatDoc.exists)
            throw new functions.https.HttpsError('not-found', 'Chat not found');
        const chat = chatDoc.data();
        if (!(chat.admins || []).includes(userId)) {
            throw new functions.https.HttpsError('permission-denied', 'Only admins can set announcement');
        }
        await chatRef.update({ announcement: text || '', announcementAt: admin.firestore.FieldValue.serverTimestamp() });
        return { success: true };
    }
    catch (e) {
        console.error('setAnnouncement error', e);
        throw new functions.https.HttpsError('internal', 'Failed to set announcement');
    }
});
exports.shareTicket = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, ticketId } = data;
    try {
        // Verify user owns the ticket
        const ticketDoc = await admin.firestore().collection('tickets').doc(ticketId).get();
        if (!ticketDoc.exists)
            throw new functions.https.HttpsError('not-found', 'Ticket not found');
        const ticket = ticketDoc.data();
        if (ticket.userId !== userId)
            throw new functions.https.HttpsError('permission-denied', 'You do not own this ticket');
        if (ticket.isRedeemed)
            throw new functions.https.HttpsError('permission-denied', 'Ticket already redeemed');
        // Get event data
        const eventDoc = await admin.firestore().collection('events').doc(ticket.eventId).get();
        const event = eventDoc.data();
        // Build shared ticket payload
        const sharedTicket = {
            ticketId,
            eventName: (event === null || event === void 0 ? void 0 : event.title) || 'Event',
            eventImageUrl: ((event === null || event === void 0 ? void 0 : event.imageUrls) && event.imageUrls[0]) || '',
            ticketType: ticket.ticketType || 'General',
            price: ticket.price || ticket.totalAmount || 0,
            isRedeemed: false,
            redeemedBy: null,
            redeemedAt: null
        };
        // Use sendMessage implementation to write and notify
        const res = await (0, exports.sendMessage)({ chatId, content: 'Shared a ticket', messageType: 'TICKET_SHARE', sharedTicket }, { auth: { uid: userId } });
        return res;
    }
    catch (error) {
        console.error('Error sharing ticket:', error);
        throw new functions.https.HttpsError('internal', 'Failed to share ticket');
    }
});
exports.redeemTicket = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, messageId, ticketId } = data;
    try {
        // Transactional update
        const result = await admin.firestore().runTransaction(async (transaction) => {
            var _a;
            // Resolve message path
            let messageRef;
            if (chatId) {
                messageRef = admin.firestore().collection('chats').doc(chatId).collection('messages').doc(messageId);
            }
            else {
                // Fallback legacy path (not recommended)
                messageRef = admin.firestore().collection('messages').doc(messageId);
            }
            const messageDoc = await transaction.get(messageRef);
            if (!messageDoc.exists)
                throw new functions.https.HttpsError('not-found', 'Message not found');
            const message = messageDoc.data();
            const sharedTicket = message === null || message === void 0 ? void 0 : message.sharedTicket;
            if (!sharedTicket || sharedTicket.ticketId !== ticketId) {
                throw new functions.https.HttpsError('invalid-argument', 'Invalid ticket');
            }
            if (sharedTicket.isRedeemed)
                throw new functions.https.HttpsError('permission-denied', 'Ticket already redeemed');
            // Update ticket: transfer ownership to redeemer and mark as used
            const ticketRef = admin.firestore().collection('tickets').doc(ticketId);
            const ticketSnap = await transaction.get(ticketRef);
            if (!ticketSnap.exists)
                throw new functions.https.HttpsError('not-found', 'Ticket not found');
            const previousOwnerId = ((_a = ticketSnap.data()) === null || _a === void 0 ? void 0 : _a.userId) || null;
            transaction.update(ticketRef, {
                isRedeemed: true,
                redeemedBy: userId,
                redeemedAt: admin.firestore.FieldValue.serverTimestamp(),
                status: 'USED',
                usedDate: admin.firestore.FieldValue.serverTimestamp(),
                userId: userId,
                transferredFrom: previousOwnerId,
                transferredAt: admin.firestore.FieldValue.serverTimestamp()
            });
            // Update message shared payload to reflect redemption
            transaction.update(messageRef, {
                'sharedTicket.isRedeemed': true,
                'sharedTicket.redeemedBy': userId,
                'sharedTicket.redeemedAt': admin.firestore.FieldValue.serverTimestamp()
            });
            return { success: true };
        });
        return result;
    }
    catch (error) {
        console.error('Error redeeming ticket:', error);
        throw new functions.https.HttpsError('internal', 'Failed to redeem ticket');
    }
});
exports.updateTypingStatus = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const userId = context.auth.uid;
    const { chatId, isTyping } = data;
    try {
        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        const user = userDoc.data();
        const typingRef = admin.firestore().collection('chats').doc(chatId).collection('typing').doc(userId);
        if (isTyping) {
            await typingRef.set({
                userId,
                userName: (user === null || user === void 0 ? void 0 : user.displayName) || 'Anonymous',
                isTyping: true,
                timestamp: admin.firestore.FieldValue.serverTimestamp()
            });
        }
        else {
            await typingRef.delete();
        }
        return { success: true };
    }
    catch (error) {
        console.error('Error updating typing status:', error);
        throw new functions.https.HttpsError('internal', 'Failed to update typing status');
    }
});
async function sendChatNotification(userId, chatName, senderName, message, chatId) {
    try {
        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        const user = userDoc.data();
        const fcmToken = user === null || user === void 0 ? void 0 : user.fcmToken;
        if (fcmToken) {
            const notification = {
                token: fcmToken,
                notification: {
                    title: `${senderName} in ${chatName}`,
                    body: message.length > 50 ? message.substring(0, 50) + '...' : message
                },
                data: {
                    type: 'chat_message',
                    chatId: chatId || ''
                }
            };
            await admin.messaging().send(notification);
        }
    }
    catch (error) {
        console.error('Error sending chat notification:', error);
    }
}
//# sourceMappingURL=chat.js.map