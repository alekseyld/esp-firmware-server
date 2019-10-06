package com.alekseyld.service

import com.alekseyld.model.EspHeaders
import com.alekseyld.repository.IFirmwareRepository
import com.alekseyld.utils.md5
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class FirmwareService(
    private val repository: IFirmwareRepository
) : IFirmwareService {

    override fun processUpdateRequest(espHeaders: EspHeaders): IFirmwareService.Result {

        val firmwareFile = repository.getFirmwareBin(espHeaders.staMac)

        val localHash = firmwareFile.md5()

    }

    //    val pathBin = "./firmwares/esp-firebase.ino.generic.bin"
//    val file = File(pathBin)
//
//    val md = MessageDigest.getInstance("MD5")
//    val hash = BigInteger(1, md.digest(file.readBytes())).toString(16).padStart(32, '0')
//
////            val logger = LoggerFactory.getLogger("FIRMWARE - ")
////            logger.debug("espHash = $espHash")
////            logger.debug("hash = $hash")
//
//    val fileSize = file.length()
//
//    val sizeInMb = fileSize / (1024.0 * 1024)
//
//    val sizeInMbStr = "%.2f".format(sizeInMb)
//
//    // construct reference to file
//    // ideally this would use a different filename
////            val file = File("./firmwares/$filename")
////            if(espHash != hash) {
////
////                call.response.status(HttpStatusCode.OK)
////                //call.response.header("Content-Type", "application/octet-stream")
////                call.response.header("Content-Disposition", "attachment; filename=$pathBin")
////                ///call.response.header("Content-Length", sizeInMbStr)
////                call.response.header("x-MD5", hash)
////
////                call.respondFile(file)
////            }
////            else
//    call.respond(HttpStatusCode.NotModified)

}