package com.hp.sample.logic.model.response

import com.google.gson.annotations.SerializedName

/**

 * Author：admin_h on 2021/4/13 19:40

 */
data class RealtimeWeatherModel(val status: String,
                                @SerializedName("server_time") val serverTime: Long,
                                val result: Result
                                )

data class Result(val realtime: Realtime)

data class Realtime(val status: String, val temperature: Float,
                    @SerializedName("apparent_temperature") val apparentTemperature: Float,//体感温度
                    val humidity: Float, //湿度
                    val visibility: Float //可见度
                    )