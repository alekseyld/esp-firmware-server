package com.alekseyld.di

import com.alekseyld.repository.FirmwareRepository
import com.alekseyld.repository.IFirmwareRepository
import com.alekseyld.service.FirmwareService
import com.alekseyld.service.IFirmwareService
import org.koin.dsl.module

val appModule = module {

    single<IFirmwareRepository> { FirmwareRepository() }

    single<IFirmwareService> { FirmwareService(get()) }

}