package com.alekseyld.service.impl

import com.alekseyld.AppConfiguration
import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat
import com.alekseyld.db.tables.toStat
import com.alekseyld.repository.IStatRepository
import com.alekseyld.service.IStatService

class StatService(
    private val localRepository: IStatRepository,
    private val firebaseRepository: IStatRepository
) : IStatService {

    override suspend fun putNodes(nodes: List<Node>) {

        val stat = nodes.toStat()

        localRepository.putStat(stat)

        //TODO в firebase должно быть только 5 значений

        val statsFromFB = firebaseRepository.getAllStats()
            .toMutableList()

        if (statsFromFB.size > AppConfiguration.firebaseCount) {
            statsFromFB.removeAt(0)
        }

        statsFromFB.add(stat)

        firebaseRepository.putStats(statsFromFB)
    }

    override fun getAllStats(): List<Stat> =
            localRepository.getAllStats()
}