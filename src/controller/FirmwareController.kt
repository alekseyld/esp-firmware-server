package com.alekseyld.controller

import com.alekseyld.model.*
import com.alekseyld.service.IFirmwareService
import com.alekseyld.service.IFirmwareService.Result.AlreadyUpdated
import com.alekseyld.service.IFirmwareService.Result.RequiredUpdate
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.firmwareController() {

    val firmwareService by inject<IFirmwareService>()

    get("/firmware") {

        val call: ApplicationCall = call.request.call

        lateinit var espHeaders: EspHeaders

        call.request.headers.apply {
            espHeaders = EspHeaders(
                staMac = get(HEADER_STA_MAC)!!,
                freeSpace = get(HEADER_FREE_SPACE)!!,
                sketchSize = get(HEADER_SKETCH_SIZE)!!,
                sketchMd5 = get(HEADER_SKETCH_MD5)!!,
                chipSize = get(HEADER_CHIP_SIZE)!!,
                sdkVersion = get(HEADER_SDK_VERSION)!!,
                mode = get(HEADER_MODE)!!
            )
        }

        val result = firmwareService.processUpdateRequest(espHeaders)

        when (result) {
            is RequiredUpdate -> {
                call.response.status(HttpStatusCode.OK)
                call.response.header("x-MD5", result.md5Hash)

                call.response.header(
                    "Content-Disposition",
                    "attachment; filename=${result.updatedFirmware.name}"
                )

                call.respondFile(result.updatedFirmware)
            }
            is AlreadyUpdated -> {
                call.respond(HttpStatusCode.NotModified)
            }
        }
    }

    post("/firmware/upload") { _ ->
        // retrieve all multipart data (suspending)
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            // if part is a file (could be form item)
            if(part is PartData.FileItem) {
                // retrieve file name of upload
                val name = part.originalFileName!!
                val file = File("./firmwares/$name")

                // use InputStream from part to save file
                part.streamProvider().use { its ->
                    // copy the stream to the file with buffering
                    file.outputStream().buffered().use {
                        // note that this is blocking
                        its.copyTo(it)
                    }
                }
            }
            // make sure to dispose of the part after use to prevent leaks
            part.dispose()
        }
    }

}