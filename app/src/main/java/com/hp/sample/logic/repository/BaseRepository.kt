package com.hp.sample.logic.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.hp.sample.logic.database.AppDatabase
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 基础仓库类，可以选择使用本地数据源还是网络数据源
 * Author：admin_h on 2021/4/14 15:28
 */
open class BaseRepository {

    /**
     * 从lambada表达式获取包含了期望数据的Observable
     */
    protected suspend fun <T> obtainResponse(block: () -> Observable<T>) : T{
        return suspendCoroutine { continuation ->
            block().subscribe({
                continuation.resume(it)
            },{
                continuation.resumeWithException(it)
            })
        }
    }

    /**
     * 将数据封装到 androidx.lifecycle.LiveData 类的实例中，并返回该实例
     */
    protected fun <T> fire(context: CoroutineContext = Dispatchers.IO, block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }

    protected fun getDatabase(context: Context) : AppDatabase{
        return AppDatabase.getDatabase(context)
    }

}