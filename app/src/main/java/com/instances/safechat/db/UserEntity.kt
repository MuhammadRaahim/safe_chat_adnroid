package com.instances.safechat.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey val id: String,
    val userName: String,
    val email: String,
    val gender: String,
    val phone: String,
    val address: String,
    val profileImage: String?
        )
