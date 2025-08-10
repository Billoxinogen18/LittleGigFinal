package com.littlegig.app.presentation.chat;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020\u000eJ\u0006\u00102\u001a\u000200J\u0006\u00103\u001a\u000200J\u000e\u00104\u001a\u0002002\u0006\u00105\u001a\u00020\u000eJ\u000e\u00106\u001a\u0002002\u0006\u00105\u001a\u00020\u000eJ\u0010\u00107\u001a\u0002002\b\b\u0002\u00108\u001a\u000209J\u0006\u0010:\u001a\u000200J\u0014\u0010;\u001a\u0002002\f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bJ\u0010\u0010=\u001a\u0002002\b\b\u0002\u00108\u001a\u000209J\u0010\u0010>\u001a\u0002002\b\b\u0002\u00108\u001a\u000209J\u000e\u0010?\u001a\u0002002\u0006\u0010@\u001a\u00020\u000eJ\u0016\u0010A\u001a\u0002002\u0006\u00101\u001a\u00020\u000e2\u0006\u0010B\u001a\u00020\u000eJ\u000e\u0010C\u001a\u0002002\u0006\u0010D\u001a\u00020\u000eJ\u0006\u0010E\u001a\u000200J\u000e\u0010F\u001a\u0002002\u0006\u0010@\u001a\u00020\u000eJ\u000e\u0010G\u001a\u0002002\u0006\u0010D\u001a\u00020\u000eR\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u001d\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0019R\u000e\u0010 \u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010&\u001a\u0004\u0018\u00010\'X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010(R\u001d\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u000b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0019R\u001d\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0019R\u0017\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00150\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0019R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006H"}, d2 = {"Lcom/littlegig/app/presentation/chat/ChatViewModel;", "Landroidx/lifecycle/ViewModel;", "chatRepository", "Lcom/littlegig/app/data/repository/ChatRepository;", "userRepository", "Lcom/littlegig/app/data/repository/UserRepository;", "authRepository", "Lcom/littlegig/app/data/repository/AuthRepository;", "(Lcom/littlegig/app/data/repository/ChatRepository;Lcom/littlegig/app/data/repository/UserRepository;Lcom/littlegig/app/data/repository/AuthRepository;)V", "_allUsers", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/littlegig/app/data/model/User;", "_chatSearchQuery", "", "_chats", "Lcom/littlegig/app/data/model/Chat;", "_contactsUsers", "_pinnedChatIds", "_searchResults", "_uiState", "Lcom/littlegig/app/presentation/chat/ChatUiState;", "allUsers", "Lkotlinx/coroutines/flow/StateFlow;", "getAllUsers", "()Lkotlinx/coroutines/flow/StateFlow;", "chatSearchQuery", "getChatSearchQuery", "chats", "getChats", "contactsUsers", "getContactsUsers", "hasMoreAllUsers", "", "hasMoreChats", "isLoadingChatsPage", "isLoadingUsersPage", "lastAllUsersId", "lastChatsTime", "", "Ljava/lang/Long;", "pinnedChatIds", "getPinnedChatIds", "searchResults", "getSearchResults", "uiState", "getUiState", "blockUser", "", "targetUserId", "clearError", "clearSearchResults", "createChat", "userId", "createChatWithUser", "loadAllUsers", "limit", "", "loadChats", "loadContacts", "contactsPhones", "loadMoreAllUsers", "loadMoreChats", "pinChat", "chatId", "reportUser", "reason", "searchUsers", "query", "startNewChat", "unpinChat", "updateChatSearchQuery", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ChatViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.ChatRepository chatRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.littlegig.app.presentation.chat.ChatUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.chat.ChatUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.littlegig.app.data.model.Chat>> _chats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.Chat>> chats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.littlegig.app.data.model.User>> _searchResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> searchResults = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.littlegig.app.data.model.User>> _contactsUsers = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> contactsUsers = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.littlegig.app.data.model.User>> _allUsers = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> allUsers = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastAllUsersId;
    private boolean hasMoreAllUsers = true;
    private boolean isLoadingUsersPage = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Long lastChatsTime;
    private boolean hasMoreChats = true;
    private boolean isLoadingChatsPage = false;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _chatSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> chatSearchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.lang.String>> _pinnedChatIds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> pinnedChatIds = null;
    
    @javax.inject.Inject()
    public ChatViewModel(@org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.ChatRepository chatRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.UserRepository userRepository, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.repository.AuthRepository authRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.littlegig.app.presentation.chat.ChatUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.Chat>> getChats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> getSearchResults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> getContactsUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.littlegig.app.data.model.User>> getAllUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getChatSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getPinnedChatIds() {
        return null;
    }
    
    public final void loadChats() {
    }
    
    public final void updateChatSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void pinChat(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
    }
    
    public final void unpinChat(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
    }
    
    public final void searchUsers(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void loadContacts(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> contactsPhones) {
    }
    
    public final void loadAllUsers(int limit) {
    }
    
    public final void loadMoreAllUsers(int limit) {
    }
    
    public final void createChat(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    public final void loadMoreChats(int limit) {
    }
    
    public final void blockUser(@org.jetbrains.annotations.NotNull()
    java.lang.String targetUserId) {
    }
    
    public final void reportUser(@org.jetbrains.annotations.NotNull()
    java.lang.String targetUserId, @org.jetbrains.annotations.NotNull()
    java.lang.String reason) {
    }
    
    public final void clearError() {
    }
    
    public final void clearSearchResults() {
    }
    
    public final void startNewChat() {
    }
    
    public final void createChatWithUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
}