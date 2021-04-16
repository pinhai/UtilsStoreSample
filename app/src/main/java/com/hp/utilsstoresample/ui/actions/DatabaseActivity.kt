package com.hp.utilsstoresample.ui.actions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.hp.utils_store.utils.ToastUtil
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.logic.database.entity.User
import com.hp.utilsstoresample.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_database.*

class DatabaseActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(DatabaseViewModel::class.java) }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, DatabaseActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        initView()
    }

    private fun initView() {
        btn_add.setOnClickListener(this)
        btn_deleteById.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        btn_selectById.setOnClickListener(this)
        btn_selectAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_add -> {
                insertUser()
            }
            R.id.btn_deleteById -> {
            }
            R.id.btn_update -> {
            }
            R.id.btn_selectById -> {
                selectById()
            }
            R.id.btn_selectAll -> {
            }
        }
    }

    private fun selectById() {
        val id = et_id.text?.trim()?.toString()
        if(TextUtils.isEmpty(id)){
            ToastUtil.show(this, "id为空！")
            return
        }
        viewModel.selectUserById(this, id!!.toLong())
            .observe(this){
                if(it.isSuccess){
                    val u = it.getOrNull()!!
                    ToastUtil.show(this, "${u.id},${u.name},${u.sex},${u.age}")
                } else ToastUtil.show(this, "查询失败")
            }
    }

    private fun insertUser() {
        val id = et_id.text?.trim()?.toString()
        val name = et_name.text?.trim()?.toString()
        val sex = et_sex.text?.trim()?.toString()
        val age = et_age.text?.trim()?.toString()
        if(TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(age)){
            ToastUtil.show(this, "有字段为空！")
            return
        }
        viewModel.insertUser(this, User(id!!.toLong(), name!!, sex!!, age!!.toInt()))
            .observe(this){
                if(it.isSuccess) ToastUtil.show(this, "新增成功")
                else ToastUtil.show(this, "新增失败")
            }
    }
}