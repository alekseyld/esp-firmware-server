package com.alekseyld.repository

import com.alekseyld.db.tables.Stat

interface IStatRepository {

    suspend fun putStat(stat: Stat)

    suspend fun putStats(stats: List<Stat>)

    fun getAllStats() : List<Stat>

}