package com.kevkhv.fuellist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevkhv.fuellist.dto.Liters
import com.kevkhv.fuellist.dto.Lut
import com.kevkhv.fuellist.repository.LutRepository
import com.kevkhv.fuellist.repository.LutRepositoryImpl
import com.kevkhv.fuellist.util.SingleLiveEvent
import ru.netology.nmedia.db.AppDb

private val empty = Lut(
    id = 0,
    month = "",
    litresTotal = 0,
    residueLitres = 0,
    startingMileage = 0,
    endMileage = 0,
    endMonthLiters = 0
)

class ViewModel(application: Application) : AndroidViewModel(application) {
//    private val repository: LutRepository =
//        LutRepositoryImpl(dao = AppDb.getInstance(context = application).lutDao)


    private val repository: LutRepository = LutRepositoryImpl(application)

    val data = repository.get()

    val editedLut = MutableLiveData<Lut?>()

    fun save(lut: Lut) {
        if (lut.id != 0) repository.updateLut(
            lut.id,
            lut.month,
            lut.startingMileage,
            lut.residueLitres
        ) else repository.saveLut(lut)
        resetEditLut()
    }

    fun removeByID(lutId: Int) = repository.removeByID(lutId)

    fun editLut(lut: Lut) {
        editedLut.value = lut
    }

    fun addLiters(liters: Liters) = repository.addLiters(liters)

    fun getLitersListLiveData(id: Int): LiveData<List<Liters>> {
        return repository.getLitersListFromDb(id)
    }

    fun addEndMileage(lutId: Int, endMileage: Int) = repository.updateEndMileage(lutId, endMileage)

    fun resetEditLut() {
        editedLut.value = null
    }

    fun getLastLutFromD(): Lut? {
        return repository.getLastLutFromDb()
    }

    fun addEndMonthLiters(lutId: Int, endMonthLiters: Int) =
        repository.updateEntMonthLiters(lutId, endMonthLiters)

    fun removeLiters(liters: Liters){
       repository.removeLitersById(liters)


    }

}