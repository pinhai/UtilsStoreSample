package com.hp.utilsstoresample.logic.repository

import com.hp.utils_store.utils.NetworkUtil
import com.hp.utilsstoresample.BaseApp
import com.hp.utilsstoresample.constants.Constants
import com.hp.utilsstoresample.logic.network.ServiceCreator
import com.hp.utilsstoresample.logic.network.cache.NetCacheProviders
import com.hp.utilsstoresample.logic.network.service.CaiYunService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import java.lang.RuntimeException

/**

 * Author：admin_h on 2021/4/13 22:52
    仓库可以选择使用本地数据源还是网络数据源
 */
object CaiyunRepository : BaseRepository(){

//    fun getRealtimeWeather(lon: Double, lat: Double) = fire {
//        //此处直接获取网络数据
//        val response = CaiyunNetwork.getRealtimeWeather(lon, lat)
//        if(response.status == "ok") Result.success(response)
//        else Result.failure(RuntimeException("response status is ${response.status}"))
//    }

    fun getRealtimeWeatherRx(lon: Double, lat: Double) = fire{
        val response = await {
            val observable = ServiceCreator.create(CaiYunService::class.java).getRealtimeWeather(lon, lat)
            //使用RxCache缓存网络数据
            NetCacheProviders.providers
                .getRealtimeWeather(observable, DynamicKey("/v2.5/${Constants.CAI_YUN_TOKEN}/"),
                    EvictDynamicKey(!NetworkUtil.isMobileConnected(BaseApp.context)))
                .subscribeOn(AndroidSchedulers.mainThread())
        }
        if(response.status == "ok") Result.success(response)
        else Result.failure(RuntimeException("response status is ${response.status}"))
    }

}