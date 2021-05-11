package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.listener.ProductTypeSelect
import com.ps12027_cuongnt.alphasupplements.model.ProductType
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductTypeSelectAdapter(
    val context: Context,
    productTypeList: ArrayList<ProductType>,
    val listener: ProductTypeSelect
) :
    RecyclerView.Adapter<ProductTypeSelectAdapter.ProductTypeViewHolder?>(), Filterable {
    var productTypeList: ArrayList<ProductType> = ArrayList()
    var filteredList: ArrayList<ProductType> = ArrayList()
    private var customFilter: CustomFilter? = null
    lateinit var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")


    init {
        this.productTypeList = productTypeList
        filteredList = productTypeList
        customFilter = CustomFilter(context)
        alphaDAO = AlphaDAO(context)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductTypeViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product_type, viewGroup, false)
        return ProductTypeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductTypeViewHolder,
        position: Int
    ) {


        val currentItem = filteredList[position]
        holder.tvProductTypeName.text = currentItem.name
        holder.tvProductTypeID.text = "#".plus(currentItem.productTypeID.toString())
        holder.tvProductCount.text =
            alphaDAO.countProductByProductID(currentItem.productTypeID).toString().plus(" sản phẩm")
        holder.clMain.setOnClickListener {
            listener.select(filteredList[position].productTypeID)
            Log.d("Asd", "${filteredList[position].name}")
        }
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter? {
        return customFilter
    }

    inner class ProductTypeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvProductTypeID: TextView = itemView.findViewById(R.id.tvProductTypeID)
        val tvProductTypeName: TextView = itemView.findViewById(R.id.tvProductTypeName)
        val tvProductCount: TextView = itemView.findViewById(R.id.tvProductCount)
        val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain)

    }

    inner class CustomFilter(val context: Context) : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val alphaDAO = AlphaDAO(context)
            val filterResults = FilterResults()
            val newList =
                productTypeList
            val resultList: ArrayList<ProductType> = ArrayList()
            val searchValue = constraint.toString().toLowerCase(Locale.ROOT)
            for (i in newList.indices) {
                val currentItemFilter = newList[i]
                val title = currentItemFilter.name.plus(currentItemFilter.productTypeID)
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
                results.values as ArrayList<ProductType>
            notifyDataSetChanged()
        }
    }

}
