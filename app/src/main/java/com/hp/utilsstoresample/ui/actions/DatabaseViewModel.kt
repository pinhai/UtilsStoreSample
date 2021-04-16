package com.hp.utilsstoresample.ui.actions

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hp.utilsstoresample.logic.database.entity.User
import com.hp.utilsstoresample.logic.repository.UserRepository

/**
 *
 * Author：admin_h on 2021/4/16 15:11
 */
class DatabaseViewModel : ViewModel() {

    fun insertUser(context: Context, user: User) = UserRepository.insertUser(context, user)

    fun selectUserById(context: Context, id: Long) = UserRepository.selectUserById(context, id)

}