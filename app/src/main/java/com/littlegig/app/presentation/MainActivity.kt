package com.littlegig.app.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.littlegig.app.presentation.LittleGigApp
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.littlegig.app.presentation.auth.AuthViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private lateinit var googleSignInClient: GoogleSignInClient
    private val authViewModel: AuthViewModel by viewModels()
    
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            // 🔥 REAL GOOGLE SIGN-IN HANDLING! 🔥
            handleGoogleSignInSuccess(account)
        } catch (e: ApiException) {
            // Handle sign-in failure
            println("Google Sign-In failed: ${e.message}")
            handleGoogleSignInFailure(e)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge design
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("673023203315-ukp6vv5c3004098667nv4ajols00l7iv.apps.googleusercontent.com")
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        
        // 🔥 AUTOMATIC ANONYMOUS AUTHENTICATION - TIKTOK STYLE! 🔥
        // Anonymous auth will be handled in LittleGigApp
        
        setContent {
            LittleGigApp()
        }
    }
    
    fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }
    
    // 🔥 REAL GOOGLE SIGN-IN SUCCESS HANDLING! 🔥
    private fun handleGoogleSignInSuccess(account: com.google.android.gms.auth.api.signin.GoogleSignInAccount) {
        val current = authViewModel.currentUser.value
        if (current == null || current.email.isNotEmpty()) {
            authViewModel.signInWithGoogle(account)
        } else {
            authViewModel.linkAnonymousAccountWithGoogle(account)
        }
    }
    
    // 🔥 REAL GOOGLE SIGN-IN FAILURE HANDLING! 🔥
    private fun handleGoogleSignInFailure(exception: ApiException) {
        // no-op for now (UI observes error state)
    }
}