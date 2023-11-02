package com.example.mainpage_rangebuddy


import com.example.mainpage_rangebuddy.models.Data
import com.example.mainpage_rangebuddy.models.LoginRequest
import com.example.mainpage_rangebuddy.models.LoginResponse
import com.example.mainpage_rangebuddy.models.Profile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APICall {
    @GET("/api/profile/")
    fun getProfiles(): Call<List<Profile>>

    @POST("/api/profile/")
    fun postProfile(@Body profile: Profile): Call<Profile>

    @POST("/api/login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/datapoint/")
    fun datapoint(@Body dataRequest: Data, @Header("Authorization") authorization: String): Call<Data>

}