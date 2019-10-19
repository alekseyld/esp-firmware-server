package com.alekseyld.db.tables

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object TableStats : IntIdTable() {
    val dateUpdated = long("dateUpdated")
}

class StatEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<StatEntity>(TableStats)

    var dateUpdated by TableStats.dateUpdated

    val nodes by NodeEntity referrersOn TableNodes.parentStat
}

data class Stat(
    val dateUpdated: Long,
    val nodes: List<Node>
)