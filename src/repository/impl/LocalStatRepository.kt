package com.alekseyld.repository.impl

import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.NodeEntity
import com.alekseyld.db.tables.Stat
import com.alekseyld.db.tables.StatEntity
import com.alekseyld.repository.IStatRepository
import org.jetbrains.exposed.sql.transactions.transaction

class LocalStatRepository : IStatRepository {

    override suspend fun putStat(stat: Stat) {
        transaction {
            val parent = StatEntity.new {
                dateUpdated = stat.dateUpdated
            }

            stat.nodes.forEach {
                NodeEntity.new {
                    nodeName = it.nodeName
                    value = it.value
                    parentStat = parent
                }
            }
        }
    }

    override suspend fun putStats(stats: List<Stat>) {
        stats.forEach { stat ->
            putStat(stat)
        }
    }

    override fun getAllStats(): List<Stat> {
        return transaction {
            StatEntity.all().map { statEntity ->
                Stat(
                    dateUpdated = statEntity.dateUpdated,
                    nodes = statEntity.nodes.map { nodeEntity ->
                        Node(
                            nodeName = nodeEntity.nodeName,
                            value = nodeEntity.value
                        )
                    }
                )
            }
        }
    }
}