package com.example.finnapp.api.webSocketListener

import android.text.TextUtils
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

object SocketListenerUtil{

    private var mDisposable: Disposable? = null

    private var mPostPayload: String = ""

    var mResponseStockPriceQuote = MutableStateFlow("")

    var mWebSocket: WebSocket? = null

    fun connect(
        sendSymbol:List<String> = listOf()
    ) {
        val listener = WebSocketListener(
            sendSymbol = sendSymbol
        )
        val request = Request.Builder()
            .url("wss://ws.finnhub.io?token=c8krti2ad3ibbdm43200")
            .build()
        val client = OkHttpClient()
        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    fun sendRepeatHeartMessage() {
        if (mDisposable != null) {
            mDisposable?.dispose()
            mDisposable = null
        }
        mDisposable = Observable.interval(0, 3, TimeUnit.SECONDS)
            .subscribe {
                if (TextUtils.equals(mPostPayload, mResponseStockPriceQuote.value)) {
                    mPostPayload = System.currentTimeMillis().toString()
                    mPostPayload = System.currentTimeMillis().toString()
                    sendMessageDetail(mPostPayload)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1000000L)
                        connect()
                    }
                }
            }
    }


    fun clear() {
        mPostPayload = ""
        mResponseStockPriceQuote.value = ""
        if (mDisposable != null) {
            mDisposable?.dispose()
            mDisposable = null
        }
        if (mWebSocket != null) {
            mWebSocket?.cancel()
            mWebSocket = null
        }
    }

    private fun sendMessageDetail(mPostPayload: String) {
        if (mWebSocket != null) {
            Log.d ("WebSocketListener", "Сообщение отправлено: $mPostPayload")
            mWebSocket?.send(mPostPayload)
        }
    }

}
