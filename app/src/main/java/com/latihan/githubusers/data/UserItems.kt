package com.latihan.githubusers.data

import com.google.gson.annotations.SerializedName

data class UserItems(
    @field:SerializedName("login")
    val username: String = "",

    @field:SerializedName("avatar_url")
    val photo: String = "",

    @field:SerializedName("id")
    val id: Int = 0,
)
