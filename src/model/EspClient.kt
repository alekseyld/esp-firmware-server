package com.alekseyld.model

import org.jetbrains.exposed.dao.IntIdTable

object EspClients : IntIdTable() {
    val macAddress = varchar("macAddress", length = 20)
}