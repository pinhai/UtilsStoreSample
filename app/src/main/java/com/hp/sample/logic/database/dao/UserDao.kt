package com.hp.sample.logic.database.dao

import androidx.room.*
import com.hp.sample.logic.database.entity.User

/**
 * 如果使用非实体类（这里是User）操作，就必须编写SQL语句，且只能使用@Query注解
 * Author：admin_h on 2021/4/16 13:47
 */
@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)

    @Query("delete from User where id = :id")
    fun deleteUserById(id: Long)

    @Update
    fun updateUser(user: User)

    @Query("select * from User where id = :id")
    fun selectById(id: Long): User

    @Query("select * from User")
    fun selectAllUser(): List<User>

}