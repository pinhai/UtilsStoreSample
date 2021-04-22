package com.hp.sample.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class HomeListViewModel : ViewModel() {
    companion object{
        //每页的item数
        const val COUNT_PER_PAGE = 15
    }
    private val currentPage = MutableLiveData(1)

    private var listDataTemp: MutableList<String>? = null
    val listData = Transformations.map(currentPage){
        //模拟网络请求
        val data = mutableListOf<String>()
        for(i in 0 until 15){
            data.add(i, "第${i + 1 + (it - 1) * COUNT_PER_PAGE}行")
        }

        if(it > 1){
            listDataTemp?.addAll(data)
            listDataTemp
        }else{
            data
        }
    }

    fun obtainData(page: Int = this.currentPage.value!!.plus(1)){
        listDataTemp = listData?.value
        this.currentPage.value = page
    }

    fun getCurrentPage(): Int {
        return currentPage.value ?: 1
    }
}
