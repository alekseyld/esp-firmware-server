package com.alekseyld

import com.alekseyld.controller.firmwareController
import com.alekseyld.controller.rootController
import com.alekseyld.controller.statController
import com.alekseyld.db.DatabaseFactory
import com.alekseyld.di.appModule
import io.ktor.application.Application
import io.ktor.routing.routing
import org.koin.core.context.startKoin


fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
//    val client = HttpClient(Apache) {}

    if (!testing) {
        startKoin {
            modules(appModule)
        }

        DatabaseFactory.init()
    }

    routing {

        rootController()
        firmwareController()
        statController()
    }
}


