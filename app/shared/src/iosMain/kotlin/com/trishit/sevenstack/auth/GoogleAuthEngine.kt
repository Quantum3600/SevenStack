package com.trishit.sevenstack.auth

import platform.Foundation.NSNotificationCenter
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class GoogleAuthEngine {
    
    companion object {
        private var activeContinuation: Continuation<String>? = null

        fun onTokenReceived(idToken: String) {
            activeContinuation?.resumeWith(Result.success(idToken))
            activeContinuation = null
        }

        fun onAuthFailed(errorMsg: String) {
            activeContinuation?.resumeWith(Result.failure(Exception(errorMsg)))
            activeContinuation = null
        }
    }

    actual suspend fun fetchGoogleIdToken(): String = suspendCoroutine { continuation ->
        activeContinuation = continuation
        NSNotificationCenter.defaultCenter.postNotificationName("TriggerNativeIOSGoogleAuth", null)
    }

    actual suspend fun clearAuthSession() {
        NSNotificationCenter.defaultCenter.postNotificationName("TriggerNativeIOSGoogleLogout", null)
    }
}
