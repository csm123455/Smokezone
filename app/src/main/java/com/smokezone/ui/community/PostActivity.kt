package com.smokezone.ui.community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.smokezone.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {

    val binding by lazy { ActivityPostBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getStringExtra("id") ?: ""

        val adapter = PostCommentAdapter()

        binding.commentsRecyclerView.adapter = adapter

        val firestore = Firebase.firestore

        // 게시물들을 가져온다.
        getPost(firestore, id, adapter)

        // 댓글 작성 기능
        binding.writeBtn.setOnClickListener {

            val postRef = firestore.collection("posts").document(id)

            val comment = mapOf(
                "author" to Firebase.auth.currentUser?.displayName.orEmpty(),
                "content" to binding.commentEditText.text.toString(),
                "timestamp" to System.currentTimeMillis() // 댓글 작성 시간 추가 (선택 사항)
            )

            firestore.runTransaction { transaction ->
                // 현재 포스트 문서를 가져옴
                val snapshot = transaction.get(postRef)
                // 현재 comments 배열을 가져옴
                val currentComments =
                    snapshot.get("comments") as? MutableList<Map<String, Any>> ?: mutableListOf()
                // 새 댓글을 comments 배열에 추가
                currentComments.add(comment)
                // comments 필드를 업데이트함
                transaction.update(postRef, "comments", currentComments)
            }.addOnSuccessListener {
                Toast.makeText(this, "댓글이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                binding.commentEditText.text.clear()
                getPost(firestore, id, adapter)
            }.addOnFailureListener { e ->
                Toast.makeText(this, "댓글 작성 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getPost(
        firestore: FirebaseFirestore,
        id: String,
        adapter: PostCommentAdapter,
    ) {
        firestore.collection("posts").document(id).get().addOnSuccessListener {
            binding.titleTextView.text = it.getString("title")
            binding.contentTextView.text = it.getString("content")

            val comments = it.get("comments") as? List<Map<String, Any>> ?: emptyList()
            val commentList = comments.mapNotNull { comment ->
                val author = comment["author"] as? String ?: return@mapNotNull null
                val content = comment["content"] as? String ?: return@mapNotNull null
                author to content
            }
            adapter.setComments(commentList)
        }
    }
}