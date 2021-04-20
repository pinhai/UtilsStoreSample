package com.hp.utilsstoresample.widget.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * 复用RecyclerView的RecycledViewPool缓冲池，适用于加载多个RecyclerView且itemView相同的场景，
 * 比如ViewPager
 * Author：admin_h on 2021/4/20 12:56
 */
object RecycledViewPoolReuse {
    val pools = mutableMapOf<Any, RecyclerView.RecycledViewPool>()

    fun put(key: Any, pool: RecyclerView.RecycledViewPool){
        pools.put(key, pool)
    }

    fun get(key: Any) : RecyclerView.RecycledViewPool?{
        return pools.get(key)
    }

    fun remove(key: Any){
        pools.remove(key)
    }

    fun RecyclerView.putOrGetPool(key: Any){
        val pool = get(key)
        if(pool == null){
            put(key, recycledViewPool)
        }else{
            setRecycledViewPool(pool)
        }
    }

}