package com.littlegig.app.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val context: Context
) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val userCache = mutableMapOf<String, User>()
    private val CACHE_DURATION = 10 * 60 * 1000L // 10 minutes
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    
    private fun getCachedUser(userId: String): User? {
        val cacheTime = prefs.getLong("user_cache_time_$userId", 0)
        if (System.currentTimeMillis() - cacheTime > CACHE_DURATION) {
            userCache.remove(userId)
            return null
        }
        return userCache[userId]
    }
    
    private fun cacheUser(user: User) {
        userCache[user.id] = user
        prefs.edit().putLong("user_cache_time_${user.id}", System.currentTimeMillis()).apply()
    }
    
    val currentUser: StateFlow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                // Try cache first
                val cachedUser = getCachedUser(firebaseUser.uid)
                if (cachedUser != null) {
                    trySend(cachedUser)
                }
                
                // Fetch from Firebase if network available
                if (isNetworkAvailable()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val userDoc = firestore.collection("users")
                                .document(firebaseUser.uid)
                                .get()
                                .await()
                            
                            if (userDoc.exists()) {
                                val user = userDoc.toObject(User::class.java)?.copy(id = userDoc.id)
                                    ?: return@launch
                                cacheUser(user)
                                trySend(user)
                            }
                        } catch (e: Exception) {
                            // Return cached user on error
                            val cachedUser = getCachedUser(firebaseUser.uid)
                            if (cachedUser != null) {
                                trySend(cachedUser)
                            }
                        }
                    }
                } else {
                    // Return cached user if no network
                    val cachedUser = getCachedUser(firebaseUser.uid)
                    if (cachedUser != null) {
                        trySend(cachedUser)
                    }
                }
            } else {
                trySend(null)
            }
        }
        
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.retry(3) { cause ->
        cause is Exception && isNetworkAvailable()
    }.catch { error ->
        // Return cached user on error
        val currentFirebaseUser = auth.currentUser
        if (currentFirebaseUser != null) {
            val cachedUser = getCachedUser(currentFirebaseUser.uid)
            if (cachedUser != null) {
                emit(cachedUser)
            }
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    suspend fun signUp(email: String, password: String, userType: UserType, phoneNumber: String? = null): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Failed to create user")
            
            // Generate username from email
            val username = generateUsername(email)
            
            val user = User(
                id = firebaseUser.uid,
                email = email,

                displayName = username,
                phoneNumber = phoneNumber ?: "",
                userType = userType,
                createdAt = System.currentTimeMillis()
            )
            
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()
            
            cacheUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Failed to sign in")
            
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)?.copy(id = userDoc.id)
                    ?: throw Exception("Failed to parse user data")
                cacheUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            userCache.clear()
            prefs.edit().clear().apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateProfile(updates: Map<String, Any>): Result<Unit> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No user signed in")
            
            firestore.collection("users")
                .document(currentUser.uid)
                .update(updates)
                .await()
            
            // Clear cache to force refresh
            userCache.clear()
            prefs.edit().clear().apply()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteAccount(): Result<Unit> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No user signed in")
            
            // Delete user data from Firestore
            firestore.collection("users")
                .document(currentUser.uid)
                .delete()
                .await()
            
            // Delete Firebase Auth account
            currentUser.delete().await()
            
            // Clear cache
            userCache.clear()
            prefs.edit().clear().apply()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun clearCache() {
        userCache.clear()
        prefs.edit().clear().apply()
    }
    
    suspend fun signInWithPhone(phoneNumber: String, verificationCode: String): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            // This would require Firebase Phone Auth setup
            // For now, we'll create a user with phone number
            val username = generateUsername(phoneNumber)
            
            val newUser = User(
                id = "phone_${System.currentTimeMillis()}",
                email = "",
                displayName = username,
                phoneNumber = phoneNumber,
                userType = UserType.REGULAR,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection("users").document(newUser.id).set(newUser).await()
            cacheUser(newUser)
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateUsername(emailOrPhone: String): String {
        val base = emailOrPhone.split("@")[0].replace(Regex("[^a-zA-Z0-9]"), "")
        val randomSuffix = (1000..9999).random()
        return "${base}${randomSuffix}"
    }
}