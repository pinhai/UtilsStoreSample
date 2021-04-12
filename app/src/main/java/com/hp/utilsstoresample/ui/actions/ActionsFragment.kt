package com.hp.utilsstoresample.ui.actions

import android.location.Address
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hp.utils_store.utils.LogUtil
import com.hp.utils_store.utils.getClassName
import com.hp.utils_store.utils.helper.LocationHelper
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*

class ActionsFragment : BaseFragment(),View.OnClickListener {

    private lateinit var actionsViewModel: ActionsViewModel

    private lateinit var locationHelper: LocationHelper

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

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_get_location.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_get_location -> {
                LogUtil.v(getClassName(), "点击定位按钮")
                location();
            }
        }
    }

    private fun location() {
        locationHelper = LocationHelper(this)
        locationHelper.requestLocation(object : LocationHelper.OnLocationResultListener{
            override fun onLocationResult(address: Address?) {
            }

            override fun onLocationFailed() {
            }
        })
    }
}