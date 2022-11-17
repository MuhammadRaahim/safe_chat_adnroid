package com.instances.safechat.activities

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.instances.safechat.R
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
import com.instances.safechat.utils.Constants.Companion.IMAGE
import com.instances.safechat.utils.Constants.Companion.MESSAGE
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
            ivSelectImage.setOnClickListener {
                openFileChooseDialog()
            }
        }
    }

    private fun openFileChooseDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_select_doc, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val pictureCv = dialogView.findViewById<MaterialButton>(R.id.picture_cv)
        val videoCvs = dialogView.findViewById<MaterialButton>(R.id.doc_cv)

        pictureCv.setOnClickListener {
            pickImage()
            alertDialog.dismiss()
        }
    }

    private fun pickImage() {
       launchGalleryIntent()
    }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this.findViewById(android.R.id.content))
            }.show()
        } else {
            snackbar.show()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                getImageFromGallery.launch(arrayOf("image/*"))
            } else {
                Log.i("Permission: ", "Denied")
                Toast.makeText(this,getString(R.string.permission_required)
                    .plus(". Please enable it settings"), Toast.LENGTH_SHORT).show()
            }
        }

    private fun launchGalleryIntent() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getImageFromGallery.launch(arrayOf("image/*"))
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showSnackBar(
                    binding.root,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.str_ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        sendPicture(uri)
    }

    private fun sendPicture(uri: Uri?) {
        hideKeyboard()
        binding.tvNoMessage.isVisible = false
        binding.rvMessages.isVisible = true
        val message = Chat(type = 1, form = IMAGE,uri.toString())
        // encrypt the message and send in reply
        val encryptedReply = Chat(type = 0, form = MESSAGE,encrypt(uri.toString())!!)
        chatList.add(message)
        chatList.add(encryptedReply)
        adapter.updateMessage(chatList)
        binding.etMessage.setText("")
        binding.rvMessages.scrollToPosition(chatList.size-1)
    }

    private fun sendMessage() {
        hideKeyboard()
        binding.tvNoMessage.isVisible = false
        binding.rvMessages.isVisible = true
        val message = Chat(type = 1, form = MESSAGE,binding.etMessage.text.toString().trim())
            // encrypt the message and send in reply
        val encryptedReply = Chat(type = 0, form = MESSAGE,encrypt(binding.etMessage.text.toString().trim())!!)
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