package com.alekseyld.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.css.*
import kotlinx.html.*

fun Routing.rootController() {

    get("/") {
        call.respondText(" WORLD!", contentType = ContentType.Text.Plain)
    }

    get("/html-dsl") {
        call.respondHtml {
            head {
                title("Document title")
                meta(charset = "utf-8")
                styleLink("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css")
                script(type = "text/javascript", src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js") {}
            }

            body {
                nav (classes = "navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0") {
                    a (classes = "navbar-brand col-sm-3 col-md-2 mr-0") {
                        text("Company name")
                    }
                    input(
                        classes = "form-control form-control-dark w-100",
                        type = InputType.text
                    ) {

                    }
                    ul (classes = "navbar-nav px-3") {
                        li (classes = "nav-item text-nowrap") {
                            a (classes = "nav-link") {
                                text("Sign out")
                            }
                        }
                    }
                }
//                    h1 { +"HTML" }
//                    ul {
//                        transaction {
//                            TempStats.selectAll().forEach {
//                                li { "${it[dateUpdated]}" }
//                            }
//                        }
////                        for (n in 1..10) {
////                            li { +"$n" }
////                        }
//                    }
            }
        }
    }

    get("/styles.css") {
        call.respondCss {
            body {
                fontSize = 0.875.em
            }
            rule(".feather") {
                width = 16.0.px
                height = 16.0.px
                verticalAlign = VerticalAlign.textBottom
            }

            p {
                fontSize = 2.em
            }
            rule("p.myclass") {
                color = Color.blue
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