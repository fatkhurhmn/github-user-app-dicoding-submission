package com.latihan.githubusers.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubusers.R
import com.latihan.githubusers.adapter.ListUserAdapter
import com.latihan.githubusers.data.UserItems
import com.latihan.githubusers.databinding.ActivityMainBinding
import com.latihan.githubusers.ui.settings.SettingActivity
import com.latihan.githubusers.ui.detail.DetailUserActivity
import com.latihan.githubusers.ui.favorite.FavoriteActivity

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val listUserAdapter: ListUserAdapter by lazy {
        ListUserAdapter()
    }
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        searchUsers()
        hideToType()
        showListUsers()
        loadingHandling()
        noResultHandling()
        errorHandling()

    }

    private fun initToolbar() {
        binding.topAppbar.setOnMenuItemClickListener(this)
    }

    private fun searchUsers() {
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(username: String?): Boolean {
                    listUserAdapter.clearListUsers()
                    username?.let {
                        mainViewModel.setUser(username)
                    }
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun hideToType() {
        mainViewModel.isTypeDone.observe(this@MainActivity, { isTypeDone ->
            isTypeDone?.let {
                if (isTypeDone) {
                    binding.viewToType.root.visibility = View.GONE
                }
            }
        })
    }

    private fun showListUsers() {
        mainViewModel.getUsers().observe(this@MainActivity, { userItems ->
            userItems?.let {
                if (userItems.isNotEmpty()) {
                    listUserAdapter.setUser(userItems)
                }
            }
        })

        with(binding.rvUsers) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listUserAdapter
        }

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserItems) {
                val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.username)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_ID, user.id)
                detailUserIntent.putExtra(DetailUserActivity.EXTRA_PHOTO, user.photo)
                startActivity(detailUserIntent)
            }
        })
    }

    private fun loadingHandling() {
        mainViewModel.loadingState.observe(this@MainActivity, { isLoading ->
            with(binding) {
                if (isLoading) {
                    rvUsers.visibility = View.GONE
                    viewListLoading.root.visibility = View.VISIBLE
                } else {
                    rvUsers.visibility = View.VISIBLE
                    viewListLoading.root.visibility = View.GONE
                }
            }
        })
    }

    private fun noResultHandling() {
        mainViewModel.isNoResult.observe(this@MainActivity, { isNoResult ->
            with(binding) {
                if (isNoResult) {
                    viewNoResult.root.visibility = View.VISIBLE
                    rvUsers.visibility = View.GONE
                } else {
                    viewNoResult.root.visibility = View.GONE
                }
            }
        })
    }

    private fun errorHandling() {
        with(mainViewModel) {
            isError.observe(this@MainActivity, { isError ->
                with(binding) {
                    if (isError) {
                        viewError.root.visibility = View.VISIBLE
                        rvUsers.visibility = View.GONE
                        errorMessage?.let {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                        }
                        errorMessage = null
                    } else {
                        viewError.root.visibility = View.GONE
                    }
                }
            })
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_settings -> {
                val intentSettings = Intent(this, SettingActivity::class.java)
                startActivity(intentSettings)
                true
            }
            R.id.btn_favorite -> {
                val intentFavorite = Intent(this, FavoriteActivity::class.java)
                startActivity(intentFavorite)
                true
            }
            else -> false
        }
    }

}