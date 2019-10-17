package com.alekseyld


import com.alekseyld.repository.dao.TempStats
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        transaction {
            create(TempStats)
        }
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