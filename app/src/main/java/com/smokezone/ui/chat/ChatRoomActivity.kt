package com.smokezone.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.smokezone.databinding.ActivityChatRoomBinding
import com.smokezone.firebase.auth
import com.smokezone.firebase.firebasedb

const val DB_CHAT = "Chats"

class ChatRoomActivity : AppCompatActivity() {

    val binding by lazy { ActivityChatRoomBinding.inflate(layoutInflater) }

    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()

    private var chatDb: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val chatKey = intent.getLongExtra("chatKey", -1)
        chatDb = firebasedb.reference.child(DB_CHAT).child("$chatKey")

        // 채팅방 내용을 구독한다.
        chatDb?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        // 채팅을 보낸다.
        binding.sendButton.setOnClickListener {
            val chatItem = ChatItem(
                senderId = auth.currentUser?.displayName.orEmpty(),
                message = binding.messageEditText.text.toString()
            )

            chatDb?.push()?.setValue(chatItem)

            binding.messageEditText.text.clear()
        }

    }
}