package com.example.mainpage_rangebuddy.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val email: String,
    val password: String,
)