package io.salir.btchat

import android.app.Application
import io.salir.btchat.data.bluetooth.BluetoothModule
import io.salir.btchat.di.DomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

@KoinApplication
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            modules(
                DomainModule().module,
                BluetoothModule().module
            )
        }
    }
}