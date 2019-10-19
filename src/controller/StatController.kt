package com.alekseyld.controller

import io.ktor.routing.Routing

//GET https://lysovda-sh.firebaseio.com/.json?auth={token}
//GET https://lysovda-sh.firebaseio.com/temps.json?auth={token}

//Для перезаписи
//PUT https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

//Для добавления
//POST https://lysovda-sh.firebaseio.com/temps.json?auth=
//Body - {"id":"1", "temp":"22.0","time": {".sv":"timestamp"}}

fun Routing.statController() {


}