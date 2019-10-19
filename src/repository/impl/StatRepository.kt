package com.alekseyld.repository.impl

import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.NodeEntity
import com.alekseyld.db.tables.StatEntity
import com.alekseyld.repository.IStatRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class StatRepository : IStatRepository {

    override fun putNodes(nodes: List<Node>) {
        transaction {
            val parent = StatEntity.new {
                dateUpdated = Date().time
            }

            nodes.forEach {
                NodeEntity.new {
                    nodeName = it.nodeName
                    value = it.value
                    parentStat = parent
                }
            }
        }
    }
}