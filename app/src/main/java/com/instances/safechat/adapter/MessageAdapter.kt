package com.instances.safechat.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.instances.safechat.R
import com.instances.safechat.db.Chat


class MessageAdapter(
//    var messageList: ArrayList<Chat>,
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_right_message, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_left_message, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 5
    }





    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var tvMessage: TextView = itemView.findViewById(R.id.tv_message)
//        var rlTextMessage: RelativeLayout = itemView.findViewById(R.id.rl_text_message)
    }
}