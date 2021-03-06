package com.hp.sample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hp.library.utils.LogUtil
import com.hp.library.utils.getClassName
import com.hp.sample.R
import com.hp.sample.adapter.recyclerview.HomeListAdapter
import com.hp.sample.constants.Constants
import com.hp.sample.ui.base.BaseFragment
import com.hp.sample.widget.recyclerview.ImageLoadScrollListener
import com.hp.sample.widget.recyclerview.RecycledViewPoolReuse.putOrGetPool
import kotlinx.android.synthetic.main.fragment_home_list.*

/**
 *
 * Author：admin_h on 2021/4/17 21:41
 */
class HomeListFragment private constructor(): BaseFragment() {

    private lateinit var viewModel: HomeListViewModel
    private lateinit var listAdapter: HomeListAdapter

    private lateinit var mTag: String

    companion object{
        fun newInstance(tag: String): HomeListFragment {
            val bundle = Bundle()
            bundle.putString("tag", tag)
            val fragment = HomeListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTag = arguments?.getString("tag") ?: ""
        LogUtil.d(getClassName(), "onCreate $mTag")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        LogUtil.d(getClassName(), "onCreateView $mTag")
        viewModel = ViewModelProvider(this).get(HomeListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtil.d(getClassName(), "onActivityCreated $mTag")
        initView()
        initObserver()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        LogUtil.d(getClassName(), "onLazyInitView $mTag")
        loadData()
    }

    private fun loadData() {
        viewModel.obtainData(1)
    }

    private fun initObserver() {
        viewModel.listData.observe(this){
            if(it != null){
                listAdapter.setData(it)
                if(viewModel.getCurrentPage() == 1){
                    refreshLayout.finishRefresh()
                }else{
                    if(it.size < HomeListViewModel.COUNT_PER_PAGE){
                        //最后一页
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    }else{
                        refreshLayout.finishLoadMore()
                    }
                }
            }
        }
    }

    private fun initView() {
        refreshLayout.setOnRefreshListener {
            viewModel.obtainData(1)
        }
        refreshLayout.setOnLoadMoreListener{
            viewModel.obtainData()
        }
        listAdapter = HomeListAdapter()
        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = listAdapter
        recycleView.addOnScrollListener(ImageLoadScrollListener(listAdapter))
        recycleView.putOrGetPool(Constants.KEY_POOL_HOME_LIST)
    }

}