package com.instances.safechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.instances.safechat.R
import com.instances.safechat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()

    }

    private fun setClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity,InboxActivity::class.java))
            }
            tvSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
            }
        }
    }
}