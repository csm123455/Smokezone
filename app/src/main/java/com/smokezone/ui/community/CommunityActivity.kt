package com.smokezone.ui.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smokezone.data.Post
import com.smokezone.databinding.ActivityCommunityBinding
import com.smokezone.firebase.auth
import com.smokezone.firebase.firebasedb
import com.smokezone.firebase.firestore
import com.smokezone.startNoSmokeLink
import com.smokezone.ui.chat.ChatListItem
import com.smokezone.ui.chat.MessageActivity.Companion.CHILD_CHAT
import com.smokezone.ui.chat.MessageActivity.Companion.DB_USERS

class CommunityActivity : AppCompatActivity() {

    val binding by lazy { ActivityCommunityBinding.inflate(layoutInflater) }

    val userDb = firebasedb.reference.child(DB_USERS)

    val postAdapter = PostAdapter({
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("post", it.first)
        intent.putExtra("id", it.second)
        startActivity(intent)
    }, {
        // 로그인 상태
        if (auth.currentUser?.uid.toString() != it) {
            val chatRoom = ChatListItem(
                roomName = auth.currentUser?.displayName.orEmpty() +"님 과의 채팅방",
                key = System.currentTimeMillis()
            )

            // 채팅방 생성
            userDb.child(auth.currentUser!!.uid)
                .child(CHILD_CHAT)
                .push()
                .setValue(chatRoom)
                .addOnSuccessListener {
                    Toast.makeText(this, "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "채팅방에 실패했습니다..", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "내가 작성한 글입니다.", Toast.LENGTH_SHORT).show()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.postsRecyclerView.adapter = postAdapter

        // 게스물 작성하러 가기 가능
        binding.fab.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java))
        }

        binding.noSmokeImageView.setOnClickListener {
            // 인터넷창 띄우기 외부로
            startNoSmokeLink()
        }
    }

    override fun onStart() {
        super.onStart()

        // 화면이 보여질 때마다 데이터를 가져온다.
        firestore.collection("posts").get().addOnSuccessListener {
            val posts = it.documents

            posts.mapNotNull { data ->
                val post = data.toObject(Post::class.java) ?: return@mapNotNull null
                post to data.id
            }.let {
                postAdapter.setPosts(it)
            }
        }.addOnFailureListener {
            Log.e("CommunityActivity", "게시물을 불러오는데 실패했습니다.", it)
            Toast.makeText(this, "게시물을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}