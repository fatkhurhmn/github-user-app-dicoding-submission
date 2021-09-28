package com.latihan.githubusers.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.latihan.githubusers.database.Favorite
import com.latihan.githubusers.database.FavoriteDao
import com.latihan.githubusers.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Favorite>> {
        return mFavoriteDao.getAllFavorite()
    }

    fun getFavoriteById(id: Int): LiveData<List<Favorite>> {
        return mFavoriteDao.getFavoriteById(id)
    }

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

}


