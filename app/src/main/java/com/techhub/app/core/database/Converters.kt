package com.techhub.app.core.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromDoubleList(list: List<Double>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toDoubleList(value: String): List<Double> {
        return try {
            json.decodeFromString(value)
        } catch (_: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            json.decodeFromString(value)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
