package com.hp.utilsstoresample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.adapter.viewpager.MyFragmentPagerAdapter
import com.hp.utilsstoresample.ui.base.BaseFragment
import com.hp.utilsstoresample.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment private constructor(): BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ImmersionBar.setTitleBar(activity, toolbar)
        //因为toolbar增加了状态栏的高度
        toolbar_layout.minimumHeight = ImmersionBar.getActionBarHeight(activity!!) +
                ImmersionBar.getStatusBarHeight(activity!!)

        toolbar.title = "我是标题"

        val fragments = arrayListOf(HomeListFragment.newInstance(),
            HomeListFragment.newInstance(), HomeListFragment.newInstance(),
            HomeListFragment.newInstance()
        )
        val pagerAdapter = MyFragmentPagerAdapter(childFragmentManager, fragments)
        viewpager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewpager)
        tabs.getTabAt(0)?.setText(R.string.tab1)
        tabs.getTabAt(1)?.setText(R.string.tab2)
        tabs.getTabAt(2)?.setText(R.string.tab3)
        tabs.getTabAt(3)?.setText(R.string.tab4)
    }
}