package com.smokezone.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.smokezone.databinding.ActivityMessageBinding
import com.smokezone.firebase.auth
import com.smokezone.firebase.firebasedb

class MessageActivity : AppCompatActivity() {

    val binding by lazy { ActivityMessageBinding.inflate(layoutInflater) }

    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("chatKey", chatRoom.key)
            startActivity(intent)
        })

        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(this)

        chatRoomList.clear()

        if (auth.currentUser == null) {
            return
        }

        val chatDb = firebasedb.reference.child(DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)

        // 채팅방 목록을 구독한다.
        chatDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }

    companion object {
        const val DB_USERS = "Users"
        const val CHILD_CHAT = "chat"
    }
}