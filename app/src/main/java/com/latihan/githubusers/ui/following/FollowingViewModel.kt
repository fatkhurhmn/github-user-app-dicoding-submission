package com.latihan.githubusers.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihan.githubusers.BuildConfig
import com.latihan.githubusers.data.UserItems
import com.latihan.githubusers.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val token = BuildConfig.API_KEY
    private val listFollowing = MutableLiveData<ArrayList<UserItems>>()

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String? = null

    fun setUser(username: String) {
        val followingItems = ArrayList<UserItems>()
        _isError.postValue(false)
        val client = ApiConfig.getApiService().getListFollowing(username, token)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                if (response.isSuccessful) {
                    _loadingState.postValue(false)
                    followingItems.addAll(response.body() as ArrayList<UserItems>)
                    listFollowing.postValue(followingItems)
                }
            }

            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isError.postValue(true)
                _loadingState.postValue(false)
                errorMessage = "LIST FOLLOWING\n${t.message}"
            }

        })
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> {
        return listFollowing
    }
}