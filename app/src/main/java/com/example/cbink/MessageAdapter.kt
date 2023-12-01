package com.example.cbink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val context: Context, val messageList: ArrayList<MessageClass>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_text_list, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        if (message.sender == SenderType.USER) {
            val userMessage = getUserLayout()
            holder.linear_layout.addView(userMessage)
            val messageTextView = userMessage?.findViewById<TextView>(R.id.message_user)
            messageTextView?.text = message.message
        } else if (message.sender == SenderType.BOT) {
            val botMessage = getBotLayout()
            holder.linear_layout.addView(botMessage)
            val messageTextView = botMessage?.findViewById<TextView>(R.id.message_bot)
            messageTextView?.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun getUserLayout(): FrameLayout? {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.user_message_box, null) as FrameLayout
    }

    fun getBotLayout(): FrameLayout? {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.bot_message_box, null) as FrameLayout
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val linear_layout = view.findViewById<LinearLayout>(R.id.linear_layout)
    }
}
