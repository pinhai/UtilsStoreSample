package com.hp.utilsstoresample.logic.repository

import androidx.lifecycle.liveData
import com.hp.utilsstoresample.logic.network.network.CaiyunNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

/**

 * Author：admin_h on 2021/4/13 22:52
    仓库可以选择使用本地数据源还是网络数据源
 */
object CaiyunRepository {

    fun getRealtimeWeather(lon: Double, lat: Double) = liveData(Dispatchers.IO) {
        //此处直接获取网络数据
        val result = try {
            val response = CaiyunNetwork.getRealtimeWeather(lon, lat)
            if(response.status == "ok") Result.success(response)
            else Result.failure(RuntimeException("response status is ${response.status}"))
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }

}