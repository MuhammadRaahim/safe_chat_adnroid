package com.instances.safechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.instances.safechat.R
import com.instances.safechat.adapter.MessageAdapter
import com.instances.safechat.databinding.ActivityInboxBinding

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            cvMessage.setOnClickListener {
                startActivity(Intent(this@InboxActivity,ChatActivity::class.java))
            }
            btnLogout.setOnClickListener {
                startActivity(Intent(this@InboxActivity,LoginActivity::class.java))
                finish()
            }
        }
    }


}