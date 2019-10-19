package com.alekseyld.service.impl

import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat
import com.alekseyld.repository.IStatRepository
import com.alekseyld.service.IStatService

class StatService(
    private val localRepository: IStatRepository,
    private val firebaseRepository: IStatRepository
) : IStatService {

    override fun putNodes(nodes: List<Node>) {
        localRepository.putNodes(nodes)
        firebaseRepository.putNodes(nodes)
    }

    override fun getAllStats(): List<Stat> =
            localRepository.getAllStats()
}