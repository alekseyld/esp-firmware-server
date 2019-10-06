package com.alekseyld.repository

import java.io.File
import java.io.InputStream

interface IFirmwareRepository {

    fun saveFirmwareBin(macAddress: String, inputStream: InputStream)

    fun getFirmwareBin(macAddress: String) : File

}