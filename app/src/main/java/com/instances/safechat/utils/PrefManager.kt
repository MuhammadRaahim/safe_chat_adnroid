package com.instances.safechat.utils

import android.content.Context
import android.content.SharedPreferences
import com.instances.safechat.utils.Constants
import com.instances.safechat.utils.Constants.Companion.FCM_TOKEN

class PrefManager( var context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(
        Constants.PREF_NAME,
        Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()


    var fcmToken:String
        get() = pref.getString(FCM_TOKEN, "")!!
        set(value) {
            editor.putString(FCM_TOKEN,value)
            editor.apply()
            editor.commit()
        }

}