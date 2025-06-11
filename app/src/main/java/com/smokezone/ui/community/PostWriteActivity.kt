package com.smokezone.ui.community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smokezone.databinding.ActivityPostWriteBinding
import com.smokezone.firebase.auth
import com.smokezone.firebase.firestore

class PostWriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityPostWriteBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 게시물 작성 버튼
        binding.saveButton.setOnClickListener {
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()

            val post = hashMapOf(
                "title" to title,
                "content" to content,
                "author" to auth.currentUser?.displayName.orEmpty(),
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("posts").add(post)
                .addOnSuccessListener {
                    Toast.makeText(this, "작성완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}