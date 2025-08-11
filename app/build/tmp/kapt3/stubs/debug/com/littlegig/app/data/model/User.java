package com.littlegig.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\bJ\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00cf\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0019\u0012\u000f\b\u0002\u0010\u001a\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u0012\r\b\u0002\u0010\u001e\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c\u0012\r\b\u0002\u0010\u001f\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u0003\u0012\u000f\b\u0002\u0010#\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c\u0012\b\b\u0002\u0010$\u001a\u00020\u000e\u00a2\u0006\u0002\u0010%J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010J\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003\u00a2\u0006\u0002\u00106J\t\u0010K\u001a\u00020\u000eH\u00c6\u0003J\u000b\u0010L\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010M\u001a\u00020\u0012H\u00c6\u0003J\u000f\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\u000f\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\u000f\u0010P\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\u000f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\t\u0010R\u001a\u00020\u0019H\u00c6\u0003J\u0010\u0010S\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001cH\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000e\u0010V\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001cH\u00c6\u0003J\u000e\u0010W\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001cH\u00c6\u0003J\u000b\u0010X\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010Y\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010Z\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010[\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001cH\u00c6\u0003J\t\u0010\\\u001a\u00020\u000eH\u00c6\u0003J\t\u0010]\u001a\u00020\u0003H\u00c6\u0003J\t\u0010^\u001a\u00020\u0003H\u00c6\u0003J\t\u0010_\u001a\u00020\u0003H\u00c6\u0003J\t\u0010`\u001a\u00020\u0003H\u00c6\u0003J\t\u0010a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010c\u001a\u00020\fH\u00c6\u0003J\u00d8\u0002\u0010d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u000f\b\u0002\u0010\u001a\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c2\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00032\r\b\u0002\u0010\u001e\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c2\r\b\u0002\u0010\u001f\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u00032\u000f\b\u0002\u0010#\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c2\b\b\u0002\u0010$\u001a\u00020\u000eH\u00c6\u0001\u00a2\u0006\u0002\u0010eJ\t\u0010f\u001a\u00020gH\u00d6\u0001J\u0013\u0010h\u001a\u00020\u000e2\b\u0010i\u001a\u0004\u0018\u00010\u001bH\u00d6\u0003J\t\u0010j\u001a\u00020gH\u00d6\u0001J\t\u0010k\u001a\u00020\u0003H\u00d6\u0001J\u0019\u0010l\u001a\u00020m2\u0006\u0010n\u001a\u00020o2\u0006\u0010p\u001a\u00020gH\u00d6\u0001R\u0013\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\'R\u0016\u0010\u001e\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\'R\u0013\u0010\"\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\'R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\'R\u0013\u0010!\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\'R\u0011\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00102R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\'R\u0015\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\n\n\u0002\u00107\u001a\u0004\b5\u00106R\u0011\u0010\u000f\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u00108R\u0018\u0010\u001a\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010*R\u0018\u0010#\u001a\t\u0018\u00010\u001b\u00a2\u0006\u0002\b\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010*R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u00102R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\'R\u0011\u0010$\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u00108R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010\'R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u00102R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\'R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010\'R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010CR\u0016\u0010\u001f\u001a\u00070\u001b\u00a2\u0006\u0002\b\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010*R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010FR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010\'R\u0013\u0010 \u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010\'\u00a8\u0006q"}, d2 = {"Lcom/littlegig/app/data/model/User;", "Landroid/os/Parcelable;", "id", "", "email", "name", "displayName", "username", "phoneNumber", "profilePictureUrl", "profileImageUrl", "userType", "Lcom/littlegig/app/data/model/UserType;", "influencerLegacy", "", "isInfluencer", "businessId", "rank", "Lcom/littlegig/app/data/model/UserRank;", "followers", "", "following", "pinnedChats", "likedEvents", "engagementScore", "", "lastRankUpdate", "", "Lkotlinx/parcelize/RawValue;", "bio", "createdAt", "updatedAt", "username_lower", "email_lower", "displayName_lower", "lastSeen", "online", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/littlegig/app/data/model/UserType;Ljava/lang/Boolean;ZLjava/lang/String;Lcom/littlegig/app/data/model/UserRank;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;DLjava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Z)V", "getBio", "()Ljava/lang/String;", "getBusinessId", "getCreatedAt", "()Ljava/lang/Object;", "getDisplayName", "getDisplayName_lower", "getEmail", "getEmail_lower", "getEngagementScore", "()D", "getFollowers", "()Ljava/util/List;", "getFollowing", "getId", "getInfluencerLegacy", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "()Z", "getLastRankUpdate", "getLastSeen", "getLikedEvents", "getName", "getOnline", "getPhoneNumber", "getPinnedChats", "getProfileImageUrl", "getProfilePictureUrl", "getRank", "()Lcom/littlegig/app/data/model/UserRank;", "getUpdatedAt", "getUserType", "()Lcom/littlegig/app/data/model/UserType;", "getUsername", "getUsername_lower", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/littlegig/app/data/model/UserType;Ljava/lang/Boolean;ZLjava/lang/String;Lcom/littlegig/app/data/model/UserRank;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;DLjava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Z)Lcom/littlegig/app/data/model/User;", "describeContents", "", "equals", "other", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
@kotlinx.parcelize.Parcelize()
public final class User implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String email = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String username = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneNumber = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String profilePictureUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String profileImageUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.model.UserType userType = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean influencerLegacy = null;
    private final boolean isInfluencer = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String businessId = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.model.UserRank rank = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> followers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> following = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> pinnedChats = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> likedEvents = null;
    private final double engagementScore = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Object lastRankUpdate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String bio = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object createdAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object updatedAt = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String username_lower = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String email_lower = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String displayName_lower = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Object lastSeen = null;
    private final boolean online = false;
    
    public User(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String profilePictureUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String profileImageUrl, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean influencerLegacy, boolean isInfluencer, @org.jetbrains.annotations.Nullable()
    java.lang.String businessId, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserRank rank, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> followers, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> following, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> pinnedChats, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> likedEvents, double engagementScore, @org.jetbrains.annotations.Nullable()
    java.lang.Object lastRankUpdate, @org.jetbrains.annotations.Nullable()
    java.lang.String bio, @org.jetbrains.annotations.NotNull()
    java.lang.Object createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.Object updatedAt, @org.jetbrains.annotations.Nullable()
    java.lang.String username_lower, @org.jetbrains.annotations.Nullable()
    java.lang.String email_lower, @org.jetbrains.annotations.Nullable()
    java.lang.String displayName_lower, @org.jetbrains.annotations.Nullable()
    java.lang.Object lastSeen, boolean online) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProfilePictureUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProfileImageUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.UserType getUserType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean getInfluencerLegacy() {
        return null;
    }
    
    public final boolean isInfluencer() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBusinessId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.UserRank getRank() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getFollowers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getFollowing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getPinnedChats() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getLikedEvents() {
        return null;
    }
    
    public final double getEngagementScore() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLastRankUpdate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBio() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.Object getCreatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.Object getUpdatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUsername_lower() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEmail_lower() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDisplayName_lower() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLastSeen() {
        return null;
    }
    
    public final boolean getOnline() {
        return false;
    }
    
    public User() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.UserRank component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component17() {
        return null;
    }
    
    public final double component18() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.Object component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.Object component22() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component23() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component24() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component25() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object component26() {
        return null;
    }
    
    public final boolean component27() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.UserType component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.User copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String displayName, @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String profilePictureUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String profileImageUrl, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserType userType, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean influencerLegacy, boolean isInfluencer, @org.jetbrains.annotations.Nullable()
    java.lang.String businessId, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.UserRank rank, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> followers, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> following, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> pinnedChats, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> likedEvents, double engagementScore, @org.jetbrains.annotations.Nullable()
    java.lang.Object lastRankUpdate, @org.jetbrains.annotations.Nullable()
    java.lang.String bio, @org.jetbrains.annotations.NotNull()
    java.lang.Object createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.Object updatedAt, @org.jetbrains.annotations.Nullable()
    java.lang.String username_lower, @org.jetbrains.annotations.Nullable()
    java.lang.String email_lower, @org.jetbrains.annotations.Nullable()
    java.lang.String displayName_lower, @org.jetbrains.annotations.Nullable()
    java.lang.Object lastSeen, boolean online) {
        return null;
    }
    
    @java.lang.Override()
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel parcel, int flags) {
    }
}