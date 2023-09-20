package com.kevkhv.fuellist.repository

import androidx.lifecycle.LiveData
import com.kevkhv.fuellist.dto.Liters
import com.kevkhv.fuellist.dto.Lut

interface LutRepository {
    fun get(): LiveData<List<Lut>>
    fun saveLut(lut: Lut)
    fun updateLut(id: Int, month: String, startingMileage: Int, residueLitres: Int)
    fun removeByID(lutId: Int)
    fun addLiters(liters: Liters)
    fun getLitersListFromDb(id: Int): LiveData<List<Liters>>
    fun updateEndMileage(lutId: Int, endMileage: Int)
    fun getLastLutFromDb():Lut?
    fun updateEntMonthLiters(lutId: Int,endMonthLiters:Int)
    fun removeLitersById(liters: Liters)


}