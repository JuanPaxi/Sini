package com.juanpaxi.sini.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(
    val siniDispatcher: SiniDispatchers,
)

enum class SiniDispatchers {
    Default,
    IO,
}
