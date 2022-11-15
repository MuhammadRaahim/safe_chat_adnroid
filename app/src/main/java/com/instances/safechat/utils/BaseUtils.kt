package com.instances.safechat.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class BaseUtils {

    companion object{

        fun openUrl(url: String, context: Context){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }

    }
}