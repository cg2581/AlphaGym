package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.model.ProductType
import java.util.*
import kotlin.collections.ArrayList

class ProductTypeACTVAdapter(
    context: Context,
    countryList: List<ProductType>
) :
    ArrayAdapter<ProductType?>(context, 0, countryList) {
    private val countryListFull: List<ProductType>

    init {
        countryListFull = ArrayList(countryList)
    }

    override fun getFilter(): Filter {
        return countryFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.item_autotextcomplete, parent, false)
        }

        val text_view_name: TextView = view!!.findViewById(R.id.text_view_name)


        val countryItem: ProductType? = getItem(position)
        if (countryItem != null) {
            text_view_name.text = countryItem.name
        }



        return view
    }

    private val countryFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results: FilterResults = FilterResults()
            val suggestuions = ArrayList<ProductType>()

            if (constraint == null || constraint.isEmpty()) {
                suggestuions.addAll(countryListFull)
            } else {
                val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()

                countryListFull.forEach { item: ProductType ->
                    if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        suggestuions.add(item)
                    }
                }
            }
            results.values = suggestuions
            results.count = suggestuions.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll((results!!.values) as ArrayList<ProductType>)
            notifyDataSetChanged()

        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as ProductType).name
        }


    }

    override fun getItem(position: Int): ProductType? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}