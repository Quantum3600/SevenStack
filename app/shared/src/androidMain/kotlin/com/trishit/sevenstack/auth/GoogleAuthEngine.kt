package com.trishit.sevenstack.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class GoogleAuthEngine(private val context: Context) {
    private val credentialManager = CredentialManager.create(context)
    
    // Explicit server client ID from Google Cloud Console
    private val SERVER_CLIENT_ID = "5375786843-13lst1ihkjl1gh1nduhb5ijd7fnuutrm.apps.googleusercontent.com"

    actual suspend fun fetchGoogleIdToken(): String = withContext(Dispatchers.Main) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(SERVER_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential

            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return@withContext tokenCredential.idToken
            } else {
                throw IllegalStateException("Parsed invalid native payload descriptor.")
            }
        } catch (e: GetCredentialException) {
            throw Exception("Google auth bottom sheet aborted by user: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Android internal auth ecosystem error: ${e.message}")
        }
    }

    actual suspend fun clearAuthSession() {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) { /* State trace cleared */ }
    }
}
