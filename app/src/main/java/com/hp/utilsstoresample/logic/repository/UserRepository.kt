package com.hp.utilsstoresample.logic.repository

import android.content.Context
import android.database.SQLException
import com.hp.utilsstoresample.logic.database.dao.UserDao
import com.hp.utilsstoresample.logic.database.entity.User

/**
 *
 * Author：admin_h on 2021/4/16 14:49
 */
object UserRepository: BaseRepository() {

    private fun getDao(context: Context): UserDao {
        return getDatabase(context).userDao()
    }

    fun insertUser(context: Context, user: User) = fire {
        val id = getDao(context).insertUser(user)
        if(id != null) Result.success(id)
        else Result.failure(SQLException("新增失败"))
    }

    fun deleteUserById(context: Context, id: Long) = fire{
        getDao(context).deleteUserById(id)
        Result.success(id)
    }

    fun updateUser(context: Context, user: User) = fire{
        getDao(context).updateUser(user)
        Result.success(user)
    }

    fun selectUserById(context: Context, id: Long) = fire {
        val user = getDao(context).selectById(id)
        if(user != null) Result.success(user)
        else Result.failure(SQLException("查询失败"))
    }

    fun selectAllUser(context: Context) = fire{
        val users = getDao(context).selectAllUser()
        if(users != null) Result.success(users)
        else Result.failure(SQLException("查询失败"))
    }

}