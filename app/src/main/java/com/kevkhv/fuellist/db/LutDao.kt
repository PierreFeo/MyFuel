package com.kevkhv.fuellist.db

import androidx.core.location.LocationRequestCompat.Quality
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kevkhv.fuellist.dto.Lut
import com.kevkhv.fuellist.repository.entity.LutEntity

@Dao
interface LutDao {
    @Query("SELECT * FROM lutsTable ORDER BY id DESC")
    fun getAll(): LiveData<List<LutEntity>>

    @Insert
    fun insert(lut: LutEntity)

//    fun save(lut: LutEntity) =
//        if (lut.id == 0)
//            insert(lut)
//        else updateLut(
//            lut.id,
//            lut.month,
//            lut.residueLitres,
//            lut.litresTotal,
//            lut.startingMileage,
//            lut.endMileage
//        )

    @Query(
        "UPDATE lutsTable SET month = :month, residueLitres = :residueLitres, startingMileage = :startingMileage WHERE id = :id"
    )
    fun updateLut(
        id: Int, month: String, startingMileage: Int, residueLitres: Int
    )

    @Query("DELETE FROM lutsTable WHERE id = :id")
    fun removeById(id: Int)

    @Query("UPDATE lutsTable SET litresTotal = (SELECT SUM(liters_count) FROM liters_table WHERE lut_id=:id) WHERE id =:id ")
    fun updateAllLiters(id: Int)

    @Query("UPDATE lutsTable SET endMileage =:endMileage WHERE id =:lutId")
    fun updateEndMileage(lutId: Int, endMileage: Int)

    @Query("SELECT * FROM lutsTable WHERE ID = (SELECT MAX(ID)  FROM lutsTable)")
    fun getLutById(): LutEntity?

    @Query("UPDATE lutsTable SET endMonthLiters=:endMonthLiters WHERE id=:lutId")
    fun updateEndMonthLiters(lutId: Int, endMonthLiters: Int)

    @Query("UPDATE lutsTable SET litresTotal=:litersTotal WHERE id=:lutId")
    fun updateTotalLitersIfListNulll(lutId: Int, litersTotal: Int)
}