package com.chkan.bestpractices.websockets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentWebsocketsBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebsocketsFragment : BaseFragment<FragmentWebsocketsBinding>(FragmentWebsocketsBinding::inflate) {

    private val viewModel: WebsocketsViewModel by viewModels()

    private lateinit var webSocketListener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSocketListener = WSListener(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.socketStatus.observe(viewLifecycleOwner) {
            binding.statusTV.text = if (it) "Connected" else "Disconnected"
        }

        var text = ""
        viewModel.messages.observe(viewLifecycleOwner) {
            text += "${if (it.first) "You: " else "Other: "} ${it.second}\n"

            binding.messageTV.text = text
        }

        binding.btnConnect.setOnClickListener {
            webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        }

        binding.btnDisconnect.setOnClickListener {
            webSocket?.close(1000, "Canceled manually.")
        }

        binding.btnSend.setOnClickListener {
            webSocket?.send(binding.etMessage.text.toString())
            viewModel.addMessage(Pair(true, binding.etMessage.text.toString()))
        }
    }

    private fun createRequest(): Request {
        val websocketURL = "ws://10.0.2.2:8080/chat" //address for emulator

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        okHttpClient.dispatcher.executorService.shutdown()
    }

}