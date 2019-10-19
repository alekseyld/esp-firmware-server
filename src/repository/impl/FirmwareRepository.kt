package com.alekseyld.repository.impl

import com.alekseyld.repository.IFirmwareRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStream

class FirmwareRepository : IFirmwareRepository {

    private val gson = Gson()

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

    private fun getDefaultFileName(macAddress: String) =
        "${macAddress.fileName()}.bin"

    private fun getFileNameByMac(macAddress: String) : String {
        val macListFile = File("firmwares/firmwares.json")

        if (!macListFile.exists()) {
            macListFile.createNewFile()

            val emptySet = mutableMapOf(
                macAddress to getDefaultFileName(macAddress)
            )

            macListFile.writeBytes(
                gson.toJson(emptySet).toByteArray()
            )

            return emptySet[macAddress]!!
        } else {

            val jsonMacs = macListFile.readText()

            val macList = gson.fromJson<Map<String, String>>(jsonMacs,
                object : TypeToken<Map<String, String>>() {}.type)

            return macList.getOrElse(macAddress) {

                val defaultFileName = getDefaultFileName(macAddress)

                macList.toMutableMap()[macAddress]= defaultFileName

                macListFile.writeBytes(
                    gson.toJson(macList).toByteArray()
                )

                defaultFileName
            }
        }
    }

    override fun getFirmwareBin(macAddress: String): File? {

        val fileName = getFileNameByMac(macAddress)

        val file = File(fileName)

        return if (file.exists()) file
        else null
    }

    private fun String.fileName() : String {
        return replace(":", "-")
    }
}