package com.hp.utilsstoresample.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hp.utilsstoresample.R

/**
 * Author：admin_h on 2021/4/17 22:02
 */
class HomeListAdapter() : RecyclerView.Adapter<HomeListAdapter.MyViewHolder>() {

    private var data: List<String> = emptyList()

    fun setData(data: List<String>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_home_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_message: TextView = itemView.findViewById(R.id.tv_message)

        fun bind(position: Int){
            tv_message.setText(data[position])
        }
    }

}