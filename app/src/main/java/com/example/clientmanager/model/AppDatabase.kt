package com.example.clientmanager.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Client::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "client_manager"
                    ).build()
                }
                INSTANCE as AppDatabase
            }else{
                INSTANCE as AppDatabase
            }
        }

    }

}