package io.salir.btchat.core.security

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import io.salir.btchat.core.security.api.DeviceIdentifier
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.security.api.MessageSerializer
import io.salir.btchat.core.security.impl.JsonMessageSerializer
import io.salir.btchat.core.security.impl.SimpleDeviceIdentifier
import io.salir.btchat.core.security.impl.SimpleMessageIdGenerator
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

private val Context.deviceIdentifierImplDataStore by preferencesDataStore(":core:security:DeviceIdentifierImpl")

@Module
class SecurityModule {

    @Single
    fun deviceIdentifier(context: Context): DeviceIdentifier =
        SimpleDeviceIdentifier(context.deviceIdentifierImplDataStore)

    @Single
    fun messageSerializer(): MessageSerializer = JsonMessageSerializer()

    @Single
    fun messageIdGenerator(): MessageIdGenerator = SimpleMessageIdGenerator()
}