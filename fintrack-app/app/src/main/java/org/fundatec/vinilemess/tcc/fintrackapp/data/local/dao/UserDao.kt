package org.fundatec.vinilemess.tcc.fintrackapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM USER_TABLE WHERE id=1 LIMIT 1")
    fun findUserByUserSignature(): UserEntity?
}