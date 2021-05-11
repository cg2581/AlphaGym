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
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.model.Product
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(
    val context: Context,
    productList: ArrayList<Product>,
    val listener: ProductEventListener
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder?>(), Filterable {

    interface ProductEventListener {
        fun removeProduct(product: Product)
    }

    var productList: ArrayList<Product> = ArrayList()
    var filteredList: ArrayList<Product> = ArrayList()
    private var customFilter: CustomFilter? = null
    var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")


    init {
        this.productList = productList
        filteredList = productList
        customFilter = CustomFilter(context)
        alphaDAO = AlphaDAO(context)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product, viewGroup, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val currentItem = filteredList[position]
        holder.tvProductName.text = currentItem.name
        holder.tvProductType.text = alphaDAO.getProductTypeName(currentItem.type)
        holder.tvProductPrice.text = df.format(currentItem.price)

        if (currentItem.image != null) {
            holder.ivProductLogo.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    currentItem.image,
                    0,
                    currentItem.image!!.size
                )
            )
        } else {
            holder.ivProductLogo.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }
        holder.clMain.setOnClickListener {
            (context as MainActivity).switchFragment(
                ProductDetailFragment(currentItem.productID),
                "Replace", "ProductDetail"
            )
        }
        if ((context as MainActivity).currentUserType == 1) {
            holder.clMain.setOnLongClickListener {
                showDialog(currentItem)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter? {
        return customFilter
    }

    inner class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val ivProductLogo: ImageView = itemView.findViewById(R.id.ivProductLogo)
        val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain)
        val tvProductType: TextView = itemView.findViewById(R.id.tvProductType)

    }

    inner class CustomFilter(val context: Context) : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val alphaDAO = AlphaDAO(context)
            val filterResults = FilterResults()
            val newList =
                productList
            val resultList: ArrayList<Product> = ArrayList()
            val searchValue = constraint.toString().toLowerCase(Locale.ROOT)
            for (i in newList.indices) {
                val currentItemFilter = newList[i]
                val title = currentItemFilter.name.plus(currentItemFilter.productID)
                    .plus(currentItemFilter.description)
                    .plus(alphaDAO.getProductTypeName(currentItemFilter.type))
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
                results.values as ArrayList<Product>
            notifyDataSetChanged()
        }
    }

    private fun showDialog(currentItem: Product) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_longclick_action)

        val tvProductName = dialog.findViewById<TextView>(R.id.tvName)
        val tvView = dialog.findViewById<TextView>(R.id.tvView)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)
        val ivProduct = dialog.findViewById<ImageView>(R.id.ivLogo)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)

        tvProductName!!.text = currentItem.name
        if (currentItem.image != null) {
            ivProduct!!.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    currentItem.image,
                    0,
                    currentItem.image!!.size
                )
            )
        } else {
            ivProduct!!.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }
        tvView!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                ProductDetailFragment(currentItem.productID),
                "Replace", "ProductDetail"
            )
            dialog.dismiss()
        }
        tvEdit!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                ProductAddEditFragment(
                    currentItem.productID, "Edit"
                ),
                "Replace", "ProductAddEdit"
            )
            dialog.dismiss()
        }
        if (currentItem.use == 1) {
            tvDelete!!.text = "Ngưng kinh doanh"
        } else {
            tvDelete!!.text = "Khôi phục kinh doanh"
        }
        tvDelete.setOnClickListener {
            tvDelete.text = "Ấn 1 lần nữa để xác nhận"
            tvDelete.setOnClickListener {
                if (currentItem.use == 1) {
                    currentItem.use = 0
                } else {
                    currentItem.use = 1
                }
                if (alphaDAO.updateProduct(currentItem)) {
                    Toast.makeText(
                        context,
                        "Cập nhật thành công!",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
