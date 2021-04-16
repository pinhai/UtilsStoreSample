package com.hp.utilsstoresample.ui.actions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hp.utilsstoresample.logic.repository.CaiyunRepository

class ActionsViewModel : ViewModel() {

    private val longitude = MutableLiveData<Double>()
    private var latitude : Double = 0.0

    val weatherModel = Transformations.switchMap(longitude){lon ->
        CaiyunRepository.obtainRealtimeWeather(lon, latitude)
    }

    fun getRealtimeWeather(lon: Double, lat: Double){
        latitude = lat
        longitude.value = lon
    }
}