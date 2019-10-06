package com.alekseyld

import com.alekseyld.di.appModule
import com.alekseyld.model.*
import com.alekseyld.service.IFirmwareService
import com.alekseyld.service.IFirmwareService.Result.AlreadyUpdated
import com.alekseyld.service.IFirmwareService.Result.RequiredUpdate
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.html.*
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)

    startKoin {
        modules(appModule)
    }
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(Apache) {}

    routing {

        val firmwareService by inject<IFirmwareService>()

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }

        post("/upload") { _ ->
            // retrieve all multipart data (suspending)
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if(part is PartData.FileItem) {
                    // retrieve file name of upload
                    val name = part.originalFileName!!
                    val file = File("/uploads/$name")

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

    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
