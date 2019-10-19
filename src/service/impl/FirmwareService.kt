package com.alekseyld.service.impl

import com.alekseyld.model.EspHeaders
import com.alekseyld.repository.IFirmwareRepository
import com.alekseyld.service.IFirmwareService
import com.alekseyld.service.IFirmwareService.Result.AlreadyUpdated
import com.alekseyld.service.IFirmwareService.Result.RequiredUpdate
import com.alekseyld.utils.md5

class FirmwareService(
    private val repository: IFirmwareRepository
) : IFirmwareService {

    override fun processUpdateRequest(espHeaders: EspHeaders): IFirmwareService.Result {

        val firmwareFile = repository.getFirmwareBin(espHeaders.staMac)

        val localHash = firmwareFile?.md5()

        return if (espHeaders.sketchMd5 != localHash) {

            RequiredUpdate(firmwareFile!!, localHash!!)
        } else {

            AlreadyUpdated
        }
    }
}