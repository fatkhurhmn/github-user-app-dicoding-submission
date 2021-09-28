package com.latihan.githubusers.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihan.githubusers.BuildConfig
import com.latihan.githubusers.data.ListUsers
import com.latihan.githubusers.data.UserItems
import com.latihan.githubusers.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val token = BuildConfig.API_KEY
    private val listUsers = MutableLiveData<ArrayList<UserItems>>()


    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _isTypeDone = MutableLiveData<Boolean>()
    val isTypeDone: LiveData<Boolean> get() = _isTypeDone

    private val _isNoResult = MutableLiveData<Boolean>()
    val isNoResult: LiveData<Boolean> get() = _isNoResult

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String? = null

    fun setUser(username: String) {
        val userItems = ArrayList<UserItems>()
        _isTypeDone.postValue(true)
        _loadingState.postValue(true)
        _isNoResult.postValue(false)
        _isError.postValue(false)

        val client = ApiConfig.getApiService().getListUsers(username, token)
        client.enqueue(object : Callback<ListUsers> {
            override fun onResponse(
                call: Call<ListUsers>,
                response: Response<ListUsers>
            ) {
                if (response.isSuccessful) {
                    _loadingState.postValue(false)
                    userItems.addAll(response.body()?.users as ArrayList<UserItems>)
                    _isNoResult.postValue(userItems.isEmpty())
                    listUsers.postValue(userItems)
                } else {
                    Log.e("Error", "onResponse: $response")
                }
            }

            override fun onFailure(call: Call<ListUsers>, t: Throwable) {
                Log.e("Error", "onFailure: ${t.message}")
                errorMessage = t.message
                _isError.postValue(true)
                _loadingState.postValue(false)
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> {
        return listUsers
    }
}