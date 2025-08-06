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

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                // Fetch user details from Firestore
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val user = document.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                            trySend(user)
                        } else {
                            // If user doesn't exist in Firestore, create a basic user object
                            val basicUser = User(
                                id = firebaseUser.uid,
                                email = firebaseUser.email ?: "",
                                displayName = firebaseUser.displayName ?: "",
                                profileImageUrl = firebaseUser.photoUrl?.toString() ?: ""
                            )
                            trySend(basicUser)
                        }
                    }
                    .addOnFailureListener {
                        // On failure, still return a basic user object
                        val basicUser = User(
                            id = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: "",
                            profileImageUrl = firebaseUser.photoUrl?.toString() ?: ""
                        )
                        trySend(basicUser)
                    }
            } else {
                trySend(null)
            }
        }
        
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }
    
    suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("User not found"))
            
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
                
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                    ?: return Result.failure(Exception("User data not found"))
                Result.success(user)
            } else {
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUpWithEmail(
        email: String, 
        password: String, 
        displayName: String,
        phoneNumber: String = "",
        userType: UserType = UserType.REGULAR
    ): Result<User> {
        return try {
            // Create the Firebase Auth user first
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Failed to create user"))
            
            // Ensure the user is properly authenticated before writing to Firestore
            if (firebaseAuth.currentUser?.uid != firebaseUser.uid) {
                return Result.failure(Exception("Authentication state mismatch"))
            }
            
            val user = User(
                id = firebaseUser.uid,
                email = email,
                displayName = displayName,
                phoneNumber = phoneNumber,
                userType = userType
            )
            
            // Save user profile to Firestore with retry logic
            try {
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()
            } catch (firestoreError: Exception) {
                // If Firestore write fails, delete the auth user to maintain consistency
                firebaseUser.delete().await()
                return Result.failure(firestoreError)
            }
                
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Failed to sign in"))
            
            // Check if user exists in Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
                
            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                    ?: return Result.failure(Exception("Failed to get user data"))
            } else {
                // Create new user profile
                val newUser = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: ""
                )
                
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(newUser)
                    .await()
                    
                newUser
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .set(user.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}