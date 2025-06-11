package com.smokezone.ui.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smokezone.data.Post
import com.smokezone.databinding.ItemPostBinding
import java.text.SimpleDateFormat
import java.util.Date

class PostAdapter(
    val onClick: (Pair<Post, String>) -> Unit,
    val onClickMessage: (String) -> Unit
) : RecyclerView.Adapter<PostViewHolder>() {

    private val posts = mutableListOf<Pair<Post, String>>()

    fun setPosts(posts: List<Pair<Post, String>>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onClick, onClickMessage)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

class PostViewHolder(
    val binding: ItemPostBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Pair<Post,String>,
             onClick: (Pair<Post,String>) -> Unit,
             onClickMessage: (String) -> Unit) {
        binding.root.setOnClickListener {
            onClick(post)
        }

        binding.messageButton.setOnClickListener {
            onClickMessage(post.first.author)
        }

        binding.titleTextView.text = post.first.title
        binding.timeTextView.text = convertLongToTime(post.first.timestamp)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}
