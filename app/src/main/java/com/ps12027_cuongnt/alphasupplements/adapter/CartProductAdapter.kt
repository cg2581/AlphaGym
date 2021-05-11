package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.model.InvoiceDetail
import java.text.DecimalFormat

class CartProductAdapter(
    val context: Context,
    val invoiceDetailArrayList: ArrayList<InvoiceDetail>,
    val listener: EventListener
) :
    RecyclerView.Adapter<CartProductAdapter.InvoiceDetailViewHolder>() {

    interface EventListener {
        fun callUpdate()
    }

    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceDetailViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return InvoiceDetailViewHolder(itemView)
    }

    override fun getItemCount(): Int = invoiceDetailArrayList.size

    override fun onBindViewHolder(holder: InvoiceDetailViewHolder, position: Int) {
        val currentItem = invoiceDetailArrayList[position]
        val alphaDAO = AlphaDAO(context)
        val product = alphaDAO.getProductByID(currentItem.productID)
        holder.tvName.text = product!!.name
        holder.tvPrice.text = df.format(product.price)
        holder.tvAmount.text = currentItem.amount.toString()
        holder.ivMinus.setOnClickListener {
            var currentAmount: Int = holder.tvAmount.text.toString().toInt()
            if (currentAmount != 1) {
                currentAmount--
                holder.tvAmount.text = currentAmount.toString()
                currentItem.amount = currentAmount
                alphaDAO.updateInvoiceDetailAmount(currentItem)
                notifyDataSetChanged()
                listener.callUpdate()
            }
        }
        holder.ivPlus.setOnClickListener {
            var currentAmount: Int = holder.tvAmount.text.toString().toInt()
            if (currentAmount != 10) {
                currentAmount++
                holder.tvAmount.text = currentAmount.toString()
                currentItem.amount = currentAmount
                alphaDAO.updateInvoiceDetailAmount(currentItem)
                notifyDataSetChanged()
                listener.callUpdate()
            }
        }
        holder.tvAmount.setOnClickListener {
            Toast.makeText(
                context,
                "Show Dialog to get amount input!",
                Toast.LENGTH_SHORT
            ).show()
        }
        holder.ivDelete.setOnClickListener {
            invoiceDetailArrayList.remove(invoiceDetailArrayList[position])
            notifyDataSetChanged()
            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
            alphaDAO.deleteInvoiceDetailByID(currentItem.invoiceDetailID)
        }
        holder.tvName.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                ProductDetailFragment(
                    currentItem.productID
                ), "Replace","ProductDetail"
            )
        }
        val image = alphaDAO.getProductByID(currentItem.productID).image
        if (image != null) {
            val bitmap =
                BitmapFactory.decodeByteArray(image, 0, image!!.size)
            holder.ivProduct.setImageBitmap(bitmap)
        } else {
            holder.ivProduct.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }


    }


    class InvoiceDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val ivMinus: ImageView = itemView.findViewById(R.id.ivMinus)
        val ivPlus: ImageView = itemView.findViewById(R.id.ivPlus)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val ivProduct: ImageView = itemView.findViewById(R.id.ivLogo)
        val llCartProduct: LinearLayout = itemView.findViewById(R.id.llCartProduct)
    }

}