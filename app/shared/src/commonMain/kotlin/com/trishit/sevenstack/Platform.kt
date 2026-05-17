package com.trishit.sevenstack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform