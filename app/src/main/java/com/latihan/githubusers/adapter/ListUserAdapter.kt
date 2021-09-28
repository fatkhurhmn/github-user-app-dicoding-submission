package com.latihan.githubusers.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.latihan.githubusers.R
import com.latihan.githubusers.data.UserItems
import com.latihan.githubusers.databinding.ItemUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private val users = ArrayList<UserItems>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUser(items: ArrayList<UserItems>) {
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListUserAdapter.ListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(userItems: UserItems) {
            with(itemView) {
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_user_default)
                    .fitCenter()
                Glide.with(this).load(userItems.photo).apply(requestOptions)
                    .into(binding.itemUserImgAvatar)
                binding.itemUserTvUsername.text = userItems.username
                binding.itemUserTvId.text = String.format("Id : %d", userItems.id)

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(userItems)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearListUsers() {
        users.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserItems)
    }
}