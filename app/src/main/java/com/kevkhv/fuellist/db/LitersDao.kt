package com.kevkhv.fuellist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kevkhv.fuellist.repository.entity.LitersEntity

@Dao
interface LitersDao {
    @Insert
    fun insertLiters(liters: LitersEntity)

    @Query("SELECT * FROM liters_table WHERE lut_id =:id ORDER BY id DESC")
    fun getLitersList(id: Int): LiveData<List<LitersEntity>>

    @Query("DELETE FROM liters_table WHERE id = :litersId")
    fun removeLitersById(litersId: Int)

    @Query("SELECT COUNT(*) FROM liters_table WHERE lut_id =:id")
    fun checkNullLitersList(id: Int): Int
}
