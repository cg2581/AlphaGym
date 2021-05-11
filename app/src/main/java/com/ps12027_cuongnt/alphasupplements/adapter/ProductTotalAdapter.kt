package com.ps12027_cuongnt.alphasupplements.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.manage.StatisticFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.model.Product
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductTotalAdapter(
    val context: Context,
    val list: ArrayList<StatisticFragment.ProductData>
) :
    RecyclerView.Adapter<ProductTotalAdapter.ProductViewHolder?>() {


    var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")


    init {
        alphaDAO = AlphaDAO(context)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product_total, viewGroup, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val currentItem = list[position]
        holder.tvName.text = "${position + 1}. ${currentItem.name}"
        holder.tvTotal.text = df.format(currentItem.total)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)

    }

}
