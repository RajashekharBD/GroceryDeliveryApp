package com.example.grocery.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grocery.model.CartItem

@Database(entities = [CartItem::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getInstance(context: android.content.Context): CartDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: android.content.Context): CartDatabase {
            return androidx.room.Room.databaseBuilder(
                context.applicationContext,
                CartDatabase::class.java,
                "cart_database"
            ).build()
        }
    }
}