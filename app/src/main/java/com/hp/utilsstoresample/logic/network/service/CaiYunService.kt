package com.hp.utilsstoresample.logic.network.service

import com.hp.utilsstoresample.Constants.Constants
import com.hp.utilsstoresample.logic.model.response.RealtimeWeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**

 * Author：admin_h on 2021/4/13 18:14

 */
interface CaiYunService {

    /**
     * 获取实时天气信息
     * {"status":"ok","api_version":"v2.5","api_status":"active","lang":"zh_CN","unit":"metric","tzshift":28800,"timezone":"Asia\/Shanghai","server_time":1618311239,"location":[22.694703,113.802954],"result":{"realtime":{"status":"ok","temperature":26.0,"humidity":0.66,"cloudrate":0.0,"skycon":"CLEAR_NIGHT","visibility":30.0,"dswrf":528.9,"wind":{"speed":8.28,"direction":212.0},"pressure":100799.85,"apparent_temperature":27.3,"precipitation":{"local":{"status":"ok","datasource":"radar","intensity":0.0},"nearest":{"status":"ok","distance":10000.0,"intensity":0.0}},"air_quality":{"pm25":12.2,"pm10":19.6,"o3":14.3,"so2":5.0,"no2":94.5,"co":0.6,"aqi":{"chn":47.0,"usa":51.0},"description":{"chn":"\u4f18","usa":"\u826f"}},"life_index":{"ultraviolet":{"index":0.0,"desc":"\u65e0"},"comfort":{"index":3,"desc":"\u70ed"}}},"primary":0}}
     */
    @GET("/v2.5/${Constants.CAI_YUN_TOKEN}/{lon},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lon") lon: Double, @Path("lat") lat: Double) : Call<RealtimeWeatherModel>

}