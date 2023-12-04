package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.model.Handbooks

@Database(
    entities = [
        Handbooks::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CompanyDB : RoomDatabase() {
    abstract fun handbooksDao(): HandbooksDAO
}