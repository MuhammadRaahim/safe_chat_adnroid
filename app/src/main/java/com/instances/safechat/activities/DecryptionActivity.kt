package com.instances.safechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.instances.safechat.databinding.ActivityDecryptionBinding

class DecryptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDecryptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDecryptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}