package com.littlegig.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BG\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\nH\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\fH\u00c6\u0003JK\u0010\u001b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\u0013\u0010\u001e\u001a\u00020\n2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u00d6\u0003J\t\u0010!\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\"\u001a\u00020\fH\u00d6\u0001J\u0019\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u001dH\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0012R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014\u00a8\u0006("}, d2 = {"Lcom/littlegig/app/data/model/ChatState;", "Landroid/os/Parcelable;", "currentChat", "Lcom/littlegig/app/data/model/Chat;", "messages", "", "Lcom/littlegig/app/data/model/Message;", "typingUsers", "Lcom/littlegig/app/data/model/TypingIndicator;", "isLoading", "", "error", "", "(Lcom/littlegig/app/data/model/Chat;Ljava/util/List;Ljava/util/List;ZLjava/lang/String;)V", "getCurrentChat", "()Lcom/littlegig/app/data/model/Chat;", "getError", "()Ljava/lang/String;", "()Z", "getMessages", "()Ljava/util/List;", "getTypingUsers", "component1", "component2", "component3", "component4", "component5", "copy", "describeContents", "", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
@kotlinx.parcelize.Parcelize
public final class ChatState implements android.os.Parcelable {
    @org.jetbrains.annotations.Nullable
    private final com.littlegig.app.data.model.Chat currentChat = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.littlegig.app.data.model.Message> messages = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.littlegig.app.data.model.TypingIndicator> typingUsers = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String error = null;
    
    public ChatState(@org.jetbrains.annotations.Nullable
    com.littlegig.app.data.model.Chat currentChat, @org.jetbrains.annotations.NotNull
    java.util.List<com.littlegig.app.data.model.Message> messages, @org.jetbrains.annotations.NotNull
    java.util.List<com.littlegig.app.data.model.TypingIndicator> typingUsers, boolean isLoading, @org.jetbrains.annotations.Nullable
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.littlegig.app.data.model.Chat getCurrentChat() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.littlegig.app.data.model.Message> getMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.littlegig.app.data.model.TypingIndicator> getTypingUsers() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getError() {
        return null;
    }
    
    public ChatState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.littlegig.app.data.model.Chat component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.littlegig.app.data.model.Message> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.littlegig.app.data.model.TypingIndicator> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.littlegig.app.data.model.ChatState copy(@org.jetbrains.annotations.Nullable
    com.littlegig.app.data.model.Chat currentChat, @org.jetbrains.annotations.NotNull
    java.util.List<com.littlegig.app.data.model.Message> messages, @org.jetbrains.annotations.NotNull
    java.util.List<com.littlegig.app.data.model.TypingIndicator> typingUsers, boolean isLoading, @org.jetbrains.annotations.Nullable
    java.lang.String error) {
        return null;
    }
    
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override
    public void writeToParcel(@org.jetbrains.annotations.NotNull
    android.os.Parcel parcel, int flags) {
    }
}