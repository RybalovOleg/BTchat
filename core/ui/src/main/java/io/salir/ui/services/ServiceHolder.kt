package io.salir.ui.services

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import io.salir.btchat.core.common.Progress
import io.salir.btchat.core.common.Result
import io.salir.btchat.core.common.SimpleResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


interface ServiceHolder<T : Service> {

    fun startAndBind()
    fun stop()

    companion object {
        inline fun <reified T : Service> create(
            context: Context,
            noinline getService: (IBinder) -> T
        ): ServiceHolder<T> = ServiceHolderImpl(context, T::class.java, getService)
    }
}

class ServiceHolderImpl<T : Service>(
    private val context: Context,
    private val clazz: Class<T>,
    getService: (IBinder) -> T
) : ServiceHolder<T> {

    private val _service = MutableStateFlow<SimpleResult<T>>(Result.Empty)
    val service = _service.asStateFlow()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            _service.value = Result.Success(getService(binder))
        }

        override fun onServiceDisconnected(p0: ComponentName) {
            _service.value = Result.Empty
        }
    }

    override fun startAndBind() {
        if (_service.value is Result.Empty) {
            _service.value = Result.Loading(Progress.Unspecified)
            val intent = Intent(context, clazz)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun stop() {
        context.unbindService(connection)
        context.stopService(Intent(context, clazz))
    }
}