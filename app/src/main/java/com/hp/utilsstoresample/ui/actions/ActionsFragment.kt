package com.hp.utilsstoresample.ui.actions

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.ToastUtil
import com.hp.utils_store.utils.getClassName
import com.hp.utils_store.utils.helper.LocationHelper
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.logic.model.response.RealtimeWeatherModel
import com.hp.utilsstoresample.logic.network.ServiceCreator
import com.hp.utilsstoresample.logic.network.service.CaiYunService
import com.hp.utilsstoresample.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_actions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActionsFragment : BaseFragment(),View.OnClickListener {

    private lateinit var actionsViewModel: ActionsViewModel

    private var locationHelper: LocationHelper? = null

    companion object{
        fun newInstance(): ActionsFragment {
            val args = Bundle()
            val fragment = ActionsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        actionsViewModel = ViewModelProvider(this).get(ActionsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_actions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_get_location.setOnClickListener(this)
        btn_get_weather_info.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationHelper!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_get_location -> {
                LogUtil.v(getClassName(), "点击定位按钮")
                location();
            }
            R.id.btn_get_weather_info -> {
                location(true)
            }
        }
    }

    private fun getWeatherInfo(longitude: Double, latitude: Double) {
        ServiceCreator.create(CaiYunService::class.java)
            .getRealtimeWeather(longitude, latitude)
            .enqueue(object : Callback<RealtimeWeatherModel> {
                override fun onResponse(call: Call<RealtimeWeatherModel>, response: Response<RealtimeWeatherModel>) {
                    if(response.isSuccessful){
                        ToastUtil.show(context, response.body()?.result?.realtime?.temperature?.toString() ?: "error")
                    }
                }

                override fun onFailure(call: Call<RealtimeWeatherModel>, t: Throwable) {
                }

            })
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