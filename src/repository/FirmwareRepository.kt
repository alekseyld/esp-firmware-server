package com.alekseyld.repository

import java.io.File
import java.io.InputStream

class FirmwareRepository : IFirmwareRepository {

    override fun saveFirmwareBin(macAddress: String, inputStream: InputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFirmwareBin(macAddress: String): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}