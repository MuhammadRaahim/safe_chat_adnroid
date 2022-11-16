package com.instances.safechat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.instances.safechat.databinding.ActivityMainBinding
import com.instances.safechat.utils.Constants.Companion.SPLASH_TIME
import com.instances.safechat.utils.PrefManager


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLoginInfo()
    }

    private fun checkLoginInfo(){
        Handler(Looper.getMainLooper()).postDelayed({
            manager = PrefManager(this)
            val intent = if (manager.session.isNotEmpty()){
                Intent(this, InboxActivity::class.java)
            }else{
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        },SPLASH_TIME)
    }



}