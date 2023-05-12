package org.fundatec.vinilemess.tcc.fintrackapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.dao.UserDao
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        fun getInstance(context:Context) : UserDatabase {
            return Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                "user.db"
            ).build()
        }
    }
}