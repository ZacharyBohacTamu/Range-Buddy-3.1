package com.example.mainpage_rangebuddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mainpage_rangebuddy.models.Data
import com.example.mainpage_rangebuddy.models.LoginRequest
import com.example.mainpage_rangebuddy.models.LoginResponse
import com.example.mainpage_rangebuddy.models.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIViewModel: ViewModel() {
    private val liveData = MutableLiveData<List<Profile>>()

    fun getData(): LiveData<List<Profile>>{
        val response = APIClient().retrofitBuilder().getProfiles()
        response.enqueue(object : Callback<List<Profile>> {
            override fun onResponse(
                call: Call<List<Profile>>?,
                response: Response<List<Profile>>?
            ) {
                if(response?.isSuccessful == true){
                    liveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Profile>>?, t: Throwable?) {
                Log.d("Api failure", t.toString())
            }

        })
        return liveData
    }

    fun postData(profile: Profile){
        val response = APIClient().retrofitBuilder().postProfile(profile)
        response.enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>?, response: Response<Profile>?) {
                Log.d("response", "Success")
            }

            override fun onFailure(call: Call<Profile>?, t: Throwable?) {
                Log.d("Api failure", t.toString())
            }
        })
    }
    fun login(loginRequest: LoginRequest){
        val response = APIClient().retrofitBuilder().login(loginRequest)
        response.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                Log.d("response", "Success")
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Log.d("Api failure", t.toString())
            }
        })
    }
    fun datapoint(data: Data){
        val token = "Token 4bf64cd47c80c5df195950cad92ddedd6108d5b2"
        val response = APIClient().retrofitBuilder().datapoint(data,token)
        response.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>?, response: Response<Data>?) {
                Log.d("response", "Success")
            }

            override fun onFailure(call: Call<Data>?, t: Throwable?) {
                Log.d("Api failure", t.toString())
            }
        })
    }
}