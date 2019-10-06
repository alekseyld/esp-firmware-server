package com.alekseyld.service

import com.alekseyld.model.EspHeaders
import java.io.File

interface IFirmwareService {

    sealed class Result {
        data class RequiredUpdate(
            val updatedFirmware: File,
            val md5Hash: String
        ) : Result()

        object AlreadyUpdated: Result()
    }

    fun processUpdateRequest(espHeaders: EspHeaders) : Result

}