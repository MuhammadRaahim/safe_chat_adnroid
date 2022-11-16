package com.instances.safechat.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.instances.safechat.R
import com.instances.safechat.databinding.ActivityLoginBinding
import com.instances.safechat.databinding.ActivitySignupBinding
import com.instances.safechat.db.Database
import com.instances.safechat.db.UserDoa
import com.instances.safechat.db.UserEntity
import com.instances.safechat.utils.BaseUtils
import com.instances.safechat.utils.BaseUtils.Companion.decrypt
import com.instances.safechat.utils.BaseUtils.Companion.encrypt
import com.instances.safechat.utils.BaseUtils.Companion.hideKeyboard
import com.instances.safechat.utils.BaseUtils.Companion.setDialogue
import com.instances.safechat.utils.PrefManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDoa: UserDoa
    private lateinit var manager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()

    }

    private fun initViews() {
        userDoa = Database.getDatabase(this).userDao()
        manager = PrefManager(this)
    }

    private fun setClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                hideKeyboard()
                validateFields()
            }
            tvSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
            }
        }
    }

    private fun ActivityLoginBinding.validateFields() {
       if (!BaseUtils.isValidEmail(etMailLogin.text.toString().trim())){
            BaseUtils.showMessage(binding.root, "Invalid Email", true)
        }else if (etPasswordLogin.text.toString().trim().length < 3){
            BaseUtils.showMessage(binding.root, "Invalid Password", true)
        }else{
            checkUserInfo()
        }
    }

    private fun ActivityLoginBinding.checkUserInfo() {

        val email = etMailLogin.text.toString().trim()
        val password = etPasswordLogin.text.toString().trim()
        // checking user already exist with encrypted email
        val user = userDoa.getUser(encrypt(email)!!)
        if (user.isNotEmpty()){
            if (decrypt(user[0].email) != email){
                BaseUtils.showMessage(binding.root, "Email is Incorrect", true)
            }else if(decrypt(user[0].password) != password){
                BaseUtils.showMessage(binding.root, "Password is Incorrect", true)
            }else{
                manager.session = user[0].email
                startActivity(Intent(this@LoginActivity,InboxActivity::class.java))
                Toast.makeText(this@LoginActivity,"Login Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            BaseUtils.showMessage(binding.root, "User doesn't Exist", true)
        }

    }
}