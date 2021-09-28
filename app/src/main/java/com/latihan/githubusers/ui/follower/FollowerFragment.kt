package com.latihan.githubusers.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubusers.adapter.ListUserAdapter
import com.latihan.githubusers.databinding.FragmentFollowerBinding
import com.latihan.githubusers.ui.detail.DetailUserActivity

class FollowerFragment : Fragment() {

    private val listFollowerAdapter: ListUserAdapter by lazy {
        ListUserAdapter()
    }
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding
    private val followerViewModel: FollowerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
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
            followerViewModel.setUser(username)
        }
    }

    private fun showListFollower() {
        followerViewModel.getUsers().observe(viewLifecycleOwner, { followerItems ->
            followerItems?.let {
                if (followerItems.isNotEmpty()) {
                    listFollowerAdapter.setUser(followerItems)
                }
            }
            binding?.rvFollowers?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listFollowerAdapter
            }
        })
    }

    private fun loadingHandling() {
        followerViewModel.loadingState.observe(viewLifecycleOwner, { isLoading ->
            binding?.apply {
                if (isLoading) {
                    rvFollowers.visibility = View.GONE
                    viewFollowerListLoading.root.visibility = View.VISIBLE
                } else {
                    rvFollowers.visibility = View.VISIBLE
                    viewFollowerListLoading.root.visibility = View.GONE
                }
            }
        })
    }

    private fun errorHandling() {
        with(followerViewModel) {
            isError.observe(viewLifecycleOwner, { isError ->
                binding?.apply {
                    if (isError) {
                        viewFollowerListLoading.root.visibility = View.GONE
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