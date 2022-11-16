package com.instances.safechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.instances.safechat.adapter.MessageAdapter
import com.instances.safechat.databinding.ActivityChatBinding
import com.instances.safechat.db.Chat
import com.instances.safechat.db.Database
import com.instances.safechat.db.UserDoa
import com.instances.safechat.db.UserEntity
import com.instances.safechat.utils.BaseUtils.Companion.decrypt
import com.instances.safechat.utils.BaseUtils.Companion.encrypt
import com.instances.safechat.utils.BaseUtils.Companion.fromGsonToJson
import com.instances.safechat.utils.BaseUtils.Companion.hideKeyboard
import com.instances.safechat.utils.BaseUtils.Companion.jsonToGson
import com.instances.safechat.utils.PrefManager

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var chatList: ArrayList<Chat>
    private lateinit var manager: PrefManager
    private lateinit var userDoa: UserDoa
    private lateinit var user: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setMessageAdapter()
        setOnClickListener()
        getChatList()
    }

    private fun initViews() {
        chatList = ArrayList()
        manager = PrefManager(this)
        userDoa = Database.getDatabase(this).userDao()
    }

    private fun getChatList() {
        user = userDoa.getUser(manager.session)[0]
            if (user.chatList != null){
                binding.tvNoMessage.isVisible = false
                binding.rvMessages.isVisible = true
                    // decrypt the json string and convert to Message list
                chatList = jsonToGson(decrypt(user.chatList))!!
                adapter.updateMessage(chatList)
            }else{
                binding.tvNoMessage.isVisible = true
                binding.rvMessages.isVisible = false
            }
    }

    private fun setOnClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                saveChat()
                onBackPressed()
            }
            ivSend.setOnClickListener {
                if (etMessage.text.trim().isNotEmpty()){
                    sendMessage()
                }
            }
        }
    }

    private fun sendMessage() {
        hideKeyboard()
        binding.tvNoMessage.isVisible = false
        binding.rvMessages.isVisible = true
        val message = Chat(type = 1,binding.etMessage.text.toString().trim())
            // encrypt the message and send in reply
        val encryptedReply = Chat(type = 0, encrypt(binding.etMessage.text.toString().trim())!!)
        chatList.add(message)
        chatList.add(encryptedReply)
        adapter.updateMessage(chatList)
        binding.etMessage.setText("")
        binding.rvMessages.scrollToPosition(chatList.size-1)
    }

    private fun setMessageAdapter() {
        adapter = MessageAdapter(messageList = chatList)
        layoutManager = LinearLayoutManager(this@ChatActivity)
        layoutManager.stackFromEnd = true
        binding.rvMessages.layoutManager = layoutManager
        binding.rvMessages.setHasFixedSize(true)
        binding.rvMessages.adapter = adapter
    }

    private fun saveChat(){
        if (chatList.isNotEmpty()) {
            // convert chat list into json string
            val jsonChatList = fromGsonToJson(chatList)
            // encrypt the json string
            var encryptedChatList = encrypt(jsonChatList!!)
            // save chat into db
            user.chatList = encryptedChatList
            userDoa.updateUser(user)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveChat()
    }

    override fun onPause() {
        super.onPause()
        saveChat()
    }
}