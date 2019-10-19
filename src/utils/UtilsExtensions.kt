package com.alekseyld.utils

fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)