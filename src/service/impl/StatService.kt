package com.alekseyld.service.impl

import com.alekseyld.db.tables.Node
import com.alekseyld.repository.IStatRepository
import com.alekseyld.service.IStatService

class StatService(
    private val localRepository: IStatRepository
) : IStatService {

    override fun putNodes(nodes: List<Node>) {

        localRepository.putNodes(nodes)


    }
}