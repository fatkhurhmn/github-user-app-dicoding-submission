package com.latihan.githubusers.ui.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.latihan.githubusers.R
import com.latihan.githubusers.adapter.PagerAdapter
import com.latihan.githubusers.database.Favorite
import com.latihan.githubusers.databinding.ActivityDetailUserBinding
import com.latihan.githubusers.helper.ViewModelFactory

class DetailUserActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailUserViewModel = obtainViewModel(this)

        initToolbar()
        initTabLayout()
        backButton()
        initUserData()
        setUserData()
        loadingHandling()
        errorHandling()
        setFavorite()
    }

    private fun initToolbar() {
        with(binding.detailTopAppbar) {
            title = intent.getStringExtra(EXTRA_USERNAME)
            setOnMenuItemClickListener(this@DetailUserActivity)
        }
    }

    private fun initUserData() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        username?.let { detailUserViewModel.setDetailUser(it) }
    }

    private fun setUserData() {
        detailUserViewModel.getDetailUser().observe(this, { userDetail ->
            with(binding) {
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_user_default)
                    .fitCenter()
                Glide.with(this@DetailUserActivity).load(userDetail.photo).apply(requestOptions)
                    .into(detailImgAvatar)
                detailTvName.text = userDetail.name
                detailTvCompany.text = userDetail.company
                detailTvLocation.text = userDetail.location
                detailTvFollower.text = getFormattedNumber(userDetail.followers)
                detailTvFollowing.text = getFormattedNumber(userDetail.following)
                detailTvRepository.text = getFormattedNumber(userDetail.publicRepos)
            }
        })
    }

    private fun initTabLayout() {
        val pagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun loadingHandling() {
        detailUserViewModel.loadingState.observe(this, { isLoading ->
            with(binding.viewDetailLoading) {
                if (isLoading) {
                    root.visibility = View.VISIBLE
                } else {
                    root.visibility = View.GONE
                }
            }
        })
    }

    fun getUsername(): String? {
        return intent.getStringExtra(EXTRA_USERNAME)
    }

    private fun backButton() {
        binding.detailTopAppbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun errorHandling() {
        with(detailUserViewModel) {
            isError.observe(this@DetailUserActivity, { isError ->
                with(binding) {
                    if (isError) {
                        Glide.with(this@DetailUserActivity).load(R.drawable.ic_user_default)
                            .into(binding.detailImgAvatar)
                        viewDetailLoading.root.visibility = View.GONE
                        errorMessage?.let {
                            Toast.makeText(
                                this@DetailUserActivity,
                                it,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        errorMessage = null
                    }
                }
            })
        }
    }

    private fun setFavorite() {
        val favorite = Favorite()
        favorite.id = intent.getIntExtra(EXTRA_ID, 0)
        favorite.username = intent.getStringExtra(EXTRA_USERNAME)
        favorite.photo = intent.getStringExtra(EXTRA_PHOTO)

        detailUserViewModel.getFavoriteById(favorite.id!!).observe(this, { listFav ->
            isFavorite = listFav.isNotEmpty()

            binding.fabFavorite.imageTintList = if (listFav.isEmpty()) {
                ColorStateList.valueOf(Color.rgb(255, 255, 255))
            } else {
                ColorStateList.valueOf(Color.rgb(247, 106, 123))
            }

        })

        binding.fabFavorite.apply {
            setOnClickListener {
                if (isFavorite) {
                    detailUserViewModel.delete(favorite)
                    snackBarShow(favorite.username, R.string.delete_favorite)
                } else {
                    detailUserViewModel.insert(favorite)
                    snackBarShow(favorite.username, R.string.add_favorite)
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }

    private fun snackBarShow(username: String?, action: Int) {
        val coordinator = binding.coordinator
        val snackBar = Snackbar.make(
            coordinator,
            getString(action, username),
            Snackbar.LENGTH_SHORT
        )

        val snackView = snackBar.view
        val text = snackView.findViewById<TextView>(R.id.snackbar_text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        } else {
            text.gravity = Gravity.CENTER_HORIZONTAL
        }
        snackBar.show()
    }

    private fun getFormattedNumber(number: Int): String {
        val numberString = when {
            number / 1_0000 >= 1 -> {
                val count = number / 1_000
                String.format("%d%s", count, getString(R.string.kilo))
            }
            number / 1_000_000 >= 1 -> {
                val count = number / 1_000_000
                String.format("%d%s", count, getString(R.string.mega))
            }
            else -> {
                number.toString()
            }
        }
        return numberString
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following,
            R.string.repository,
        )

        const val EXTRA_USERNAME = "extra username"
        const val EXTRA_ID = "extra id"
        const val EXTRA_PHOTO = "extra photo"
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_share -> {
                var shareValue: String?
                binding.apply {
                    shareValue = """|Name    : ${detailTvName.text}
                    |Username   : ${intent.getStringExtra(EXTRA_USERNAME)}
                    |Location   : ${detailTvLocation.text}
                    |Company    : ${detailTvCompany.text}
                    |Repository : ${detailTvRepository.text}
                    |Followers  : ${detailTvFollower.text}
                    |Following  : ${detailTvFollowing.text}
                    |
                    |Link : https://github.com/${intent.getStringExtra(EXTRA_USERNAME)}
                """.trimMargin()
                }

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareValue)
                    type = "text/plain"
                }

                val intentShare = Intent.createChooser(sendIntent, null)
                startActivity(intentShare)
                true
            }
            else -> false
        }
    }
}
