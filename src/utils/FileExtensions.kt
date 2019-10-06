package com.alekseyld.utils

import kotlinx.html.InputType
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun File.md5() : String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1,
        md.digest(readBytes())
    )
        .toString(16)
        .padStart(32, '0')
}