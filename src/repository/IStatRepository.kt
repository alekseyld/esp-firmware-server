package com.alekseyld.repository

import com.alekseyld.db.tables.Node

interface IStatRepository {

    fun putNodes(nodes: List<Node>)

}