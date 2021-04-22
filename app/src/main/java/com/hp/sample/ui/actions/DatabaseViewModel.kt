package com.hp.sample.ui.actions

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hp.sample.logic.database.entity.User
import com.hp.sample.logic.repository.UserRepository

/**
 *
 * Author：admin_h on 2021/4/16 15:11
 */
class DatabaseViewModel : ViewModel() {

    fun insertUser(context: Context, user: User) = UserRepository.insertUser(context, user)

    fun deleteUser(context: Context, id: Long) = UserRepository.deleteUserById(context, id)

    fun updateUser(context: Context, user: User) = UserRepository.updateUser(context, user)

    fun selectUserById(context: Context, id: Long) = UserRepository.selectUserById(context, id)

    fun selectAllUser(context: Context) = UserRepository.selectAllUser(context)

}