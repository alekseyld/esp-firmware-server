package com.alekseyld

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
import org.slf4j.LoggerFactory
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(Apache) {
    }

    routing {
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

            val espHash = call.request.headers["x-ESP8266-sketch-md5"]

            //x-ESP8266-sketch-md5
            //X-ESP8266-STA-MAC
            //x-ESP8266-free-space
            //x-ESP8266-sketch-size
            //x-ESP8266-sketch-md5
            //x-ESP8266-chip-size
            //x-ESP8266-sdk-version
            //x-ESP8266-mode

            val pathBin = "./firmwares/esp-firebase.ino.generic.bin"
            val file = File(pathBin)

            val md = MessageDigest.getInstance("MD5")
            val hash = BigInteger(1, md.digest(file.readBytes())).toString(16).padStart(32, '0')

            val logger = LoggerFactory.getLogger("FIRMWARE - ")
            logger.debug("espHash = $espHash")
            logger.debug("hash = $hash")

            val fileSize = file.length()

            val sizeInMb = fileSize / (1024.0 * 1024)

            val sizeInMbStr = "%.2f".format(sizeInMb)

            // construct reference to file
            // ideally this would use a different filename
//            val file = File("./firmwares/$filename")
            if(espHash != hash) {

                call.response.status(HttpStatusCode.OK)
                //call.response.header("Content-Type", "application/octet-stream")
                call.response.header("Content-Disposition", "attachment; filename=$pathBin")
                ///call.response.header("Content-Length", sizeInMbStr)
                call.response.header("x-MD5", hash)

                call.respondFile(file)
            }
            else call.respond(HttpStatusCode.NotModified)
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
