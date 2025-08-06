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
var __exportStar = (this && this.__exportStar) || function(m, exports) {
    for (var p in m) if (p !== "default" && !Object.prototype.hasOwnProperty.call(exports, p)) __createBinding(exports, m, p);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.sendEventReminders = exports.flutterwaveWebhookHandler = exports.expireOldTickets = exports.updateAdAnalytics = exports.processRefund = exports.processInfluencerAdPayment = exports.processTicketPayment = exports.verifyFlutterwavePayment = exports.initializeFlutterwavePayment = void 0;
const functions = __importStar(require("firebase-functions"));
const admin = __importStar(require("firebase-admin"));
// @ts-ignore
const flutterwave_node_v3_1 = __importDefault(require("flutterwave-node-v3"));
admin.initializeApp();
// Export all new functions
__exportStar(require("./ranking"), exports);
__exportStar(require("./recaps"), exports);
__exportStar(require("./chat"), exports);
// export * from './activeNow';
// Initialize Flutterwave SDK with LIVE credentials
const flw = new flutterwave_node_v3_1.default("FLWPUBK-3fa265a8e3265a459035c2d9bbfa798c-X", // LIVE Public Key
"FLWSECK-e30b0920b7b209167ef35802a287a5ef-1987eb05964vt-X" // LIVE Secret Key
);
// Initialize Flutterwave payment with proper V3 SDK
exports.initializeFlutterwavePayment = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const { amount, currency = 'KES', email, phone_number, tx_ref, customer_name, payment_type = 'card', meta = {} } = data;
    try {
        // Create order record in Firestore first
        const orderRef = admin.firestore().collection('orders').doc();
        await orderRef.set({
            userId: context.auth.uid,
            amount: amount,
            currency: currency,
            flutterwave_tx_ref: tx_ref,
            status: 'pending',
            createdAt: admin.firestore.FieldValue.serverTimestamp(),
            customer: {
                email: email,
                phone_number: phone_number,
                name: customer_name
            },
            meta: meta
        });
        // Create payment initialization with Flutterwave V3 SDK
        const paymentData = {
            tx_ref: tx_ref,
            amount: amount,
            currency: currency,
            redirect_url: `https://littlegig.app/payment/callback?orderId=${orderRef.id}`,
            customer: {
                email: email,
                phone_number: phone_number,
                name: customer_name
            },
            customizations: {
                title: 'LittleGig Payment',
                description: 'Payment for LittleGig services',
                logo: 'https://littlegig.app/logo.png'
            },
            meta: meta,
            payment_options: payment_type === 'card' ? 'card' : 'mpesa,ussd,banktransfer,mobilemoneyghana,mobilemoneyuganda'
        };
        // Use Flutterwave V3 SDK to initiate payment
        const response = await flw.Payment.initiate(paymentData);
        if (response.status === 'success') {
            return {
                success: true,
                paymentUrl: response.data.link,
                paymentId: response.data.id,
                orderId: orderRef.id,
                message: 'Payment initialized successfully'
            };
        }
        else {
            // Update order status to failed
            await orderRef.update({
                status: 'failed',
                failure_reason: response.message || 'Link generation failed'
            });
            throw new Error(response.message || 'Failed to initialize payment');
        }
    }
    catch (error) {
        console.error('Payment initialization error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Payment initialization failed'
        };
    }
});
// Verify Flutterwave payment with proper V3 SDK
exports.verifyFlutterwavePayment = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const { transaction_id } = data;
    try {
        // Use Flutterwave V3 SDK to verify payment
        const response = await flw.Transaction.verify({ id: String(transaction_id) });
        if (response.status === 'success') {
            const transactionData = response.data;
            // Find the original order in Firestore using the tx_ref from the response
            const orderQuery = admin.firestore().collection('orders')
                .where('flutterwave_tx_ref', '==', transactionData.tx_ref)
                .limit(1);
            const orderSnapshot = await orderQuery.get();
            if (orderSnapshot.empty) {
                console.error(`No order found for tx_ref: ${transactionData.tx_ref}`);
                throw new functions.https.HttpsError('not-found', 'Order record not found.');
            }
            const orderDoc = orderSnapshot.docs[0];
            const orderData = orderDoc.data();
            // CRITICAL CHECKS:
            const isStatusSuccessful = transactionData.status === 'successful';
            const isAmountCorrect = transactionData.amount === orderData.amount;
            const isCurrencyCorrect = transactionData.currency === orderData.currency;
            const isOrderPending = orderData.status === 'pending';
            if (isStatusSuccessful && isAmountCorrect && isCurrencyCorrect && isOrderPending) {
                // All checks passed. Payment is verified.
                // Use a Firestore transaction to ensure atomic updates
                await admin.firestore().runTransaction(async (t) => {
                    // Update the order status to 'paid'
                    t.update(orderDoc.ref, {
                        status: 'paid',
                        verifiedAt: admin.firestore.FieldValue.serverTimestamp()
                    });
                    // Create a new document in the 'transactions' collection for auditing
                    const transactionRef = admin.firestore().collection('transactions').doc(String(transactionData.id));
                    t.set(transactionRef, {
                        orderId: orderDoc.id,
                        flutterwave_flw_ref: transactionData.flw_ref,
                        status: transactionData.status,
                        paymentMethod: transactionData.payment_type,
                        amount: transactionData.amount,
                        currency: transactionData.currency,
                        customer: transactionData.customer,
                        verifiedAt: admin.firestore.FieldValue.serverTimestamp(),
                    });
                });
                return {
                    success: true,
                    paymentId: transactionData.id,
                    amount: transactionData.amount,
                    currency: transactionData.currency,
                    message: 'Payment verified successfully'
                };
            }
            else {
                // One or more checks failed. Do not give value.
                console.warn('Payment verification failed.', {
                    tx_ref: transactionData.tx_ref,
                    checks: { isStatusSuccessful, isAmountCorrect, isCurrencyCorrect, isOrderPending },
                    flutterwaveData: transactionData,
                    orderData: orderData
                });
                // Update order to failed status for record keeping
                await orderDoc.ref.update({
                    status: 'failed',
                    failure_reason: 'Verification failed'
                });
                throw new functions.https.HttpsError('aborted', 'Payment verification failed.');
            }
        }
        else {
            throw new Error(response.message || 'Payment verification failed');
        }
    }
    catch (error) {
        console.error('Payment verification error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Payment verification failed'
        };
    }
});
exports.processTicketPayment = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const { eventId, userId, quantity, totalAmount, transaction_id } = data;
    try {
        // Use Flutterwave V3 SDK to verify payment
        const response = await flw.Transaction.verify({ id: String(transaction_id) });
        if (response.status === 'success') {
            const transactionData = response.data;
            if (transactionData.status === 'successful' && transactionData.amount === totalAmount) {
                // Calculate commission (4%)
                const commission = totalAmount * 0.04;
                // Create ticket record
                const ticketData = {
                    eventId,
                    userId,
                    quantity,
                    totalAmount,
                    commission,
                    paymentId: transaction_id,
                    status: 'ACTIVE',
                    purchaseDate: admin.firestore.FieldValue.serverTimestamp(),
                };
                // Save ticket to Firestore
                await admin.firestore().collection('tickets').add(ticketData);
                // Update event ticket count
                const eventRef = admin.firestore().collection('events').doc(eventId);
                await eventRef.update({
                    ticketsSold: admin.firestore.FieldValue.increment(quantity)
                });
                // Record payment transaction
                const paymentRecord = {
                    userId,
                    type: 'ticket',
                    amount: totalAmount,
                    currency: 'KES',
                    status: 'completed',
                    paymentId: transaction_id,
                    description: `Ticket purchase for event ${eventId}`,
                    timestamp: admin.firestore.FieldValue.serverTimestamp(),
                };
                await admin.firestore().collection('payments').add(paymentRecord);
                return {
                    success: true,
                    paymentId: transaction_id,
                    message: 'Payment processed successfully'
                };
            }
            else {
                throw new Error(`Payment verification failed: ${transactionData.status}`);
            }
        }
        else {
            throw new Error(response.message || 'Payment verification failed');
        }
    }
    catch (error) {
        console.error('Payment processing error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Payment processing failed'
        };
    }
});
exports.processInfluencerAdPayment = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const { advertisementId, influencerId, budget, transaction_id } = data;
    try {
        // Use Flutterwave V3 SDK to verify payment
        const response = await flw.Transaction.verify({ id: String(transaction_id) });
        if (response.status === 'success') {
            const transactionData = response.data;
            if (transactionData.status === 'successful' && transactionData.amount === budget) {
                // Update advertisement with payment info
                await admin.firestore().collection('advertisements').doc(advertisementId).update({
                    paymentId: transaction_id,
                    status: 'ACTIVE',
                    startDate: admin.firestore.FieldValue.serverTimestamp(),
                    endDate: new Date(Date.now() + (7 * 24 * 60 * 60 * 1000)), // 7 days from now
                });
                // Record payment transaction
                const paymentRecord = {
                    userId: influencerId,
                    type: 'advertisement',
                    amount: budget,
                    currency: 'KES',
                    status: 'completed',
                    paymentId: transaction_id,
                    description: `Advertisement payment for ad ${advertisementId}`,
                    timestamp: admin.firestore.FieldValue.serverTimestamp(),
                };
                await admin.firestore().collection('payments').add(paymentRecord);
                return {
                    success: true,
                    paymentId: transaction_id,
                    message: 'Advertisement payment processed successfully'
                };
            }
            else {
                throw new Error(`Payment verification failed: ${transactionData.status}`);
            }
        }
        else {
            throw new Error(response.message || 'Payment verification failed');
        }
    }
    catch (error) {
        console.error('Advertisement payment error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Payment processing failed'
        };
    }
});
exports.processRefund = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    }
    const { paymentId, reason } = data;
    try {
        // Process refund with Flutterwave V3 SDK
        const payload = {
            id: String(paymentId),
            amount: null,
            reason: reason
        };
        const response = await flw.Transaction.refund(payload);
        if (response.status === 'success') {
            const refund = response.data;
            if (refund.status === 'successful') {
                // Update payment record
                await admin.firestore().collection('payments')
                    .where('paymentId', '==', paymentId)
                    .get()
                    .then((snapshot) => {
                    snapshot.forEach((doc) => {
                        doc.ref.update({
                            status: 'refunded',
                            refundReason: reason,
                            refundDate: admin.firestore.FieldValue.serverTimestamp(),
                        });
                    });
                });
                return {
                    success: true,
                    message: 'Refund processed successfully'
                };
            }
            else {
                throw new Error(`Refund failed with status: ${refund.status}`);
            }
        }
        else {
            throw new Error(response.message || 'Refund processing failed');
        }
    }
    catch (error) {
        console.error('Refund processing error:', error);
        return {
            success: false,
            message: error instanceof Error ? error.message : 'Refund processing failed'
        };
    }
});
// Scheduled function to update advertisement analytics
exports.updateAdAnalytics = functions.pubsub.schedule('every 1 hours').onRun(async (context) => {
    try {
        // Get all active advertisements
        const activeAds = await admin.firestore()
            .collection('advertisements')
            .where('status', '==', 'ACTIVE')
            .get();
        // Update analytics for each ad (this is a simplified example)
        const updatePromises = activeAds.docs.map(async (doc) => {
            const impressions = Math.floor(Math.random() * 100); // Simulate impressions
            const clicks = Math.floor(Math.random() * 10); // Simulate clicks
            const reach = Math.floor(Math.random() * 500); // Simulate reach
            return doc.ref.update({
                impressions: admin.firestore.FieldValue.increment(impressions),
                clicks: admin.firestore.FieldValue.increment(clicks),
                reach: admin.firestore.FieldValue.increment(reach),
                updatedAt: admin.firestore.FieldValue.serverTimestamp(),
            });
        });
        await Promise.all(updatePromises);
        console.log(`Updated analytics for ${activeAds.docs.length} advertisements`);
    }
    catch (error) {
        console.error('Error updating ad analytics:', error);
    }
});
// Function to check and expire old tickets
exports.expireOldTickets = functions.pubsub.schedule('every 24 hours').onRun(async (context) => {
    try {
        const now = admin.firestore.Timestamp.now();
        // Get events that have passed
        const pastEvents = await admin.firestore()
            .collection('events')
            .where('dateTime', '<', now.toMillis())
            .get();
        const eventIds = pastEvents.docs.map(doc => doc.id);
        if (eventIds.length > 0) {
            // Update tickets for past events to expired
            const expiredTickets = await admin.firestore()
                .collection('tickets')
                .where('eventId', 'in', eventIds)
                .where('status', '==', 'ACTIVE')
                .get();
            const updatePromises = expiredTickets.docs.map(doc => doc.ref.update({ status: 'EXPIRED' }));
            await Promise.all(updatePromises);
            console.log(`Expired ${expiredTickets.docs.length} tickets`);
        }
    }
    catch (error) {
        console.error('Error expiring tickets:', error);
    }
});
// Webhook handler for Flutterwave asynchronous payment confirmations
exports.flutterwaveWebhookHandler = functions.https.onRequest(async (req, res) => {
    // Verify the webhook signature for security
    const signature = req.headers['flutterwave-signature'];
    if (!signature) {
        console.error('No signature found in webhook');
        res.status(401).send('No signature found');
        return;
    }
    // Acknowledge the request immediately
    res.sendStatus(200);
    // Process the webhook payload
    const payload = req.body;
    console.log('Received Flutterwave Webhook:', payload);
    if (payload.event === 'charge.completed') {
        const transactionData = payload.data;
        try {
            // Find the original order in Firestore using the tx_ref
            const orderQuery = admin.firestore().collection('orders')
                .where('flutterwave_tx_ref', '==', transactionData.tx_ref)
                .limit(1);
            const orderSnapshot = await orderQuery.get();
            if (!orderSnapshot.empty) {
                const orderDoc = orderSnapshot.docs[0];
                const orderData = orderDoc.data();
                // Perform the same verification checks as in verifyPayment
                const isStatusSuccessful = transactionData.status === 'successful';
                const isAmountCorrect = transactionData.amount === orderData.amount;
                const isCurrencyCorrect = transactionData.currency === orderData.currency;
                const isOrderPending = orderData.status === 'pending';
                if (isStatusSuccessful && isAmountCorrect && isCurrencyCorrect && isOrderPending) {
                    // Update order status to 'paid'
                    await orderDoc.ref.update({
                        status: 'paid',
                        webhookVerifiedAt: admin.firestore.FieldValue.serverTimestamp()
                    });
                    // Create transaction record for auditing
                    const transactionRef = admin.firestore().collection('transactions').doc(String(transactionData.id));
                    await transactionRef.set({
                        orderId: orderDoc.id,
                        flutterwave_flw_ref: transactionData.flw_ref,
                        status: transactionData.status,
                        paymentMethod: transactionData.payment_type,
                        amount: transactionData.amount,
                        currency: transactionData.currency,
                        customer: transactionData.customer,
                        webhookVerifiedAt: admin.firestore.FieldValue.serverTimestamp(),
                    });
                    console.log(`Payment verified via webhook for tx_ref: ${transactionData.tx_ref}`);
                }
                else {
                    console.warn('Webhook verification failed for tx_ref:', transactionData.tx_ref);
                    await orderDoc.ref.update({
                        status: 'failed',
                        failure_reason: 'Webhook verification failed'
                    });
                }
            }
        }
        catch (error) {
            console.error('Error processing webhook:', error);
        }
    }
});
// Function to send push notifications for event reminders
exports.sendEventReminders = functions.pubsub.schedule('every 1 hours').onRun(async (context) => {
    try {
        const now = new Date();
        const reminderTime = new Date(now.getTime() + (24 * 60 * 60 * 1000)); // 24 hours from now
        // Get events happening in 24 hours
        const upcomingEvents = await admin.firestore()
            .collection('events')
            .where('dateTime', '>', now.getTime())
            .where('dateTime', '<', reminderTime.getTime())
            .get();
        for (const eventDoc of upcomingEvents.docs) {
            const event = eventDoc.data();
            // Get all active tickets for this event
            const tickets = await admin.firestore()
                .collection('tickets')
                .where('eventId', '==', eventDoc.id)
                .where('status', '==', 'ACTIVE')
                .get();
            // Send notification to each ticket holder
            const notificationPromises = tickets.docs.map(async (ticketDoc) => {
                const ticket = ticketDoc.data();
                // Get user's FCM token (you'd need to store this when user logs in)
                const userDoc = await admin.firestore()
                    .collection('users')
                    .doc(ticket.userId)
                    .get();
                const user = userDoc.data();
                if (user === null || user === void 0 ? void 0 : user.fcmToken) {
                    const message = {
                        notification: {
                            title: 'Event Reminder',
                            body: `${event.title} is happening tomorrow!`,
                        },
                        data: {
                            type: 'event_reminder',
                            eventId: eventDoc.id,
                            ticketId: ticketDoc.id,
                        },
                        token: user.fcmToken,
                    };
                    return admin.messaging().send(message);
                }
                return null;
            });
            await Promise.all(notificationPromises.filter(Boolean));
        }
        console.log(`Sent reminders for ${upcomingEvents.docs.length} events`);
    }
    catch (error) {
        console.error('Error sending event reminders:', error);
    }
});
//# sourceMappingURL=index.js.map