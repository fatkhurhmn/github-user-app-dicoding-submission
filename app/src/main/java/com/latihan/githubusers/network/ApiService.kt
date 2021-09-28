package com.latihan.githubusers.network

import com.latihan.githubusers.data.ListUsers
import com.latihan.githubusers.data.UserDetail
import com.latihan.githubusers.data.UserItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") username: String,
        @Header("Authorization") token : String
    ): Call<ListUsers>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("users/{username}/followers")
    fun getListFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ):Call<List<UserItems>>

    @GET("users/{username}/following")
    fun getListFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ):Call<List<UserItems>>
}