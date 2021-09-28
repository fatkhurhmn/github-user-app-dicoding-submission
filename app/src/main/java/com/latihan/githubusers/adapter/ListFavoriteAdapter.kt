package com.latihan.githubusers.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.latihan.githubusers.R
import com.latihan.githubusers.database.Favorite
import com.latihan.githubusers.databinding.ItemUserBinding

class ListFavoriteAdapter : RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>() {

    private val users = ArrayList<Favorite>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListFavorite(items: List<Favorite>) {
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFavoriteAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFavoriteAdapter.ListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(favorite: Favorite) {
            with(itemView) {
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_user_default)
                    .fitCenter()
                Glide.with(this).load(favorite.photo).apply(requestOptions)
                    .into(binding.itemUserImgAvatar)
                binding.itemUserTvUsername.text = favorite.username
                binding.itemUserTvId.text = String.format("Id : %d", favorite.id)

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(favorite)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favorite: Favorite)
    }
}