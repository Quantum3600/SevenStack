package com.trishit.sevenstack.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import java.io.File
import java.util.Properties

object GoogleVerifier {
    private val properties = Properties().apply {
        // local.properties is usually at the project root
        // We try both the current directory and the parent directory to accommodate different execution contexts
        val rootFile = File("local.properties")
        val parentFile = File("../local.properties")

        val propertiesFile = when {
            rootFile.exists() -> rootFile
            parentFile.exists() -> parentFile
            else -> null
        }

        propertiesFile?.inputStream()?.use { load(it) }
    }

    private val GOOGLE_CLIENT_ID: String = properties.getProperty("google.client.id") ?: ""


    private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(GOOGLE_CLIENT_ID))
        .build()

    fun verifyToken(idTokenString: String): GoogleProfile? {
        return try {
            val idToken = verifier.verify(idTokenString) ?: return null
            val payload = idToken.payload
            GoogleProfile(
                id = payload.subject,
                email = payload.email,
                name = payload["name"]?.toString() ?: payload.email.substringBefore("@"),
                pictureUrl = payload["picture"]?.toString()
            )
        } catch (e: Exception) {
            null
        }
    }
}

data class GoogleProfile(
    val id: String,
    val email: String,
    val name: String,
    val pictureUrl: String?
)
