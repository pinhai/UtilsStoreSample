package com.hp.utilsstoresample.logic.repository

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**

 * Author：admin_h on 2021/4/14 15:28
 */
open class BaseRepository {

    internal fun <T> fire(context: CoroutineContext = Dispatchers.IO,
                          block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }

}