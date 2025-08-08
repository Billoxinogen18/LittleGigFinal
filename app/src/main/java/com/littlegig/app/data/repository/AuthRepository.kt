package com.littlegig.app.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.littlegig.app.data.model.User
import com.littlegig.app.data.model.UserType
import com.littlegig.app.data.model.UserRank
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
                            } else {
                                // Create anonymous user if doesn't exist
                                val anonymousUser = createAnonymousUser(firebaseUser.uid)
                                cacheUser(anonymousUser)
                                trySend(anonymousUser)
                            }
                        } catch (e: Exception) {
                            // Return cached user on error
                            val cachedUser = getCachedUser(firebaseUser.uid)
                            if (cachedUser != null) {
                                trySend(cachedUser)
                            } else {
                                // Create anonymous user as fallback
                                val anonymousUser = createAnonymousUser(firebaseUser.uid)
                                cacheUser(anonymousUser)
                                trySend(anonymousUser)
                            }
                        }
                    }
                } else {
                    // Return cached user if no network
                    val cachedUser = getCachedUser(firebaseUser.uid)
                    if (cachedUser != null) {
                        trySend(cachedUser)
                    } else {
                        // Create anonymous user as fallback
                        val anonymousUser = createAnonymousUser(firebaseUser.uid)
                        cacheUser(anonymousUser)
                        trySend(anonymousUser)
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
            } else {
                // Create anonymous user as fallback
                val anonymousUser = createAnonymousUser(currentFirebaseUser.uid)
                cacheUser(anonymousUser)
                emit(anonymousUser)
            }
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    // 🔥 ANONYMOUS AUTHENTICATION - TIKTOK STYLE! 🔥
    suspend fun signInAnonymously(): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val result = auth.signInAnonymously().await()
            val firebaseUser = result.user ?: throw Exception("Failed to create anonymous user")
            
            // Create anonymous user with smart algorithm data
            val anonymousUser = createAnonymousUser(firebaseUser.uid)
            
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(anonymousUser)
                .await()
            
            cacheUser(anonymousUser)
            Result.success(anonymousUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 🧠 SMART ACCOUNT LINKING - PRESERVE ALGORITHM DATA! 🧠
    suspend fun linkAnonymousAccount(
        email: String, 
        password: String, 
        displayName: String,
        phoneNumber: String? = null,
        userType: UserType = UserType.REGULAR
    ): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No anonymous user to link")
            
            // Create credential for linking
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)
            
            // Link anonymous account with email/password
            val linkResult = currentUser.linkWithCredential(credential).await()
            val linkedUser = linkResult.user ?: throw Exception("Failed to link account")
            
            // Get existing anonymous user data to preserve algorithm
            val existingUserDoc = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()
            
            val existingUser = if (existingUserDoc.exists()) {
                existingUserDoc.toObject(User::class.java)?.copy(id = existingUserDoc.id)
            } else {
                createAnonymousUser(currentUser.uid)
            } ?: throw Exception("Failed to get existing user data")
            
            // 🧠 PRESERVE ALGORITHM DATA - SMART MERGE! 🧠
            val username = generateUsername(displayName, phoneNumber ?: "")
            val linkedUserData = User(
                id = linkedUser.uid,
                email = email,
                displayName = displayName,
                username = username,
                phoneNumber = phoneNumber ?: "",
                userType = userType,
                rank = UserRank.NOVICE,
                followers = existingUser.followers, // Preserve existing followers
                following = existingUser.following, // Preserve existing following
                bio = "",
                // 🧠 PRESERVE ALGORITHM DATA! 🧠
                profilePictureUrl = existingUser.profilePictureUrl,
                profileImageUrl = existingUser.profileImageUrl,
                isInfluencer = existingUser.isInfluencer,
                businessId = existingUser.businessId,
                createdAt = existingUser.createdAt, // Keep original creation date
                updatedAt = System.currentTimeMillis()
            )
            
            // Update user document with linked data
            firestore.collection("users")
                .document(linkedUser.uid)
                .set(linkedUserData)
                .await()
            
            cacheUser(linkedUserData)
            Result.success(linkedUserData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 🔥 GOOGLE SIGN-IN - FIREBASE SUPPORT! 🔥
    suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Failed to sign in with Google")
            
            // Check if user exists in Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            if (userDoc.exists()) {
                // User exists - return existing user
                val user = userDoc.toObject(User::class.java)?.copy(id = userDoc.id)
                    ?: throw Exception("Failed to parse user data")
                cacheUser(user)
                Result.success(user)
            } else {
                // Create new user from Google account
                val username = generateUsername(googleSignInAccount.displayName ?: "", googleSignInAccount.email ?: "")
                val newUser = User(
                    id = firebaseUser.uid,
                    email = googleSignInAccount.email ?: "",
                    displayName = googleSignInAccount.displayName ?: "Google User",
                    username = username,
                    phoneNumber = "",
                    userType = UserType.REGULAR,
                    rank = UserRank.NOVICE,
                    followers = emptyList(),
                    following = emptyList(),
                    bio = "",
                    profilePictureUrl = googleSignInAccount.photoUrl?.toString() ?: "",
                    profileImageUrl = googleSignInAccount.photoUrl?.toString() ?: "",
                    isInfluencer = false,
                    businessId = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(newUser)
                    .await()
                
                cacheUser(newUser)
                Result.success(newUser)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 🔥 LINK ANONYMOUS ACCOUNT WITH GOOGLE! 🔥
    suspend fun linkAnonymousAccountWithGoogle(googleSignInAccount: GoogleSignInAccount): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No anonymous user to link")
            
            // Create credential for linking
            val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
            
            // Link anonymous account with Google
            val linkResult = currentUser.linkWithCredential(credential).await()
            val linkedUser = linkResult.user ?: throw Exception("Failed to link account")
            
            // Get existing anonymous user data to preserve algorithm
            val existingUserDoc = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()
            
            val existingUser = if (existingUserDoc.exists()) {
                existingUserDoc.toObject(User::class.java)?.copy(id = existingUserDoc.id)
            } else {
                createAnonymousUser(currentUser.uid)
            } ?: throw Exception("Failed to get existing user data")
            
            // 🧠 PRESERVE ALGORITHM DATA - SMART MERGE! 🧠
            val username = generateUsername(googleSignInAccount.displayName ?: "", googleSignInAccount.email ?: "")
            val linkedUserData = User(
                id = linkedUser.uid,
                email = googleSignInAccount.email ?: "",
                displayName = googleSignInAccount.displayName ?: "Google User",
                username = username,
                phoneNumber = "",
                userType = UserType.REGULAR,
                rank = UserRank.NOVICE,
                followers = existingUser.followers, // Preserve existing followers
                following = existingUser.following, // Preserve existing following
                bio = "",
                // 🧠 PRESERVE ALGORITHM DATA! 🧠
                profilePictureUrl = googleSignInAccount.photoUrl?.toString() ?: existingUser.profilePictureUrl,
                profileImageUrl = googleSignInAccount.photoUrl?.toString() ?: existingUser.profileImageUrl,
                isInfluencer = existingUser.isInfluencer,
                businessId = existingUser.businessId,
                createdAt = existingUser.createdAt, // Keep original creation date
                updatedAt = System.currentTimeMillis()
            )
            
            // Update user document with linked data
            firestore.collection("users")
                .document(linkedUser.uid)
                .set(linkedUserData)
                .await()
            
            cacheUser(linkedUserData)
            Result.success(linkedUserData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 🧠 SMART PHONE ACCOUNT LINKING! 🧠
    suspend fun linkAnonymousAccountWithPhone(
        phoneNumber: String,
        displayName: String,
        userType: UserType = UserType.REGULAR
    ): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No anonymous user to link")
            
            // Get existing anonymous user data to preserve algorithm
            val existingUserDoc = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()
            
            val existingUser = if (existingUserDoc.exists()) {
                existingUserDoc.toObject(User::class.java)?.copy(id = existingUserDoc.id)
            } else {
                createAnonymousUser(currentUser.uid)
            } ?: throw Exception("Failed to get existing user data")
            
            // 🧠 PRESERVE ALGORITHM DATA - SMART MERGE! 🧠
            val username = generateUsername(displayName, phoneNumber)
            val linkedUserData = User(
                id = currentUser.uid,
                email = "",
                displayName = displayName,
                username = username,
                phoneNumber = phoneNumber,
                userType = userType,
                rank = UserRank.NOVICE,
                followers = existingUser.followers, // Preserve existing followers
                following = existingUser.following, // Preserve existing following
                bio = "",
                // 🧠 PRESERVE ALGORITHM DATA! 🧠
                profilePictureUrl = existingUser.profilePictureUrl,
                profileImageUrl = existingUser.profileImageUrl,
                isInfluencer = existingUser.isInfluencer,
                businessId = existingUser.businessId,
                createdAt = existingUser.createdAt, // Keep original creation date
                updatedAt = System.currentTimeMillis()
            )
            
            // Update user document with linked data
            firestore.collection("users")
                .document(currentUser.uid)
                .set(linkedUserData)
                .await()
            
            cacheUser(linkedUserData)
            Result.success(linkedUserData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // 🧠 CREATE ANONYMOUS USER WITH SMART ALGORITHM DATA! 🧠
    private fun createAnonymousUser(uid: String): User {
        return User(
            id = uid,
            email = "",
            displayName = "Anonymous User",
            username = "anon_${uid.takeLast(8)}",
            phoneNumber = "",
            userType = UserType.REGULAR,
            rank = UserRank.NOVICE,
            followers = emptyList(),
            following = emptyList(),
            bio = "",
            profilePictureUrl = "",
            profileImageUrl = "",
            isInfluencer = false,
            businessId = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
    
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
    
    suspend fun signUpWithPhone(phoneNumber: String, displayName: String, userType: UserType): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            val username = generateUsername(displayName, phoneNumber)
            
            val newUser = User(
                id = "phone_${System.currentTimeMillis()}",
                email = "",
                displayName = displayName,
                username = username,
                phoneNumber = phoneNumber,
                userType = userType,
                rank = UserRank.NOVICE,
                followers = emptyList(),
                following = emptyList(),
                bio = "",
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
    
    suspend fun signInWithPhone(phoneNumber: String): Result<User> {
        if (!isNetworkAvailable()) {
            return Result.failure(Exception("No network connection"))
        }
        
        return try {
            // Check if user exists with this phone number
            val userQuery = firestore.collection("users")
                .whereEqualTo("phoneNumber", phoneNumber)
                .limit(1)
                .get()
                .await()
            
            if (userQuery.documents.isNotEmpty()) {
                val userDoc = userQuery.documents[0]
                val user = userDoc.toObject(User::class.java)?.copy(id = userDoc.id)
                    ?: throw Exception("User data corrupted")
                cacheUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception("No account found with this phone number"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateUsername(email: String): String {
        val base = email.split("@")[0].replace(Regex("[^a-zA-Z0-9]"), "").take(8)
        val randomSuffix = (100..999).random()
        return "${base}${randomSuffix}".lowercase()
    }
    
    private fun generateUsername(displayName: String, phoneNumber: String): String {
        val base = displayName.replace(Regex("[^a-zA-Z0-9]"), "").take(8)
        val phoneSuffix = phoneNumber.takeLast(4)
        val randomSuffix = (100..999).random()
        return "${base}${phoneSuffix}${randomSuffix}".lowercase()
    }
}