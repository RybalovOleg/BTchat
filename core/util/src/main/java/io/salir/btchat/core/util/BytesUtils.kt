package io.salir.btchat.core.util

fun Long.toByteArray(): ByteArray = ByteArray(8) { (this shr ((7 - it) * 8)).toByte() }