package com.latihan.githubusers.data

import com.google.gson.annotations.SerializedName

data class UserDetail(

	@field:SerializedName("company")
	val company: String = "",

	@field:SerializedName("public_repos")
	val publicRepos: Int = 0,

	@field:SerializedName("url")
	val url: String? = "",

	@field:SerializedName("followers")
	val followers: Int = 0,

	@field:SerializedName("avatar_url")
	val photo: String = "",

	@field:SerializedName("following")
	val following: Int = 0,

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("location")
	val location: String = "",

)
