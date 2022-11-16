package com.instances.safechat.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.instances.safechat.R
import com.instances.safechat.databinding.ActivitySignupBinding
import com.instances.safechat.db.Database
import com.instances.safechat.db.UserDoa
import com.instances.safechat.db.UserEntity
import com.instances.safechat.utils.BaseUtils.Companion.decrypt
import com.instances.safechat.utils.BaseUtils.Companion.encrypt
import com.instances.safechat.utils.BaseUtils.Companion.hideKeyboard
import com.instances.safechat.utils.BaseUtils.Companion.isValidEmail
import com.instances.safechat.utils.BaseUtils.Companion.setDialogue
import com.instances.safechat.utils.BaseUtils.Companion.showMessage

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var userDoa: UserDoa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
    }

    private fun initViews() {
        userDoa = Database.getDatabase(this).userDao()
    }

    private fun setClickListeners() {
        binding.apply {
            tvSignin.setOnClickListener {
                onBackPressed()
            }
            btnSignup.setOnClickListener {
                hideKeyboard()
                validateFields()
            }
        }
    }

    private fun ActivitySignupBinding.validateFields() {
        if (etName.text.toString().trim().isEmpty()){
            showMessage(binding.root,"Invalid Username",true)
        }else if (!isValidEmail(etMail.text.toString().trim())){
            showMessage(binding.root,"Invalid Email",true)
        }else if (etPassword.text.toString().trim().length < 3){
            showMessage(binding.root,"Invalid Password",true)
        }else{
            getData()
        }
    }

    private fun ActivitySignupBinding.getData() {
        // encrypt data with AES or CBC algo
        val username = encrypt(etName.text.toString().trim())
        val email = encrypt(etMail.text.toString().trim())
        val password = encrypt(etPassword.text.toString().trim())

        val usersTable = UserEntity(
            userName = username!!,
            email = email!!,
            password = password!!,
            chatList = null)
        // checking user already exist
        val user = userDoa.getUser(email)

        if (user.isEmpty()){
            userDoa.addUser(usersTable)
            Toast.makeText(this@SignupActivity,"Signup successfully",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            showMessage(binding.root,"User Already Exist",true)
        }

    }


}