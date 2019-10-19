package com.alekseyld.repository

import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat

interface IStatRepository {

    fun putNodes(nodes: List<Node>)

    fun getAllStats() : List<Stat>

}