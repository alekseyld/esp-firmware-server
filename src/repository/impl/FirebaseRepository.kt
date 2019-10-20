package com.alekseyld.repository.impl

import com.alekseyld.AppConfiguration
import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat
import com.alekseyld.di.inject
import com.alekseyld.repository.IStatRepository
import com.alekseyld.utils.format
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.coroutines.runBlocking

//GET https://.firebaseio.com/.json?auth={token}
//GET https://.firebaseio.com/temps.json?auth={token}

//Для перезаписи
//PUT https://firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

//Для добавления
//POST https://.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

class FirebaseRepository(
    private val gson: Gson,
    private val typeToken: TypeToken<Map<String, FirebaseModel>>
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

    private suspend fun putNode(node: Node, time: Long) {
        val httpClient by inject<HttpClient>()

        httpClient.post<Unit> {
            url(nodeUrl)
            body = TextContent(
                gson.toJson(
                    node.firebaseModel(time)
                ),
                ContentType.Application.Json
            )
        }

        httpClient.close()
    }

    override suspend fun putStat(stat: Stat) {
        stat.nodes.forEach {
            putNode(it, stat.dateUpdated)
        }
    }

    private suspend fun deleteStats() {
        val httpClient by inject<HttpClient>()

        httpClient.delete<Unit> {
            url(nodeUrl)
        }

        httpClient.close()
    }

    override suspend fun putStats(stats: List<Stat>) {
        deleteStats()

        stats.forEach { stat ->
            putStat(stat)
        }
    }

    override fun getAllStats(): List<Stat> {
        return runBlocking {

            val httpClient by inject<HttpClient>()

            val json = httpClient.get<String> {
                url(nodeUrl)
            }

            val models = gson.fromJson<Map<String, FirebaseModel>>(json, typeToken.type)
                .map {
                    it.value
                }

            if (models.isNullOrEmpty().not()) {

                var i = 0
                val stats = mutableListOf(
                        Stat(models[0].time, mutableListOf())
                    )

                models.forEach {

                    //FIXME IF List not sorted
                    if (stats[i].dateUpdated != it.time) {
                        stats.add(
                            Stat(it.time, mutableListOf())
                        )
                        i++
                    }

                    (stats[i].nodes as MutableList)
                        .add(Node(
                            nodeName = it.id,
                            value = it.temp.toFloat()
                        ))
                }

                return@runBlocking stats

            } else emptyList<Stat>()
        }
    }
}