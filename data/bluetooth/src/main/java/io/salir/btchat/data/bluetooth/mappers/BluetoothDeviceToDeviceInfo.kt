package io.salir.btchat.data.bluetooth.mappers

import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import io.salir.btchat.core.model.connection.DeviceInfo

@RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
internal fun BluetoothDevice.toDeviceInfo(): DeviceInfo = DeviceInfo(id = address, name = name)