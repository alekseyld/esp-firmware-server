package com.alekseyld.di

import com.alekseyld.db.tables.Node
import com.alekseyld.repository.IFirmwareRepository
import com.alekseyld.repository.IStatRepository
import com.alekseyld.repository.impl.FirebaseRepository
import com.alekseyld.repository.impl.FirmwareRepository
import com.alekseyld.repository.impl.LocalStatRepository
import com.alekseyld.service.IFirmwareService
import com.alekseyld.service.IStatService
import com.alekseyld.service.impl.FirmwareService
import com.alekseyld.service.impl.StatService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val FIREBASE_STAT_REPO = "firebase"
const val LOCAL_STAT_REPO = "local"

inline fun <reified T> inject() = lazy {
        object : KoinComponent {
            val value: T by inject()
        }.value
    }

val appModule = module {

    factory { HttpClient(Apache) }

    single<IFirmwareRepository> { FirmwareRepository() }

    single<IFirmwareService> { FirmwareService(get()) }

    single<IStatRepository>(named(FIREBASE_STAT_REPO)) { FirebaseRepository(get()) }

    single<IStatRepository>(named(LOCAL_STAT_REPO)) { LocalStatRepository() }

    single<IStatService> {
        StatService(
            get(named(FIREBASE_STAT_REPO)),
            get(named(LOCAL_STAT_REPO))
        )
    }

    single { Gson() }

    single<TypeToken<List<Node>>> { object : TypeToken<List<Node>>() {} }

}