package com.example.clientmanager.model

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toDate(long: Long?): Date? {
        return if (long != null) Date(long) else null
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}