package com.instances.safechat.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.instances.safechat.R
import com.instances.safechat.db.Chat
import com.instances.safechat.utils.Constants.Companion.IMAGE
import com.instances.safechat.utils.Constants.Companion.MESSAGE


class  MessageAdapter(
    var messageList: ArrayList<Chat>,
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
        return if (messageList[position].type == 1) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if (message.form == MESSAGE){
            holder.rlTextMessage.isVisible = true
            holder.cvImage.isVisible = false
            holder.cvVideoDetail.isVisible = false
            holder.tvMessage.text = message.Message
        }else if (message.form == IMAGE){
            holder.rlTextMessage.isVisible = false
            holder.cvImage.isVisible = true
            holder.cvVideoDetail.isVisible = false
            holder.ivMessage.setImageURI(Uri.parse(message.Message))
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMessage(messageList: ArrayList<Chat>){
        this.messageList = messageList
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        var rlTextMessage: RelativeLayout = itemView.findViewById(R.id.rl_text_message)
        var cvImage: CardView = itemView.findViewById(R.id.cv_image)
        var cvVideoDetail: CardView = itemView.findViewById(R.id.cv_document)
        var ivMessage: ImageView = itemView.findViewById(R.id.iv_message)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}