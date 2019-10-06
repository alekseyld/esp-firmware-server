package com.alekseyld.repository

import java.io.File
import java.io.InputStream

class FirmwareRepository : IFirmwareRepository {

    override fun saveFirmwareBin(macAddress: String, inputStream: InputStream) {

        val file = File("firmwares/${macAddress.fileName()}")

        file.delete()
        file.createNewFile()

        inputStream.use { input ->
            file.outputStream().buffered().use {
                input.copyTo(it)
            }
        }
    }

    override fun getFirmwareBin(macAddress: String): File? {

        val file = File("firmwares/${macAddress.fileName()}")

        return if (file.exists()) file
        else null
    }

    private fun String.fileName() : String {
        return replace(":", "-")
    }
}