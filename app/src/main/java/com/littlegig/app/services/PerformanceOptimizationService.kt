package com.littlegig.app.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.Semaphore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceOptimizationService @Inject constructor(
    private val context: Context,
    private val imageLoader: ImageLoader
) {

    companion object {
        private const val MEMORY_CACHE_SIZE = 50 * 1024 * 1024 // 50MB
        private const val DISK_CACHE_SIZE = 100 * 1024 * 1024 // 100MB
        private const val MAX_CONCURRENT_OPERATIONS = 4
        private const val IMAGE_COMPRESSION_QUALITY = 85
        private const val MAX_IMAGE_DIMENSION = 2048
    }

    // Memory cache for images
    private val imageCache = LruCache<String, Bitmap>(MEMORY_CACHE_SIZE / 8)
    
    // Disk cache directory
    private val cacheDir = File(context.cacheDir, "image_cache")
    
    // Background operations
    private val optimizationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val operationSemaphore = Semaphore(MAX_CONCURRENT_OPERATIONS)
    
    // Performance metrics
    private val cacheHits = AtomicInteger(0)
    private val cacheMisses = AtomicInteger(0)
    private val totalOperations = AtomicInteger(0)

    init {
        initializeCache()
    }

    // Optimized image loading with caching
    suspend fun loadOptimizedImage(
        url: String,
        width: Int = 0,
        height: Int = 0,
        placeholder: Bitmap? = null
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            operationSemaphore.acquire()
            totalOperations.incrementAndGet()
            
            // Check memory cache first
            val cachedBitmap = imageCache.get(generateCacheKey(url, width, height))
            if (cachedBitmap != null) {
                cacheHits.incrementAndGet()
                return@withContext cachedBitmap
            }
            
            cacheMisses.incrementAndGet()
            
            // Check disk cache
            val diskCachedBitmap = loadFromDiskCache(url, width, height)
            if (diskCachedBitmap != null) {
                imageCache.put(generateCacheKey(url, width, height), diskCachedBitmap)
                return@withContext diskCachedBitmap
            }
            
            // Load from network with optimization
            val optimizedBitmap = loadAndOptimizeImage(url, width, height, placeholder?.let { BitmapDrawable(context.resources, it) })
            if (optimizedBitmap != null) {
                // Cache the optimized image
                imageCache.put(generateCacheKey(url, width, height), optimizedBitmap)
                saveToDiskCache(url, width, height, optimizedBitmap)
            }
            
            optimizedBitmap
        } finally {
            operationSemaphore.release()
        }
    }

    // Batch image preloading for better UX
    suspend fun preloadImages(urls: List<String>, priority: PreloadPriority = PreloadPriority.NORMAL) {
        optimizationScope.launch {
            urls.forEach { url ->
                try {
                    loadOptimizedImage(url)
                    delay(100) // Small delay to avoid overwhelming the system
                } catch (e: Exception) {
                    // Log error but continue with other images
                }
            }
        }
    }

    // Memory management and cleanup
    fun optimizeMemoryUsage() {
        optimizationScope.launch {
            // Clear least recently used items from memory cache
            imageCache.trimToSize(imageCache.size() / 2)
            
            // Clear expired disk cache files
            cleanupExpiredDiskCache()
            
            // Force garbage collection if memory pressure is high
            if (isMemoryPressureHigh()) {
                System.gc()
            }
        }
    }

    // Image compression and optimization
    suspend fun compressImage(
        bitmap: Bitmap,
        quality: Int = IMAGE_COMPRESSION_QUALITY,
        maxDimension: Int = MAX_IMAGE_DIMENSION
    ): Bitmap = withContext(Dispatchers.Default) {
        val compressedBitmap = if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
            val scaleFactor = minOf(
                maxDimension.toFloat() / bitmap.width,
                maxDimension.toFloat() / bitmap.height
            )
            
            val newWidth = (bitmap.width * scaleFactor).toInt()
            val newHeight = (bitmap.height * scaleFactor).toInt()
            
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }
        
        // Apply additional optimizations
        if (compressedBitmap.config != Bitmap.Config.RGB_565) {
            val optimizedBitmap = compressedBitmap.copy(Bitmap.Config.RGB_565, false)
            if (compressedBitmap != bitmap) compressedBitmap.recycle()
            optimizedBitmap
        } else {
            compressedBitmap
        }
    }

    // Lazy loading for lists and recycler views
    fun createLazyImageLoader(
        visibleRange: IntRange,
        preloadDistance: Int = 5
    ): LazyImageLoader {
        return LazyImageLoader(
            visibleRange = visibleRange,
            preloadDistance = preloadDistance,
            imageLoader = this
        )
    }

    // Performance monitoring
    fun getPerformanceMetrics(): PerformanceMetrics {
        return PerformanceMetrics(
            cacheHitRate = cacheHits.get().toDouble() / totalOperations.get(),
            memoryUsage = imageCache.size(),
            diskCacheSize = getDiskCacheSize(),
            totalOperations = totalOperations.get()
        )
    }

    // Private helper functions
    private fun initializeCache() {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    private fun generateCacheKey(url: String, width: Int, height: Int): String {
        val key = "${url}_${width}x${height}"
        return MessageDigest.getInstance("MD5").digest(key.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    private suspend fun loadAndOptimizeImage(
        url: String,
        width: Int,
        height: Int,
        placeholder: Drawable?
    ): Bitmap? {
        return try {
            val request = ImageRequest.Builder(context)
            .data(url)
            .size(width, height)
            .apply {
                if (placeholder != null) {
                    this.placeholder(placeholder)
                }
            }
            .build()
            
            val result = imageLoader.execute(request)
            if (result is SuccessResult) {
                val drawable = result.drawable
                val bitmapFromDrawable = (drawable as? BitmapDrawable)?.bitmap
                    ?: drawableToBitmap(drawable)
                val compressedBitmap = compressImage(bitmapFromDrawable)
                return compressedBitmap
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private suspend fun loadFromDiskCache(url: String, width: Int, height: Int): Bitmap? {
        return try {
            val cacheFile = File(cacheDir, generateCacheKey(url, width, height))
            if (cacheFile.exists() && !isCacheFileExpired(cacheFile)) {
                BitmapFactory.decodeFile(cacheFile.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveToDiskCache(url: String, width: Int, height: Int, bitmap: Bitmap) {
        try {
            val cacheFile = File(cacheDir, generateCacheKey(url, width, height))
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESSION_QUALITY, out)
            }
        } catch (e: Exception) {
            // Log error but don't fail
        }
    }

    private fun isCacheFileExpired(file: File): Boolean {
        val maxAge = 7 * 24 * 60 * 60 * 1000L // 7 days
        return System.currentTimeMillis() - file.lastModified() > maxAge
    }

    private suspend fun cleanupExpiredDiskCache() {
        val files = cacheDir.listFiles() ?: return
        val maxAge = 7 * 24 * 60 * 60 * 1000L // 7 days
        
        files.forEach { file ->
            if (System.currentTimeMillis() - file.lastModified() > maxAge) {
                file.delete()
            }
        }
    }

    private fun isMemoryPressureHigh(): Boolean {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        return usedMemory > maxMemory * 0.8 // 80% memory usage
    }

    private fun getDiskCacheSize(): Long {
        return cacheDir.walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
    }

    // Cleanup resources
    fun cleanup() {
        optimizationScope.cancel()
        imageCache.evictAll()
        cacheDir.deleteRecursively()
    }

    // Data classes
    enum class PreloadPriority {
        LOW,
        NORMAL,
        HIGH
    }

    data class PerformanceMetrics(
        val cacheHitRate: Double,
        val memoryUsage: Int,
        val diskCacheSize: Long,
        val totalOperations: Int
    )

    // Lazy image loader for RecyclerViews
    class LazyImageLoader(
        private val visibleRange: IntRange,
        private val preloadDistance: Int,
        private val imageLoader: PerformanceOptimizationService
    ) {
        private val preloadScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        
        fun updateVisibleRange(newRange: IntRange) {
            val oldRange = visibleRange
            val newItems = newRange - oldRange
            
            // Preload new items
            newItems.forEach { index ->
                preloadScope.launch {
                    // Implement preloading logic here
                }
            }
        }
        
        fun cleanup() {
            preloadScope.cancel()
        }
    }
}
