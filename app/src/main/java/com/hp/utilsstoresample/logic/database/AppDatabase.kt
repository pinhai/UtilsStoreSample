package com.hp.utilsstoresample.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hp.utilsstoresample.logic.database.dao.UserDao
import com.hp.utilsstoresample.logic.database.entity.User

/**
 *
 * Author：admin_h on 2021/4/16 14:17
 */
@Database(version = 1, entities = [User::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        private const val DATABASE_NAME = "database_appname"
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .addMigrations(migration_1_2)
                .build().apply { instance == this }
        }

        //更新数据库，给user表添加一列，同时也要在User实体类添加该字段
        val migration_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table User add column interest text not null default 'unknown'")
            }
        }
    }

}