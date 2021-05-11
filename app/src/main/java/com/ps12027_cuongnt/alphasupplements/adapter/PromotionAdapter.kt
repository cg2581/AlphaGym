package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.promotion.PromotionAddDetailEditFragment
import com.ps12027_cuongnt.alphasupplements.model.Promotion
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class PromotionAdapter(val context: Context, promotionList: ArrayList<Promotion>) :
    RecyclerView.Adapter<PromotionAdapter.ProductViewHolder>(), Filterable {
    var promotionList: ArrayList<Promotion> = ArrayList()
    var filteredList: ArrayList<Promotion> = ArrayList()
    var customFilter: CustomFilter? = null
    var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("-###,### VNĐ")

    init {
        this.promotionList = promotionList
        filteredList = promotionList
        customFilter = CustomFilter(context)
        alphaDAO = AlphaDAO(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_promotion, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val currentItem = filteredList[position]
        holder.tvPromotionID.text = "#".plus(currentItem.id.toString())
        holder.tvPromotionCode.text = currentItem.code
        holder.tvPromotionAmount.text = df.format(currentItem.amount)
        holder.clMain.setOnClickListener {
            (context as MainActivity)!!.switchFragment(
                PromotionAddDetailEditFragment(
                    currentItem.id,
                    "View"
                ), "Replace", "PromotionAddDetailEdit"
            )
        }
        holder.clMain.setOnLongClickListener {
            showDialog(currentItem)
            true
        }
    }

    private fun showDialog(currentItem: Promotion) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_longclick_action_product_type)

        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvView = dialog.findViewById<TextView>(R.id.tvView)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        val tvID = dialog.findViewById<TextView>(R.id.tvID)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        tvName!!.text = currentItem.code
        tvID!!.text = "#".plus(currentItem.id.toString())
        tvView!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                PromotionAddDetailEditFragment(
                    currentItem.id,
                    "View"
                ), "Replace", "PromotionAddDetailEdit"
            )
            dialog.dismiss()
        }
        tvEdit!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                PromotionAddDetailEditFragment(
                    currentItem.id,
                    "Edit"
                ), "Replace", "PromotionAddDetailEdit"
            )
            dialog.dismiss()
        }
        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPromotionID: TextView = itemView.findViewById(R.id.tvProductTypeID)
        val tvPromotionCode: TextView = itemView.findViewById(R.id.tvPromotionCode)
        val tvPromotionAmount: TextView = itemView.findViewById(R.id.tvPromotionAmount)
        val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter? {
        return customFilter
    }

    inner class CustomFilter(val context: Context) : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            val newList =
                promotionList
            val resultList: ArrayList<Promotion> = ArrayList()
            val searchValue = constraint.toString().toLowerCase(Locale.ROOT)
            for (i in newList.indices) {
                val currentItemFilter = newList[i]
                val title =
                    currentItemFilter.code.plus(currentItemFilter.id).plus(currentItemFilter.amount)
                        .plus(currentItemFilter.code).plus(currentItemFilter.endDate)
                if (title.toLowerCase(Locale.ROOT).contains(searchValue)) {
                    resultList.add(currentItemFilter)
                }
            }
            filterResults.count = resultList.size
            filterResults.values = resultList
            return filterResults
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            filteredList =
                results.values as ArrayList<Promotion>
            notifyDataSetChanged()
        }
    }

}