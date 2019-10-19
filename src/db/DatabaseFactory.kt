package com.alekseyld.db

import com.alekseyld.db.tables.StatEntity
import com.alekseyld.db.tables.TableEspClients
import com.alekseyld.db.tables.TableNodes
import com.alekseyld.db.tables.TableStats
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        transaction {
            create(TableEspClients)
            create(TableNodes)
            create(TableStats)
        }

        val stat = transaction {
            StatEntity[1]
        }

//        val stat = transaction {
//            StatEntity.new {
//                dateUpdated = Date().time
//            }
//        }
//
//        transaction {
//
//            NodeEntity.new {
//                nodeName = "Temp 1"
//                value = 25f
//                parentStat = stat
//            }
//
//            NodeEntity.new {
//                nodeName = "Temp 2"
//                value = 20f
//                parentStat = stat
//            }
//
//            NodeEntity.new {
//                nodeName = "Temp 3"
//                value = 30f
//                parentStat = stat
//            }
//        }

        val nodes = transaction {
            stat.nodes.first().nodeName
        }

        println("sadda")
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
//        config.jdbcUrl = "jdbc:h2:mem:smarthomedb"
        config.jdbcUrl = "jdbc:h2:file:./smarthomedb"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
         block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}