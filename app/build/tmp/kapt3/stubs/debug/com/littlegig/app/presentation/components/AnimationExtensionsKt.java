package com.littlegig.app.presentation.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u001a\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u0005H\u0007\u001a\u0014\u0010\t\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0001\u001a(\u0010\f\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u00012\b\b\u0002\u0010\u000e\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u001a\u001e\u0010\u000f\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u001a\u001e\u0010\u0011\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u001a\u0014\u0010\u0013\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u001a\u001e\u0010\u0014\u001a\u00020\n*\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a8\u0006\u0016"}, d2 = {"rememberSmoothTransition", "", "targetState", "", "duration", "", "rememberStaggeredAnimationDelay", "index", "baseDelay", "bounceClick", "Landroidx/compose/ui/Modifier;", "scaleDown", "breathingEffect", "minScale", "maxScale", "floatingEffect", "strength", "gentleRotation", "degrees", "shimmerEffect", "slideInAnimation", "delay", "app_debug"})
public final class AnimationExtensionsKt {
    
    /**
     * Adds a smooth bounce animation when the component is tapped
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier bounceClick(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$bounceClick, float scaleDown) {
        return null;
    }
    
    /**
     * Adds a floating animation effect
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier floatingEffect(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$floatingEffect, float strength, int duration) {
        return null;
    }
    
    /**
     * Adds a subtle rotation animation
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier gentleRotation(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$gentleRotation, float degrees, int duration) {
        return null;
    }
    
    /**
     * Adds a breathing/pulsing animation
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier breathingEffect(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$breathingEffect, float minScale, float maxScale, int duration) {
        return null;
    }
    
    /**
     * Adds a shimmer loading effect
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier shimmerEffect(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$shimmerEffect, int duration) {
        return null;
    }
    
    /**
     * Adds a delayed entrance animation
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.compose.ui.Modifier slideInAnimation(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier $this$slideInAnimation, int delay, int duration) {
        return null;
    }
    
    /**
     * Creates a staggered animation delay based on index
     */
    @androidx.compose.runtime.Composable
    public static final int rememberStaggeredAnimationDelay(int index, int baseDelay) {
        return 0;
    }
    
    /**
     * Creates a smooth transition between states
     */
    @androidx.compose.runtime.Composable
    public static final float rememberSmoothTransition(boolean targetState, int duration) {
        return 0.0F;
    }
}