package com.alekseyld.di

import com.alekseyld.db.tables.Node
import com.alekseyld.repository.IFirmwareRepository
import com.alekseyld.repository.IStatRepository
import com.alekseyld.repository.impl.FirmwareRepository
import com.alekseyld.repository.impl.StatRepository
import com.alekseyld.service.IFirmwareService
import com.alekseyld.service.IStatService
import com.alekseyld.service.impl.FirmwareService
import com.alekseyld.service.impl.StatService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.dsl.module

val appModule = module {

    single<IFirmwareRepository> { FirmwareRepository() }

    single<IFirmwareService> { FirmwareService(get()) }

    single<IStatRepository> { StatRepository() }

    single<IStatService> { StatService(get()) }

    single { Gson() }

    single<TypeToken<List<Node>>> { object : TypeToken<List<Node>>() {} }

}