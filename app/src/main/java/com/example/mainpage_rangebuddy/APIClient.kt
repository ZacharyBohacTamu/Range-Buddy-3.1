package com.example.mainpage_rangebuddy

import com.squareup.okhttp.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    fun retrofitBuilder(): APICall{
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:8000")
            //.baseUrl("ec2-13-58-120-15.us-east-2.compute.amazonaws.com/")
         .build()
        return retrofit.create(APICall::class.java)
    }
}