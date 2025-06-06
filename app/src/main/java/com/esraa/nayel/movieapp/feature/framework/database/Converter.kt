package com.esraa.nayel.movieapp.feature.framework.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converter {
    @TypeConverter
    fun fromSetStringToString(value: Set<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToSetString(value: String): Set<String> {
        return Json.decodeFromString(value)
    }

}