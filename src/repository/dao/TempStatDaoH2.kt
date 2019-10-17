package com.alekseyld.repository.dao

import org.jetbrains.exposed.dao.IntIdTable

object TempStats : IntIdTable() {
    val dateUpdated = long("dateUpdated")
}

object TempNode : IntIdTable() {

}

class TempStatDaoH2 : TempStatDao