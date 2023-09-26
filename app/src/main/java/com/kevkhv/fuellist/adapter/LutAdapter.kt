package com.kevkhv.fuellist.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kevkhv.fuellist.R
import com.kevkhv.fuellist.databinding.CardLutBinding
import com.kevkhv.fuellist.dto.Lut
import kotlin.math.roundToInt

interface LutInteractionListener {
    fun onLutEditClicked(lut: Lut)
    fun onLutRemoveClicked(lut: Lut)
    fun showAddFuelDialog(lutId: Int)
    fun showBottomSheetWithLiters(lutId: Int)
    fun showAddMileageDialog(lut: Lut)
    fun showAddEndMonthLitersDialog(lut: Lut)
    fun onClickRoot()


}

class LutAdapter(
    private val onInteractionListener: LutInteractionListener
) :
    ListAdapter<Lut, LutViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LutViewHolder {
        val binding = CardLutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LutViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: LutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Lut>() {

        override fun areItemsTheSame(oldItem: Lut, newItem: Lut) =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Lut, newItem: Lut) =
            oldItem == newItem
    }
}

class LutViewHolder(
    private val binding: CardLutBinding,
    private val onInteractionListener: LutInteractionListener

) : RecyclerView.ViewHolder(binding.root) {


    fun bind(lut: Lut) {


        binding.apply {
            titleView.text = lut.month
            textViewValue.text = lut.startingMileage.toString()
            textView2Value.text = lut.endMileage.toString()
            //remaining.text = "Остакток в баке на начло месяца: ${lut.residueLitres}"
            remainingEndValue.text = lut.endMonthLiters.toString()
            textView3Value.text = lut.litresTotal.toString()
            mileagePeriodValue.mileagePeriod(lut)
            textView4Value.switchColor(lut)
            addFuelButton.setOnClickListener {
                onInteractionListener.showBottomSheetWithLiters(lut.id)
            }


            addLitersButton.setOnClickListener {
                onInteractionListener.showAddFuelDialog(lut.id)
            }

            editValueMil.setOnClickListener {
                onInteractionListener.showAddMileageDialog(lut)
            }

            addRemainderButton.setOnClickListener {
                onInteractionListener.showAddEndMonthLitersDialog(lut)
            }




            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_lut)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onLutRemoveClicked(lut)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onLutEditClicked(lut)
                                Log.d("click", "i click edit lut")
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    private fun TextView.switchColor(lut: Lut) {

        if (lut.endMileage > 0 && lut.litresTotal > 0) {
            val standart: Double =


                (lut.litresTotal - (lut.endMonthLiters - lut.residueLitres)) * 100 / (lut.endMileage - lut.startingMileage).toDouble()
            text = String.format("%.3f", standart)


            setTextColor(
                if (standart >= 10.068) {
                    resources.getColor(R.color.red)
                } else if (standart <= 10.068) {
                    resources.getColor(R.color.green)
                } else {
                    resources.getColor(R.color.mainColor)
                }
            )


//            setTextColor(
//                if (standart < 12) resources.getColor(R.color.green) else resources.getColor(
//                    R.color.red
//                )
//            )
//

        } else {
            text = "N/A"

        }
    }

    private fun TextView.mileagePeriod(lut: Lut) {
        val mileagePeriod = if (lut.endMileage > 0 && lut.startingMileage > 0) {
            (lut.endMileage - lut.startingMileage).toString()
        } else "N/A"
        text = mileagePeriod
    }

    private fun roundDouble(double: Double): Double {
        return if (double.isNaN()) 0.0 else (double * 100).roundToInt() / 100.0
    }
}