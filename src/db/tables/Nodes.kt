package com.alekseyld.db.tables

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.util.*

object TableNodes : IntIdTable() {
    val parentStat = reference("parentStat", TableStats)

    val nodeName = varchar("nodeName", 255)
    val value = float("value")
}

class NodeEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<NodeEntity>(TableNodes)

    var nodeName by TableNodes.nodeName
    var value by TableNodes.value

    var parentStat by StatEntity referencedOn TableNodes.parentStat
}

data class Node(
    val nodeName: String,
    val value: Float
)

fun List<Node>.toStat() : Stat =
    Stat(
        Date().time,
        this
    )