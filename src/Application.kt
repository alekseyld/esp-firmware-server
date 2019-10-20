package com.alekseyld

import com.alekseyld.controller.firmwareController
import com.alekseyld.controller.rootController
import com.alekseyld.controller.statController
import com.alekseyld.db.DatabaseFactory
import com.alekseyld.di.appModule
import com.alekseyld.di.inject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.Application
import io.ktor.routing.routing
import org.koin.core.context.startKoin
import java.io.File

object AppConfiguration{
    lateinit var firebaseUrl: String
    lateinit var authToken: String
    lateinit var node: String
}

fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val gson by inject<Gson>()

    if (!testing) {
        startKoin {
            modules(appModule)
        }

        DatabaseFactory.init()


        val config = gson.fromJson<Map<String, String>>(
            File("./credentials").readText(),
            object : TypeToken<Map<String, String>>() {}.type
        )

        AppConfiguration.firebaseUrl = config.getOrDefault("firebaseUrl", "")
        AppConfiguration.authToken = config.getOrDefault("authToken", "")
        AppConfiguration.node = config.getOrDefault("node", "")
    }

    routing {

        rootController()
        firmwareController()
        statController()
    }
}


