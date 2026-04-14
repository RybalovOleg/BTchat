package io.salir.btchat.data.bluetooth

import io.salir.btchat.core.model.bluetooth.DeviceInfo

data class DeviceInfoImpl(
    val macAdress: String,
    override val name: String
) : DeviceInfo