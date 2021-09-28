package com.latihan.githubusers.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubusers.adapter.ListFavoriteAdapter
import com.latihan.githubusers.database.Favorite
import com.latihan.githubusers.databinding.ActivityFavoriteBinding
import com.latihan.githubusers.helper.ViewModelFactory
import com.latihan.githubusers.ui.detail.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    private lateinit var listFavoriteAdapter: ListFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        showListFavorite()
        setListFavorite()
        backButton()
    }

    private fun showListFavorite() {
        listFavoriteAdapter = ListFavoriteAdapter()

        binding?.rvFavorite?.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = listFavoriteAdapter
        }

        listFavoriteAdapter.setOnItemClickCallback(object :
            ListFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(favorite: Favorite) {
                val detailUserIntent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, favorite.username)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_ID, favorite.id)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_PHOTO, favorite.photo)
                startActivity(detailUserIntent)
            }

        })
    }

    private fun setListFavorite() {
        favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllFavorite().observe(this, { favList ->
            if (favList != null) {
                listFavoriteAdapter.setListFavorite(favList)
            }

            if (favList.isEmpty()) {
                binding?.viewEmptyFavorite?.root?.visibility = View.VISIBLE
            } else {
                binding?.viewEmptyFavorite?.root?.visibility = View.GONE
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun backButton() {
        binding?.favoriteTopAppbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}