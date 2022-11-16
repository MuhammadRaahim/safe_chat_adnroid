package com.instances.safechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.instances.safechat.R
import com.instances.safechat.adapter.MessageAdapter
import com.instances.safechat.databinding.ActivityInboxBinding
import com.instances.safechat.db.Database
import com.instances.safechat.db.UserDoa
import com.instances.safechat.utils.BaseUtils
import com.instances.safechat.utils.PrefManager

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding
    private lateinit var manager: PrefManager
    private lateinit var userDoa: UserDoa


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setOnClickListeners()
    }

    private fun initViews() {
        manager = PrefManager(this)
        userDoa = Database.getDatabase(this).userDao()
    }

    private fun setOnClickListeners() {
        binding.apply {
            cvMessage.setOnClickListener {
                startActivity(Intent(this@InboxActivity,ChatActivity::class.java))
            }
            cvClear.setOnClickListener {
                clearChat()
            }
            btnLogout.setOnClickListener {
                manager.logout()
                startActivity(Intent(this@InboxActivity,LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun clearChat() {
        var user = userDoa.getUser(manager.session)[0]
        user.chatList = null
        userDoa.updateUser(user)
        BaseUtils.showMessage(binding.root, "Chat Clear")
    }


}