package com.alekseyld.controller

import com.alekseyld.db.tables.Node
import com.alekseyld.service.IStatService
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.put
import org.koin.ktor.ext.inject

//GET https://lysovda-sh.firebaseio.com/.json?auth={token}
//GET https://lysovda-sh.firebaseio.com/temps.json?auth={token}

//Для перезаписи
//PUT https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

//Для добавления
//POST https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

fun Routing.statController() {

    val statService by inject<IStatService>()

    val gson by inject<Gson>()
    val nodeTypeToken by inject<TypeToken<List<Node>>>()

    //[{"nodeName":"Name 1", "value": 10.0}, {"nodeName":"Name 2", "value": 15.0}]
    put("/stats") {
        val body = call.receiveText()

        val nodes = try {
            gson.fromJson<List<Node>>(body, nodeTypeToken.type)

        } catch (e: JsonSyntaxException) { emptyList<Node>() }

        call.respond(
            if (nodes.isNullOrEmpty().not()) {
                statService.putNodes(nodes)

                HttpStatusCode.OK
            } else {
                HttpStatusCode.NotModified
            }
        )
    }

}