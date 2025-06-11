package com.smokezone.ui.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smokezone.databinding.ItemPostCommentBinding
import java.io.Serializable

class PostCommentAdapter : RecyclerView.Adapter<PostCommentViewHolder>() {

    private val comments = mutableListOf<Pair<String, String>>()

    fun setComments(comments: List<Pair<String, String>>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViewHolder {
        return PostCommentViewHolder(ItemPostCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PostCommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}

class PostCommentViewHolder(
    val binding: ItemPostCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Pair<String, String>) {
        binding.contentTextView.text = comment.second
    }
}

data class PostComment(
    val author: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
): Serializable