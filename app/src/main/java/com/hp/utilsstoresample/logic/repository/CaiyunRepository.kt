package com.hp.utilsstoresample.logic.repository

import androidx.lifecycle.liveData
import com.hp.utilsstoresample.logic.network.network.CaiyunNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**

 * Author：admin_h on 2021/4/13 22:52
    仓库可以选择使用本地数据源还是网络数据源
 */
object CaiyunRepository : BaseRepository(){

    fun getRealtimeWeather(lon: Double, lat: Double) = fire {
        //此处直接获取网络数据
        val response = CaiyunNetwork.getRealtimeWeather(lon, lat)
        if(response.status == "ok") Result.success(response)
        else Result.failure(RuntimeException("response status is ${response.status}"))
    }

}