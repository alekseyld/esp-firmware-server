package com.alekseyld.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object TempStats : IntIdTable() {
    val dateUpdated = long("dateUpdated")
}

object TempNode : IntIdTable() {

}