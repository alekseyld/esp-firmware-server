package com.alekseyld.model

const val HEADER_STA_MAC = "X-ESP8266-STA-MAC"
const val HEADER_FREE_SPACE = "x-ESP8266-free-space"
const val HEADER_SKETCH_SIZE = "x-ESP8266-sketch-size"
const val HEADER_SKETCH_MD5 = "x-ESP8266-sketch-md5"
const val HEADER_CHIP_SIZE = "x-ESP8266-chip-size"
const val HEADER_SDK_VERSION = "x-ESP8266-sdk-version"
const val HEADER_MODE = "x-ESP8266-mode"

data class EspHeaders (
    val staMac : String,
    val freeSpace : String,
    val sketchSize : String,
    val sketchMd5 : String,
    val chipSize : String,
    val sdkVersion : String,
    val mode : String
)