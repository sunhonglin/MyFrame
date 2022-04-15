package com.sunhonglin.base.utils

import androidx.lifecycle.*
import okhttp3.*
import timber.log.Timber


class WebSocketUtil : WebSocketListener() {

    companion object {
        const val ACTIVE_SHUTDOWN = "Active Shutdown"
        const val PASSIVE_SHUTDOWN = "Passive Shutdown"
        private var INST: WebSocketUtil = WebSocketUtil()
        fun getInstance(): WebSocketUtil {
//            if (INST == null) {
//                synchronized(WebSocketUtil::class.java) {
//                    INST = WebSocketUtil(url)
//                    return INST
//                }
//            }
//            if (INST?.wsUrl != url) INST?.connect()
            return INST
        }
    }

    private var webSocket: WebSocket? = null

    private val _status = MutableLiveData<ConnectStatus>()
    val status: LiveData<ConnectStatus>
        get() = _status

    private val _stringMessage = MutableLiveData<String>()
    val stringMessage: LiveData<String>
        get() = _stringMessage

    private val client: OkHttpClient = OkHttpClient.Builder().retryOnConnectionFailure(true)
        .build()

    fun observer(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                close(PASSIVE_SHUTDOWN)
            }
        })
    }

    fun connect(wsUrl: String) {
        //构造request对象
        close(ACTIVE_SHUTDOWN)
        val request = Request.Builder().url(wsUrl).build()
        webSocket = client.newWebSocket(request, this)
        _status.postValue(ConnectStatus.Connecting)
    }

//    fun reConnect() {
//        webSocket?.let {
//            close(ACTIVE_SHUTDOWN)
//            webSocket?.
//            webSocket = client.newWebSocket(it.request(), this)
//            _status.postValue(ConnectStatus.Connecting)
//        }
//    }

    fun send(text: String) {
        Timber.d("send： $text")
        webSocket?.send(text)
    }

    fun cancel() {
        webSocket?.cancel()
    }

    fun close(reason: String) {
        webSocket?.close(1000, reason)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Timber.d("onOpen")
        _status.postValue(ConnectStatus.Open)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Timber.d("onMessage: $text")
        _stringMessage.postValue(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        _status.postValue(ConnectStatus.Closing)
        Timber.d("onClosing $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Timber.d("onClosed $reason")
        _status.postValue(ConnectStatus.Closed)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Timber.d("onFailure: $t")
        t.printStackTrace()
        _status.postValue(ConnectStatus.Canceled)
    }
}

enum class ConnectStatus {
    Connecting,  // the initial state of each web socket.
    Open,  // the web socket has been accepted by the remote peer
    Closing,  // one of the peers on the web socket has initiated a graceful shutdown
    Closed,  //  the web socket has transmitted all of its messages and has received all messages from the peer
    Canceled // the web socket connection failed
}