package com.example.mainpage_rangebuddy.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("bullet_group")
    val bulletGroup: Float,
    @SerializedName("point_calc")
    val pointCalc: Float,
)
