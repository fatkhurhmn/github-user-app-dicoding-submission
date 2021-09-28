package com.latihan.githubusers.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubusers.adapter.ListUserAdapter
import com.latihan.githubusers.databinding.FragmentFollowingBinding
import com.latihan.githubusers.ui.detail.DetailUserActivity

class FollowingFragment : Fragment() {

    private val listFollowingAdapter: ListUserAdapter by lazy {
        ListUserAdapter()
    }
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding
    private val followingViewModel: FollowingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListFollower()
        showListFollower()
        loadingHandling()
        errorHandling()
    }

    private fun setListFollower() {
        val username = (activity as DetailUserActivity).getUsername()
        username?.let {
            followingViewModel.setUser(username)
        }
    }

    private fun showListFollower() {
        followingViewModel.getUsers().observe(viewLifecycleOwner, { followerItems ->
            followerItems?.let {
                if (followerItems.isNotEmpty()) {
                    listFollowingAdapter.setUser(followerItems)
                }
            }
            binding?.rvFollowing?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listFollowingAdapter
            }
        })
    }

    private fun loadingHandling() {
        followingViewModel.loadingState.observe(viewLifecycleOwner, { isLoading ->
            binding?.apply {
                if (isLoading) {
                    rvFollowing.visibility = View.GONE
                    viewFollowingListLoading.root.visibility = View.VISIBLE
                } else {
                    rvFollowing.visibility = View.VISIBLE
                    viewFollowingListLoading.root.visibility = View.GONE
                }
            }
        })
    }

    private fun errorHandling() {
        with(followingViewModel) {
            isError.observe(viewLifecycleOwner, { isError ->
                binding?.apply {
                    if (isError) {
                        viewFollowingListLoading.root.visibility = View.GONE
                        errorMessage?.let {
                            Toast.makeText(
                                context,
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}