package com.alekseyld.service

import com.alekseyld.db.tables.Node
import com.alekseyld.db.tables.Stat

interface IStatService {

    fun putNodes(nodes: List<Node>)

    fun getAllStats() : List<Stat>

}