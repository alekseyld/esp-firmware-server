package com.alekseyld.service

import com.alekseyld.db.tables.Node

interface IStatService {

    fun putNodes(nodes: List<Node>)

}