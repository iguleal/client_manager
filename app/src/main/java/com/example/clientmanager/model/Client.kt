package com.example.clientmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "mobile")val mobile: String,
    @ColumnInfo(name = "contact")val contact: String,
    @ColumnInfo(name = "desc")val desc: String,

    //    @ColumnInfo(name = "dateStart")val dateStart: Date = Date(),
    //    @ColumnInfo(name = "dateFinish")val dateFinish: Date,

    @ColumnInfo(name = "isPaid")val isPaid: Boolean = false,
    @ColumnInfo(name = "isFixed")val isFixed: Boolean = false,
    @ColumnInfo(name = "totalValue")val totalValue: Int = 0,
    @ColumnInfo(name = "cashValue")val cashValue: Int = 0,
    @ColumnInfo(name = "pixValue")val pixValue: Int = 0,
    @ColumnInfo(name = "cardValue")val cardValue: Int = 0,
    @ColumnInfo(name = "cardTimes")val cardTimes: Int = 0
)