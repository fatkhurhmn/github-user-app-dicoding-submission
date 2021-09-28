package com.latihan.githubusers.data

import com.google.gson.annotations.SerializedName

data class ListUsers(
    @field:SerializedName("items")
    val users: ArrayList<UserItems> = arrayListOf()
)