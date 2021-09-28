package com.latihan.githubusers.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihan.githubusers.data.UserDetail
import com.latihan.githubusers.database.Favorite
import com.latihan.githubusers.network.ApiConfig
import com.latihan.githubusers.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val userDetail = MutableLiveData<UserDetail>()

    private val _loadingState = MutableLiveData(true)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String? = null

    fun setDetailUser(username: String) {
        _isError.postValue(false)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    _loadingState.postValue(false)
                    response.body()?.let { userDetail.postValue(it) }
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _isError.postValue(true)
                _loadingState.postValue(false)
                errorMessage = "DETAIL USER\n${t.message}"
            }

        })
    }

    fun getDetailUser(): LiveData<UserDetail> {
        return userDetail
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }

    fun getFavoriteById(id: Int): LiveData<List<Favorite>> {
        return mFavoriteRepository.getFavoriteById(id)
    }

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
}