package io.salir.btchat.di

import io.salir.btchat.core.interfaces.TransportRepository
import io.salir.btchat.domain.bluetooth.SessionManager
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class DomainModule {

    @Single
    fun sessionManager(
        transportRepository: TransportRepository,
        @Named("AppScope") applicationScope: CoroutineScope
    ): SessionManager {
        return SessionManager(transportRepository, applicationScope)
    }
}
