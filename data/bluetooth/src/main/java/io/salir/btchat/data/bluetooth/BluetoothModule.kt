package io.salir.btchat.data.bluetooth

import android.content.Context
import io.salir.btchat.core.interfaces.bluetooth.TransportRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class BluetoothModule {

    @Single
    fun transportRepository(
        context: Context,
        @Named("AppScope") applicationScope: CoroutineScope
    ): TransportRepository {
        return BluetoothTransportRepository(context, applicationScope)
    }
}
