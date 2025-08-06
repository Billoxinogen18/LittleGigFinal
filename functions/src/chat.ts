import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// Chat System Cloud Functions
export const createChat = functions.https.onCall(async (data, context) => {
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
                        throw new functions.https.HttpsError(
                            'permission-denied',
                            'Cannot start chat with private user'
                        );
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
    } catch (error) {
        console.error('Error creating chat:', error);
        throw new functions.https.HttpsError('internal', 'Failed to create chat');
    }
});

export const sendMessage = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { chatId, content, messageType, mediaUrls, sharedTicket } = data;
    
    try {
        // Verify user is in chat
        const chatDoc = await admin.firestore()
            .collection('chats')
            .doc(chatId)
            .get();
        
        if (!chatDoc.exists) {
            throw new functions.https.HttpsError('not-found', 'Chat not found');
        }
        
        const chat = chatDoc.data();
        if (!chat || !chat.participants.includes(userId)) {
            throw new functions.https.HttpsError('permission-denied', 'Not a member of this chat');
        }
        
        // Get user data
        const userDoc = await admin.firestore()
            .collection('users')
            .doc(userId)
            .get();
        
        const user = userDoc.data();
        
        // Create message
        const messageData = {
            chatId,
            senderId: userId,
            senderName: user?.displayName || 'Anonymous',
            senderImageUrl: user?.profileImageUrl || '',
            content,
            messageType,
            mediaUrls: mediaUrls || [],
            sharedTicket: sharedTicket || null,
            timestamp: admin.firestore.FieldValue.serverTimestamp(),
            isRead: false
        };
        
        const messageRef = await admin.firestore()
            .collection('messages')
            .add(messageData);
        
        // Update chat last message
        await admin.firestore()
            .collection('chats')
            .doc(chatId)
            .update({
                lastMessage: messageData,
                lastMessageTime: admin.firestore.FieldValue.serverTimestamp()
            });
        
        // Send push notifications to other participants
        const otherParticipants = chat?.participants?.filter((id: string) => id !== userId) || [];
        for (const participantId of otherParticipants) {
            await sendChatNotification(participantId, chat?.name || 'Chat', user?.displayName || 'Someone', content, chatId);
        }
        
        return { success: true, messageId: messageRef.id };
    } catch (error) {
        console.error('Error sending message:', error);
        throw new functions.https.HttpsError('internal', 'Failed to send message');
    }
});

export const shareTicket = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { chatId, ticketId } = data;
    
    try {
        // Verify user owns the ticket
        const ticketDoc = await admin.firestore()
            .collection('tickets')
            .doc(ticketId)
            .get();
        
        if (!ticketDoc.exists) {
            throw new functions.https.HttpsError('not-found', 'Ticket not found');
        }
        
        const ticket = ticketDoc.data();
        if (!ticket || ticket.userId !== userId) {
            throw new functions.https.HttpsError('permission-denied', 'You do not own this ticket');
        }
        
        if (ticket.isRedeemed) {
            throw new functions.https.HttpsError('permission-denied', 'Ticket already redeemed');
        }
        
        // Get event data
        const eventDoc = await admin.firestore()
            .collection('events')
            .doc(ticket.eventId)
            .get();
        
        const event = eventDoc.data();
        
        // Create shared ticket data
        const sharedTicket = {
            ticketId,
            eventName: event?.title || 'Unknown Event',
            eventImageUrl: event?.imageUrls?.[0] || '',
            ticketType: ticket.ticketType,
            price: ticket.price,
            isRedeemed: false,
            redeemedBy: null,
            redeemedAt: null
        };
        
        // Send ticket share message
        await sendMessage({
            chatId,
            content: 'Shared a ticket',
            messageType: 'TICKET_SHARE',
            sharedTicket
        } as any, { auth: { uid: userId } } as any);
        
        return { success: true };
    } catch (error) {
        console.error('Error sharing ticket:', error);
        throw new functions.https.HttpsError('internal', 'Failed to share ticket');
    }
});

export const redeemTicket = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { messageId, ticketId } = data;
    
    try {
        // Use transaction to ensure atomicity
        const result = await admin.firestore().runTransaction(async (transaction) => {
            // Get the shared ticket message
            const messageDoc = await transaction.get(
                admin.firestore().collection('messages').doc(messageId)
            );
            
            if (!messageDoc.exists) {
                throw new functions.https.HttpsError('not-found', 'Message not found');
            }
            
            const message = messageDoc.data();
            const sharedTicket = message?.sharedTicket;
            
            if (!sharedTicket || sharedTicket.ticketId !== ticketId) {
                throw new functions.https.HttpsError('invalid-argument', 'Invalid ticket');
            }
            
            if (sharedTicket.isRedeemed) {
                throw new functions.https.HttpsError('permission-denied', 'Ticket already redeemed');
            }
            
            // Update ticket as redeemed
            transaction.update(
                admin.firestore().collection('tickets').doc(ticketId),
                {
                    isRedeemed: true,
                    redeemedBy: userId,
                    redeemedAt: admin.firestore.FieldValue.serverTimestamp()
                }
            );
            
            // Update message shared ticket
            transaction.update(
                admin.firestore().collection('messages').doc(messageId),
                {
                    'sharedTicket.isRedeemed': true,
                    'sharedTicket.redeemedBy': userId,
                    'sharedTicket.redeemedAt': admin.firestore.FieldValue.serverTimestamp()
                }
            );
            
            return { success: true };
        });
        
        return result;
    } catch (error) {
        console.error('Error redeeming ticket:', error);
        throw new functions.https.HttpsError('internal', 'Failed to redeem ticket');
    }
});

export const updateTypingStatus = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    
    const userId = context.auth.uid;
    const { chatId, isTyping } = data;
    
    try {
        const userDoc = await admin.firestore()
            .collection('users')
            .doc(userId)
            .get();
        
        const user = userDoc.data();
        
        if (isTyping) {
            // Set typing indicator
            await admin.firestore()
                .collection('typingIndicators')
                .doc(`${chatId}_${userId}`)
                .set({
                    chatId,
                    userId,
                    userName: user?.displayName || 'Anonymous',
                    isTyping: true,
                    timestamp: admin.firestore.FieldValue.serverTimestamp()
                });
        } else {
            // Remove typing indicator
            await admin.firestore()
                .collection('typingIndicators')
                .doc(`${chatId}_${userId}`)
                .delete();
        }
        
        return { success: true };
    } catch (error) {
        console.error('Error updating typing status:', error);
        throw new functions.https.HttpsError('internal', 'Failed to update typing status');
    }
});

async function sendChatNotification(userId: string, chatName: string, senderName: string, message: string, chatId?: string) {
    try {
        // Get user's FCM token
        const userDoc = await admin.firestore()
            .collection('users')
            .doc(userId)
            .get();
        
        const user = userDoc.data();
        const fcmToken = user?.fcmToken;
        
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
    } catch (error) {
        console.error('Error sending chat notification:', error);
    }
} 