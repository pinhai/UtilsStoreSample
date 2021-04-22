package com.hp.sample.logic.network.cache

import com.hp.sample.constants.Constants
import com.hp.sample.logic.model.response.RealtimeWeatherModel
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import io.rx_cache2.LifeCache
import io.rx_cache2.ProviderKey
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import java.io.File
import java.util.concurrent.TimeUnit

/**

 * Author：admin_h on 2021/4/15 14:16
    网络数据缓存提供器
 */
interface NetCacheProviders {

    companion object{
        val providers = RxCache.Builder()
            .persistence(File(Constants.DIR_CACHE_FILE), GsonSpeaker())
            .using(NetCacheProviders::class.java)
    }

    @ProviderKey("realtime-weather")
    @LifeCache(duration = 15, timeUnit = TimeUnit.DAYS)
    fun obtainRealtimeWeather(o: Observable<RealtimeWeatherModel>, key: DynamicKey, evictDynamicKey: EvictDynamicKey) : Observable<RealtimeWeatherModel>

}