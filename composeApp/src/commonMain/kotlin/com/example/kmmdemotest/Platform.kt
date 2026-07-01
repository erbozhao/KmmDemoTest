package com.example.kmmdemotest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform