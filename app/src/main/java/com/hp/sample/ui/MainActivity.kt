package com.hp.sample.ui

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.hp.library.widget.bottombar.BottomBar
import com.hp.library.widget.bottombar.BottomBarTab
import com.hp.sample.R
import com.hp.sample.ui.actions.ActionsFragment
import com.hp.sample.ui.base.BaseActivity
import com.hp.sample.ui.home.HomeFragment
import com.hp.sample.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportFragment

class MainActivity : BaseActivity() {

    private val mFragments = arrayOfNulls<SupportFragment>(3)

    private val FIRST = 0
    private val SECOND = 1
    private val THIRD = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImmersionBar.with(this)
            .init()

        if (savedInstanceState == null) {
            mFragments[FIRST] = HomeFragment.newInstance()
            mFragments[SECOND] = ActionsFragment.newInstance()
            mFragments[THIRD] = NotificationsFragment.newInstance()
            loadMultipleRootFragment(
                R.id.fl_tab_container, FIRST,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD],
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findFragment(HomeFragment::class.java)
            mFragments[SECOND] = findFragment(ActionsFragment::class.java)
            mFragments[THIRD] = findFragment(NotificationsFragment::class.java)
        }

        initView()
    }

    private fun initView() {
        bottomBar.addItem(BottomBarTab(this, R.drawable.selector_tab1, getString(R.string.title_home)))
            .addItem(BottomBarTab(this, R.drawable.selector_tab2, getString(R.string.title_actions)))
            .addItem(BottomBarTab(this, R.drawable.selector_tab3, getString(R.string.title_notifications)))
        bottomBar.setOnTabSelectedListener(onTabSelectedListener)
        bottomBar.setCurrentItem(0)
    }

    private val onTabSelectedListener = object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])
                if(position == 0){
                    ImmersionBar.with(this@MainActivity).statusBarDarkFont(false).init()
                }else{
                    ImmersionBar.with(this@MainActivity).statusBarDarkFont(true).init()
                }
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {
            }
        }
}