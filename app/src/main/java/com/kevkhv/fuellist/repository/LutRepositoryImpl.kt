package com.kevkhv.fuellist.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kevkhv.fuellist.dto.Liters
import com.kevkhv.fuellist.dto.Lut
import com.kevkhv.fuellist.repository.entity.LitersEntity
import com.kevkhv.fuellist.repository.entity.LutEntity.Companion.toEntity
import com.kevkhv.fuellist.repository.entity.LutEntity.Companion.toModel
import com.kevkhv.fuellist.repository.entity.toLiters
import com.kevkhv.fuellist.repository.entity.toLitersEntity
import ru.netology.nmedia.db.AppDb

class LutRepositoryImpl(
    private val application: Application
) : LutRepository {


    private val dao = AppDb.getInstance(application).lutDao
    private val litersDao = AppDb.getInstance(application).litersDao


    override fun get(): LiveData<List<Lut>> = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun saveLut(lut: Lut) = dao.insert(lut.toEntity())
    override fun updateLut(id: Int, month: String, startingMileage: Int, residueLitres: Int) =
        dao.updateLut(id, month, startingMileage, residueLitres)


    override fun removeByID(lutId: Int) = dao.removeById(lutId)

    override fun addLiters(liters: Liters) {
        litersDao.insertLiters(liters.toLitersEntity())
        dao.updateAllLiters(liters.lutId)
    }

    override fun getLitersListFromDb(id: Int): LiveData<List<Liters>> {
        return litersDao.getLitersList(id)
            .map { list: List<LitersEntity> -> list.map { LitersEntity -> LitersEntity.toLiters() } }
    }

    override fun updateEndMileage(lutId: Int, endMileage: Int) =
        dao.updateEndMileage(lutId, endMileage)

    override fun getLastLutFromDb(): Lut? {
        return dao.getLutById()?.toModel()
    }

    override fun updateEntMonthLiters(lutId: Int, endMonthLiters: Int) =
        dao.updateEndMonthLiters(lutId, endMonthLiters)

    override fun removeLitersById(liters: Liters) {
        litersDao.removeLitersById(liters.id)
        if (checkNullLitersList(liters.lutId)!=0)dao.updateAllLiters(liters.lutId)
        else dao.updateTotalLitersIfListNulll(liters.lutId,0)
    }

    private fun checkNullLitersList(litersId: Int): Int {
        return litersDao.checkNullLitersList(litersId)
    }

}