package com.littlegig.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\n\u0010\u000bJ\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00110\u00102\u0006\u0010\u0012\u001a\u00020\tJ*\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0014\u0010\u000bJ*\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00062\u0006\u0010\u0017\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0018\u0010\u000bJ\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00110\u00102\u0006\u0010\u001a\u001a\u00020\tJ*\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001c\u0010\u001dJ*\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001f\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/littlegig/app/data/repository/TicketRepository;", "", "firestore", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "cancelTicket", "Lkotlin/Result;", "", "ticketId", "", "cancelTicket-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generateQRCode", "ticket", "Lcom/littlegig/app/data/model/Ticket;", "getEventTickets", "Lkotlinx/coroutines/flow/Flow;", "", "eventId", "getTicketById", "getTicketById-gIAlu-s", "getTotalRevenue", "", "organizerId", "getTotalRevenue-gIAlu-s", "getUserTickets", "userId", "purchaseTicket", "purchaseTicket-gIAlu-s", "(Lcom/littlegig/app/data/model/Ticket;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "useTicket", "useTicket-gIAlu-s", "app_debug"})
public final class TicketRepository {
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.firestore.FirebaseFirestore firestore = null;
    
    @javax.inject.Inject
    public TicketRepository(@org.jetbrains.annotations.NotNull
    com.google.firebase.firestore.FirebaseFirestore firestore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Ticket>> getUserTickets(@org.jetbrains.annotations.NotNull
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.littlegig.app.data.model.Ticket>> getEventTickets(@org.jetbrains.annotations.NotNull
    java.lang.String eventId) {
        return null;
    }
    
    private final java.lang.String generateQRCode(com.littlegig.app.data.model.Ticket ticket) {
        return null;
    }
}