package com.littlegig.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b+\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00b9\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\u0002\u0010\u0019J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\t\u00101\u001a\u00020\u0010H\u00c6\u0003J\t\u00102\u001a\u00020\u0010H\u00c6\u0003J\u000f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\t\u00105\u001a\u00020\u0015H\u00c6\u0003J\t\u00106\u001a\u00020\u0015H\u00c6\u0003J\t\u00107\u001a\u00020\u0018H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\t\u0010=\u001a\u00020\u000bH\u00c6\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010?\u001a\u00020\u000eH\u00c6\u0003J\u00bd\u0001\u0010@\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u00c6\u0001J\t\u0010A\u001a\u00020\u0010H\u00d6\u0001J\u0013\u0010B\u001a\u00020\u00182\b\u0010C\u001a\u0004\u0018\u00010DH\u00d6\u0003J\t\u0010E\u001a\u00020\u0010H\u00d6\u0001J\t\u0010F\u001a\u00020\u0003H\u00d6\u0001J\u0019\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020\u0010H\u00d6\u0001R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0016\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001bR\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010!R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010#R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001bR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001bR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010#R\u0011\u0010\u0011\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010%\u00a8\u0006L"}, d2 = {"Lcom/littlegig/app/data/model/Recap;", "Landroid/os/Parcelable;", "id", "", "eventId", "userId", "userName", "userImageUrl", "mediaUrls", "", "recapType", "Lcom/littlegig/app/data/model/RecapType;", "caption", "location", "Lcom/littlegig/app/data/model/Location;", "likes", "", "views", "likedBy", "viewedBy", "createdAt", "", "duration", "isActive", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lcom/littlegig/app/data/model/RecapType;Ljava/lang/String;Lcom/littlegig/app/data/model/Location;IILjava/util/List;Ljava/util/List;JJZ)V", "getCaption", "()Ljava/lang/String;", "getCreatedAt", "()J", "getDuration", "getEventId", "getId", "()Z", "getLikedBy", "()Ljava/util/List;", "getLikes", "()I", "getLocation", "()Lcom/littlegig/app/data/model/Location;", "getMediaUrls", "getRecapType", "()Lcom/littlegig/app/data/model/RecapType;", "getUserId", "getUserImageUrl", "getUserName", "getViewedBy", "getViews", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "describeContents", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
@kotlinx.parcelize.Parcelize()
public final class Recap implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eventId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userImageUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> mediaUrls = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.model.RecapType recapType = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String caption = null;
    @org.jetbrains.annotations.NotNull()
    private final com.littlegig.app.data.model.Location location = null;
    private final int likes = 0;
    private final int views = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> likedBy = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> viewedBy = null;
    private final long createdAt = 0L;
    private final long duration = 0L;
    private final boolean isActive = false;
    
    public Recap(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String eventId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String userName, @org.jetbrains.annotations.NotNull()
    java.lang.String userImageUrl, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> mediaUrls, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.RecapType recapType, @org.jetbrains.annotations.Nullable()
    java.lang.String caption, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.Location location, int likes, int views, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> likedBy, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> viewedBy, long createdAt, long duration, boolean isActive) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEventId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserImageUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getMediaUrls() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.RecapType getRecapType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCaption() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.Location getLocation() {
        return null;
    }
    
    public final int getLikes() {
        return 0;
    }
    
    public final int getViews() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getLikedBy() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getViewedBy() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long getDuration() {
        return 0L;
    }
    
    public final boolean isActive() {
        return false;
    }
    
    public Recap() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component13() {
        return null;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final long component15() {
        return 0L;
    }
    
    public final boolean component16() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
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
    public final java.util.List<java.lang.String> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.RecapType component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.Location component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.littlegig.app.data.model.Recap copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String eventId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String userName, @org.jetbrains.annotations.NotNull()
    java.lang.String userImageUrl, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> mediaUrls, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.RecapType recapType, @org.jetbrains.annotations.Nullable()
    java.lang.String caption, @org.jetbrains.annotations.NotNull()
    com.littlegig.app.data.model.Location location, int likes, int views, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> likedBy, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> viewedBy, long createdAt, long duration, boolean isActive) {
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