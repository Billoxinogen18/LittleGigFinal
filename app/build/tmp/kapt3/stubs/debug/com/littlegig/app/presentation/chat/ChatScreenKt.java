package com.littlegig.app.presentation.chat;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u001a\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u001a(\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\u0010"}, d2 = {"ChatCard", "", "chat", "Lcom/littlegig/app/presentation/chat/ChatPreview;", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "ChatScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/littlegig/app/presentation/chat/ChatViewModel;", "UserSearchCard", "user", "Lcom/littlegig/app/presentation/chat/UserSearchResult;", "app_debug"})
public final class ChatScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void ChatScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull
    com.littlegig.app.presentation.chat.ChatViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void UserSearchCard(@org.jetbrains.annotations.NotNull
    com.littlegig.app.presentation.chat.UserSearchResult user, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void ChatCard(@org.jetbrains.annotations.NotNull
    com.littlegig.app.presentation.chat.ChatPreview chat, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
}