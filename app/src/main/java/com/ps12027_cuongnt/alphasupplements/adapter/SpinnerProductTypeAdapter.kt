package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.model.ProductType


class SpinnerProductTypeAdapter(
    context: Context,
    productTypeList: ArrayList<ProductType>
) :
    ArrayAdapter<ProductType?>(context, 0, productTypeList as List<ProductType?>) {
    private val productTypeList: ArrayList<ProductType>
    var tvName: TextView? = null
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.spinner_category, null)
        }
        val productType: ProductType = productTypeList[position]
        if (productType != null) {
            tvName = view!!.findViewById(R.id.tvName)
            tvName!!.text = productType!!.name
        }
        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    init {
        this.productTypeList = productTypeList
    }
}
