package com.hp.utilsstoresample.logic.network

import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.getClassName
import com.hp.utilsstoresample.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**

 * Author：admin_h on 2021/4/13 14:11

 */
object ServiceCreator {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val cacheServices = HashMap<Class<*>, Any>()

    private var loggingInterceptor = HttpLoggingInterceptor { message ->
        //打印retrofit日志
        LogUtil.d(getClassName(), message)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build();

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun <T> create(service: Class<T>) : T {
        var serviceApi = cacheServices[service] as T
        if (serviceApi == null) {
            serviceApi = retrofit.create(service)
            cacheServices[service] = serviceApi!!
        }
        return serviceApi
    }

}