package com.hp.sample.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hp.sample.R
import com.squareup.picasso.Picasso

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
        private val iv_header: ImageView = itemView.findViewById(R.id.iv_header)

        fun bind(position: Int){
            tv_message.setText(data[position])
            Picasso.get().load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fyouimg1.c-ctrip.com%2Ftarget%2Ftg%2F035%2F063%2F726%2F3ea4031f045945e1843ae5156749d64c.jpg&refer=http%3A%2F%2Fyouimg1.c-ctrip.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621441482&t=be1f90a00b6904b4952b82ecfeaf7f0d")
                .tag(this@HomeListAdapter)
                .into(iv_header)
        }
    }

}