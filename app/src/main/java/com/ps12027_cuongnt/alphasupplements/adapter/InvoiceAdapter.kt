package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.Invoice
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class InvoiceAdapter(val context: Context, invoiceList: ArrayList<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.ProductViewHolder>(), Filterable {
    var invoiceList: ArrayList<Invoice> = ArrayList()
    var filteredList: ArrayList<Invoice> = ArrayList()
    var customFilter: CustomFilter? = null
    var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        this.invoiceList = invoiceList
        filteredList = invoiceList
        customFilter = CustomFilter(context)
        alphaDAO = AlphaDAO(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return ProductViewHolder(itemView)
    }


    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val currentItem = invoiceList[position]
        holder.tvInvoiceID.text = "#".plus(currentItem.invoiceID.toString())
        holder.tvInvoiceCustomer.text = alphaDAO.getUserName(currentItem.customer)
        holder.tvInvoiceTotalPrice.text = df.format(alphaDAO.getInvoiceTotal(currentItem))
        holder.tvInvoiceTime.text = currentItem.date.format(formatter)

    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInvoiceID: TextView = itemView.findViewById(R.id.tvInvoiceID)
        val tvInvoiceCustomer: TextView = itemView.findViewById(R.id.tvInvoiceCustomer)
        val tvInvoiceTotalPrice: TextView = itemView.findViewById(R.id.tvInvoiceTotalPrice)
        val tvInvoiceTime: TextView = itemView.findViewById(R.id.tvInvoiceTime)
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
                invoiceList
            val resultList: ArrayList<Invoice> = ArrayList()
            val searchValue = constraint.toString().toLowerCase(Locale.ROOT)
            for (i in newList.indices) {
                val currentItemFilter = newList[i]
                val title =
                    alphaDAO.getUserName(currentItemFilter.customer).plus(currentItemFilter.date)
                        .plus(currentItemFilter.invoiceID)
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
                results.values as ArrayList<Invoice>
            notifyDataSetChanged()
        }
    }
}

