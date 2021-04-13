package com.hp.utilsstoresample.logic.network.network

import com.hp.utilsstoresample.logic.network.ServiceCreator
import com.hp.utilsstoresample.logic.network.service.CaiYunService

/**

 * Author：admin_h on 2021/4/13 22:31

 */
object CaiyunNetwork : BaseNetwork() {

    private val service = ServiceCreator.create(CaiYunService::class.java)

    suspend fun getRealtimeWeather(lon: Double, lat: Double) = service.getRealtimeWeather(lon, lat).await()

}