package com.hp.sample.logic.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 实体类的类名默认是数据库的表名
 * Author：admin_h on 2021/4/16 13:24
 */
@Entity
data class User(@PrimaryKey var id: Long,
                var name: String,
                var sex: String,
                var age: Int
                )
