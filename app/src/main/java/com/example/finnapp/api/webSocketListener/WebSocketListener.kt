package com.example.finnapp.api.webSocketListener

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener(
    private val sendSymbol:List<String>
):WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sendSymbol.forEach {
            webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"$it\"}")
            SocketListenerUtil.mWebSocket = webSocket
            SocketListenerUtil.sendRepeatHeartMessage()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocketListener:", "onClosed: $code / $reason")
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000000L)
            SocketListenerUtil.connect()
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocketListener:", "onClosing: $code / $reason")
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000000L)
            SocketListenerUtil.connect()
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocketListener:", "onMessage: $text")
        SocketListenerUtil.mResponseStockPriceQuote.value = text
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("WebSocketListener:", "onFailure:" + t.message)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000000L)
            SocketListenerUtil.connect()
        }
    }

}