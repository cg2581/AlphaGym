package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.model.InvoiceDetail
import java.text.DecimalFormat

class InvoiceDetailAdapter(
    val context: Context,
    val invoiceDetailArrayList: ArrayList<InvoiceDetail>
) :
    RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailViewHolder>() {


    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceDetailViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invoice_detail, parent, false)
        return InvoiceDetailViewHolder(itemView)
    }

    override fun getItemCount(): Int = invoiceDetailArrayList.size

    override fun onBindViewHolder(holder: InvoiceDetailViewHolder, position: Int) {
        val currentItem = invoiceDetailArrayList[position]
        val alphaDAO = AlphaDAO(context)
        val product = alphaDAO.getProductByID(currentItem.productID)
        holder.tvName.text = product.name
        holder.tvAmount.text = invoiceDetailArrayList[position].amount.toString().plus(" x ").plus(product.price)
        holder.tvPrice.text = df.format(invoiceDetailArrayList[position].amount * product.price)
        holder.clMain.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                ProductDetailFragment(
                    currentItem.productID
                ), "Replace", "ProductDetail"
            )
        }
        val image = alphaDAO.getProductByID(currentItem.productID).image
        if (image != null) {
            val bitmap =
                BitmapFactory.decodeByteArray(image, 0, image!!.size)
            holder.ivProductLogo.setImageBitmap(bitmap)
        } else {
            holder.ivProductLogo.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }


    }


    class InvoiceDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val ivProductLogo: ImageView = itemView.findViewById(R.id.ivProductLogo)
        val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain)
    }

}