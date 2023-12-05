package com.example.cbink

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cbink.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivity: ActivityMainBinding
    private lateinit var messageList: ArrayList<MessageClass>
    private val user = SenderType.USER
    private val bot = SenderType.BOT

    private val rasaService: RasaService by lazy {
        Retrofit.Builder()
            .baseUrl("https://1e12-46-198-171-223.ngrok.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RasaService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivity.root)

        messageList = ArrayList<MessageClass>()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mainActivity.messageList.layoutManager = linearLayoutManager

        val adapter = MessageAdapter(this, messageList)
        mainActivity.messageList.adapter = adapter

        mainActivity.sendButton.setOnClickListener {
            val msg = mainActivity.messageBox.text.toString()
            sendMessageToRasa(msg, adapter)
        }
    }

    private fun sendMessageToRasa(message: String, adapter: MessageAdapter) {
        if (message.isEmpty()) {
            Toast.makeText(this, "Please type your message", Toast.LENGTH_SHORT).show()
        } else {
            // Add the user's message to the list
            val userMessage = MessageClass(message, user)
            messageList.add(userMessage)
            adapter.notifyItemInserted(messageList.size - 1)

            // Send the user's message to Rasa
            val messageRequest = MessageRequest("user", message)
            val call = rasaService.sendMessage(messageRequest)

            call.enqueue(object : Callback<List<MessageResponse>> {
                override fun onResponse(
                    call: Call<List<MessageResponse>>,
                    response: Response<List<MessageResponse>>
                ) {
                    if (response.isSuccessful) {
                        // Add the bot response to the list
                        response.body()?.let { messages ->
                            if (messages.isNotEmpty()) {
                                val botMessageObject = MessageClass(messages[0].text, bot)
                                messageList.add(botMessageObject)
                                adapter.notifyItemInserted(messageList.size - 1)

                                // Scroll the RecyclerView to the last item to show the latest messages
                                mainActivity.messageList.scrollToPosition(messageList.size - 1)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<MessageResponse>>, t: Throwable) {
                    // Handle failure (e.g., show an error message)
                    Toast.makeText(
                        this@MainActivity,
                        "Failed Rasa communication",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            // Clear the message box
            mainActivity.messageBox.setText("")
        }
    }
}
