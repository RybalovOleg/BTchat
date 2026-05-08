package io.salir.btchat.data.bluetooth

import android.content.Context
import io.salir.btchat.core.security.api.DeviceIdentifier
import io.salir.btchat.core.interfaces.TransportRepository
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.security.api.MessageSerializer
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class BluetoothModule {

    @Single
    fun transportRepository(
        context: Context,
        @Named("AppScope") applicationScope: CoroutineScope,
        deviceIdentifier: DeviceIdentifier,
        messageSerializer: MessageSerializer,
        messageIdGenerator: MessageIdGenerator
    ): TransportRepository {
        return BluetoothTransportRepository(
            context,
            applicationScope,
            deviceIdentifier,
            messageSerializer,
            messageIdGenerator
        )
    }
}
