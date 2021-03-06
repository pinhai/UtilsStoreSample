package com.hp.sample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.hp.sample.R
import com.hp.sample.adapter.viewpager.MyFragmentPagerAdapter
import com.hp.sample.ui.base.BaseFragment
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
        //这里应该是CoordinatorLayout的一个bug，计算下面的布局高度会比实际需要的多（比如ViewPager，NestedScrollView）
        //它应该是把CollapsingToolbarLayout折叠后的高度计算为0了
        toolbar_layout.minimumHeight = ImmersionBar.getActionBarHeight(activity!!) +
                ImmersionBar.getStatusBarHeight(activity!!)

        toolbar.title = "我是标题"

        val fragments = arrayListOf(
            HomeListFragment.newInstance("tag1"),
            HomeListFragment.newInstance("tag2"),
            HomeListFragment.newInstance("tag3"),
            HomeListFragment.newInstance("tag4")
        )
        val pagerAdapter = MyFragmentPagerAdapter(childFragmentManager, fragments)
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = fragments.size - 1
        tabs.setupWithViewPager(viewpager)
        tabs.getTabAt(0)?.setText(R.string.tab1)
        tabs.getTabAt(1)?.setText(R.string.tab2)
        tabs.getTabAt(2)?.setText(R.string.tab3)
        tabs.getTabAt(3)?.setText(R.string.tab4)
    }
}