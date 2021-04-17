package com.hp.utilsstoresample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.adapter.recyclerview.HomeListAdapter
import com.hp.utilsstoresample.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_list.*

/**
 *
 * Author：admin_h on 2021/4/17 21:41
 */
class HomeListFragment private constructor(): BaseFragment() {

    private lateinit var viewModel: HomeListViewModel

    companion object{
        fun newInstance(): HomeListFragment {
            return HomeListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(HomeListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        val data = mutableListOf<String>()
        for(i in 0..14){
            data.add(i, "第${i+1}行")
        }
        val adapter = HomeListAdapter(data)
        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = adapter
    }

}