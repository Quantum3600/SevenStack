package com.trishit.sevenstack.auth

expect class GoogleAuthEngine {
    suspend fun fetchGoogleIdToken(): String
    suspend fun clearAuthSession()
}
