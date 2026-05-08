package io.salir.btchat.core.security.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.salir.btchat.core.security.api.DeviceIdentifier
import io.salir.btchat.core.security.SimpleIdGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SimpleDeviceIdentifier(
    private val prefs: DataStore<Preferences>
) : DeviceIdentifier {

    override suspend fun me(): String {
        return prefs.data.map {
            it[ID_KEY] ?: SimpleIdGenerator.generate(ID_LENGTH)
        }.first()
    }

    companion object {
        const val ID_LENGTH = 16
        val ID_KEY = stringPreferencesKey("my_id")
    }
}