package com.alekseyld

import com.alekseyld.service.IFirmwareService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.mockk.mockk
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun `firmware on valid params`() {

        val mockFirmwareService = mockk<IFirmwareService>()

        val mockModule = module {
            single { mockFirmwareService }
        }

        startKoin {
            modules(mockModule)
        }

        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/firmware") {
//                addHeader()


            }.apply {


            }
        }
    }

}
