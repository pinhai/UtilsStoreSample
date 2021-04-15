package com.hp.utilsstoresample.logic.repository

import androidx.lifecycle.liveData
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**

 * Author：admin_h on 2021/4/14 15:28
 */
open class BaseRepository {

    internal suspend fun <T> await(block: () -> Observable<T>) : T{
        return suspendCoroutine { continuation ->
            block().subscribe({
                continuation.resume(it)
            },{
                continuation.resumeWithException(it)
            })
        }
    }

    /**
     * 将数据封装到 androidx.lifecycle.LiveData 类中
     */
    internal fun <T> fire(context: CoroutineContext = Dispatchers.IO, block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }

}