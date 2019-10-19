package com.alekseyld.db.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

object TableEspClients : IdTable<String>() {
    override val id: Column<EntityID<String>>
        get() = macAddress

    val macAddress = varchar("macAddress", length = 30).primaryKey().entityId()
    val firmwareFile = varchar("firmwareFile", length = 80)
}

class EspClientEntity(macAddress: EntityID<String>)
    : Entity<String>(macAddress) {
    companion object : EntityClass<String, EspClientEntity>(TableEspClients)

    var macAddress by TableEspClients.macAddress
    var firmwareFile by TableEspClients.firmwareFile
}