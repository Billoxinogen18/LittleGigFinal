package com.littlegig.app.presentation.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\u001aJ\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001aX\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001a(\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\n2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001a \u0010\u0015\u001a\u00020\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001a\u0010\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002\u00a8\u0006\u001c"}, d2 = {"NeumorphicChatBubble", "", "message", "Lcom/littlegig/app/data/model/Message;", "isFromCurrentUser", "", "onLikeMessage", "Lkotlin/Function1;", "", "onShareTicket", "Lcom/littlegig/app/data/model/SharedTicket;", "modifier", "Landroidx/compose/ui/Modifier;", "NeumorphicChatInput", "onMessageChange", "onSendMessage", "Lkotlin/Function0;", "onAttachMedia", "NeumorphicTicketShareCard", "ticket", "onRedeem", "NeumorphicTypingIndicator", "typingUsers", "", "Lcom/littlegig/app/data/model/TypingIndicator;", "formatMessageTime", "timestamp", "", "app_debug"})
public final class ChatComponentsKt {
    
    @androidx.compose.runtime.Composable()
    public static final void NeumorphicChatBubble(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.Message message, boolean isFromCurrentUser, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onLikeMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.littlegig.app.data.model.SharedTicket, kotlin.Unit> onShareTicket, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void NeumorphicTicketShareCard(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.SharedTicket ticket, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRedeem, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void NeumorphicTypingIndicator(@org.jetbrains.annotations.NotNull()
    java.util.List<com.littlegig.app.data.model.TypingIndicator> typingUsers, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void NeumorphicChatInput(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onMessageChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSendMessage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAttachMedia, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onShareTicket, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    private static final java.lang.String formatMessageTime(long timestamp) {
        return null;
    }
}