package com.trishit.sevenstack.network

import com.trishit.sevenstack.UserDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class SevenStackApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val baseUrl = "http://10.0.2.2:8080" // Default for Android Emulator to localhost

    suspend fun verifyGoogleToken(idToken: String): Result<UserDto> = try {
        val response = client.submitForm(
            url = "$baseUrl/api/auth/google",
            formParameters = parameters {
                append("idToken", idToken)
            }
        )
        if (response.status == HttpStatusCode.OK) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Server returned ${response.status}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserProfile(userId: String): Result<UserDto> = try {
        // Implementation for getting user profile if needed
        Result.failure(Exception("Not implemented yet"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
