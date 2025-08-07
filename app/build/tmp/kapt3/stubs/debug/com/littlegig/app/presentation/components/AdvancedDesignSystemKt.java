package com.littlegig.app.presentation.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000j\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\u001aN\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0011\u0010\b\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\tH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\n\u0010\u000b\u001aN\u0010\f\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0011\u0010\b\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\tH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000b\u001a3\u0010\u000e\u001a\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0011\u0010\u000f\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\tH\u0007\u001a=\u0010\u0010\u001a\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\u0011\u0010\b\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\tH\u0007\u001a&\u0010\u0013\u001a\u00020\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00010\u0017H\u0007\u001a&\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00122\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a%\u0010\u001c\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0011\u0010\b\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\tH\u0007\u001a.\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u00122\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00010\u00172\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a0\u0010 \u001a\u00020\u00012\b\u0010!\u001a\u0004\u0018\u00010\"2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00010\u00172\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a4\u0010$\u001a\u00020\u00012\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00010\u00172\b\u0010!\u001a\u0004\u0018\u00010\"H\u0003\u001aP\u0010&\u001a\u00020\u00012\u0006\u0010\'\u001a\u00020\u00152\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00010\u00172\f\u0010)\u001a\b\u0012\u0004\u0012\u00020+0*2\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020\u00010\u00172\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u001a\u0010-\u001a\u00020\u00012\u0006\u0010.\u001a\u00020/2\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0012\u00100\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a$\u00101\u001a\u00020\u0001*\u0002022\u0006\u00103\u001a\u00020\u00122\u0006\u00104\u001a\u00020\u00122\u0006\u0010\u0006\u001a\u000205H\u0002\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u00066"}, d2 = {"AdvancedGlassmorphicCard", "", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "Lkotlin/Function0;", "cornerRadius", "Landroidx/compose/ui/unit/Dp;", "content", "Landroidx/compose/runtime/Composable;", "AdvancedGlassmorphicCard-d8LSEHM", "(Landroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;FLkotlin/jvm/functions/Function0;)V", "AdvancedNeumorphicCard", "AdvancedNeumorphicCard-d8LSEHM", "FloatingActionButton", "icon", "HapticButton", "enabled", "", "LiquidGlassBottomNavigation", "currentRoute", "", "onNavigate", "Lkotlin/Function1;", "LiquidGlassNavItem", "item", "Lcom/littlegig/app/presentation/components/BottomNavItem;", "isSelected", "LoadingPulseAnimation", "NeumorphicActiveNowToggle", "isActive", "onToggle", "NeumorphicDatePicker", "selectedDate", "Ljava/util/Date;", "onDateSelected", "NeumorphicDateTimePickerDialog", "onDismiss", "NeumorphicPlacesAutocomplete", "query", "onQueryChange", "suggestions", "", "Lcom/littlegig/app/presentation/components/PlaceSuggestion;", "onPlaceSelected", "NeumorphicRankBadge", "rank", "Lcom/littlegig/app/data/model/UserRank;", "ShimmerLoadingCard", "drawAdvancedNeumorphicShadow", "Landroidx/compose/ui/graphics/drawscope/DrawScope;", "isDark", "pressed", "", "app_debug"})
public final class AdvancedDesignSystemKt {
    
    @androidx.compose.runtime.Composable
    public static final void HapticButton(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void LoadingPulseAnimation(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ShimmerLoadingCard(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void FloatingActionButton(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> icon) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void LiquidGlassBottomNavigation(@org.jetbrains.annotations.Nullable
    java.lang.String currentRoute, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigate) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void LiquidGlassNavItem(@org.jetbrains.annotations.NotNull
    com.littlegig.app.presentation.components.BottomNavItem item, boolean isSelected, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final void drawAdvancedNeumorphicShadow(androidx.compose.ui.graphics.drawscope.DrawScope $this$drawAdvancedNeumorphicShadow, boolean isDark, boolean pressed, float cornerRadius) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void NeumorphicDatePicker(@org.jetbrains.annotations.Nullable
    java.util.Date selectedDate, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.util.Date, kotlin.Unit> onDateSelected, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    private static final void NeumorphicDateTimePickerDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.util.Date, kotlin.Unit> onDateSelected, java.util.Date selectedDate) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void NeumorphicPlacesAutocomplete(@org.jetbrains.annotations.NotNull
    java.lang.String query, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, @org.jetbrains.annotations.NotNull
    java.util.List<com.littlegig.app.presentation.components.PlaceSuggestion> suggestions, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.littlegig.app.presentation.components.PlaceSuggestion, kotlin.Unit> onPlaceSelected, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void NeumorphicRankBadge(@org.jetbrains.annotations.NotNull
    com.littlegig.app.data.model.UserRank rank, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void NeumorphicActiveNowToggle(boolean isActive, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
}