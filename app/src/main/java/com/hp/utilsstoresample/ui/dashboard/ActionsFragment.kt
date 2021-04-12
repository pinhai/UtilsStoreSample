package com.hp.utilsstoresample.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hp.utils_store.utils.LogUtil
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseFragment

class ActionsFragment : BaseFragment(),View.OnClickListener {

    private lateinit var actionsViewModel: ActionsViewModel

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
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_get_location -> {
                //todo 定位
                LogUtil.v(TAG, "点击定位按钮")
            }
        }
    }
}