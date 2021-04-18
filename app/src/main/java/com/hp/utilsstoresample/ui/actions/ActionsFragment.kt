package com.hp.utilsstoresample.ui.actions

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.ToastUtil
import com.hp.utils_store.utils.getClassName
import com.hp.utils_store.utils.helper.LocationHelper
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_actions.*

class ActionsFragment private constructor(): BaseFragment(),View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ActionsViewModel::class.java) }

    private var locationHelper: LocationHelper? = null

    companion object{
        fun newInstance(): ActionsFragment {
            return ActionsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ImmersionBar.setTitleBar(this, contentLayout)
        initViewModelObservable()
        btn_get_location.setOnClickListener(this)
        btn_get_weather_info.setOnClickListener(this)
        btn_database.setOnClickListener(this)
    }

    private fun initViewModelObservable() {
        viewModel.weatherModel.observe(viewLifecycleOwner){
            if(it.isSuccess){
                ToastUtil.show(context, it.getOrNull()?.result?.realtime?.temperature?.toString() ?: "error")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        locationHelper!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_get_location -> {
                LogUtil.v(getClassName(), "点击定位按钮")
                location();
            }
            R.id.btn_get_weather_info -> {
//                location(true)
                getWeatherInfo(113.81413,22.63381)
            }
            R.id.btn_database -> {
                DatabaseActivity.start(v.context)
            }
        }
    }

    private fun getWeatherInfo(longitude: Double, latitude: Double) {
        viewModel.getRealtimeWeather(longitude, latitude)
    }

    private fun location(getWeatherInfo: Boolean = false) {
        if(locationHelper == null) locationHelper = LocationHelper(this)
        locationHelper!!.requestLocation(object : LocationHelper.OnLocationResultListener{
            override fun onLocationResult(address: Address?) {
                if(getWeatherInfo && address != null) getWeatherInfo(address.longitude, address.latitude)
            }

            override fun onLocationFailed() {
            }
        })
    }
}