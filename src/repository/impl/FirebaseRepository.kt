package com.alekseyld.repository.impl

import com.alekseyld.AppConfiguration
import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat
import com.alekseyld.di.inject
import com.alekseyld.repository.IStatRepository
import com.alekseyld.utils.format
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.coroutines.runBlocking
import java.util.*

//GET https://lysovda-sh.firebaseio.com/.json?auth={token}
//GET https://lysovda-sh.firebaseio.com/temps.json?auth={token}

//Для перезаписи
//PUT https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

//Для добавления
//POST https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

class FirebaseRepository(
    private val gson: Gson
) : IStatRepository {

    data class FirebaseModel(
        val id: String,
        val temp: String,
        val time: Long
    )

    fun Node.firebaseModel(time: Long) =
        FirebaseModel(
            id = this.nodeName,
            temp = this.value.format(2),
            time = time
        )

    private val nodeUrl =
        "${AppConfiguration.firebaseUrl}${AppConfiguration.node}.json?auth=${AppConfiguration.authToken}"

    override fun putNodes(nodes: List<Node>) {
        runBlocking { //FIXME to suspend fun

            val dateLong = Date().time

            val httpClient by inject<HttpClient>()

            httpClient.post<Unit> {
                url(nodeUrl)
                body = TextContent(
                    gson.toJson(
                        nodes.map { it.firebaseModel(dateLong) }
                    ),
                    ContentType.Application.Json
                )
            }
        }
    }

    override fun getAllStats(): List<Stat> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}