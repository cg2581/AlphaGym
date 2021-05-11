package com.ps12027_cuongnt.alphasupplements.fragment.invoice

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.CartProductAdapter
import com.ps12027_cuongnt.alphasupplements.adapter.InvoiceDetailAdapter
import com.ps12027_cuongnt.alphasupplements.adapter.ProductAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.cart.CustomerSelectDialog
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.listener.CustomerChoose
import com.ps12027_cuongnt.alphasupplements.model.Invoice
import com.ps12027_cuongnt.alphasupplements.model.InvoiceDetail
import com.ps12027_cuongnt.alphasupplements.model.Product
import com.ps12027_cuongnt.alphasupplements.model.User
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class InvoiceDetailFragment(private val invoiceID: Int) : Fragment() {


    //
    var rcvInvoiceDetail: RecyclerView? = null
    var invoiceDetailList: ArrayList<InvoiceDetail> = ArrayList()
    var cartProductAdapter: InvoiceDetailAdapter? = null
    var currentInvoice: Invoice? = null

    private lateinit var tvTitle: TextView
    private lateinit var llCustomer: LinearLayout
    private lateinit var tvInvoiceID: TextView
    private lateinit var tvCustomerName: TextView
    private lateinit var tvEmployeeName: TextView
    private lateinit var tvTempPrice: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTotal: TextView
    private lateinit var llPromotionTotal: LinearLayout
    private lateinit var alphaDAO: AlphaDAO
    private lateinit var currentUser: User

    var discount = 0
    var tempPrice = 0

    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = LocalDateTime.now().format(formatter)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_invoice_detail, container, false)
        //
        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle.text = "Chi tiết hóa đơn"
        //
        rcvInvoiceDetail = view.findViewById(R.id.rcv)
        tvInvoiceID = view.findViewById(R.id.tvInvoiceID)
        tvCustomerName = view.findViewById(R.id.tvCustomerName)
        tvEmployeeName = view.findViewById(R.id.tvEmployeeName)
        tvTempPrice = view.findViewById(R.id.tvTempPrice)
        tvDiscount = view.findViewById(R.id.tvDiscount)
        tvTotal = view.findViewById(R.id.tvTotal)
        tvDate = view.findViewById(R.id.tvDate)
        llPromotionTotal = view.findViewById(R.id.llPromotionTotal)
        llCustomer = view.findViewById(R.id.llCustomer)
        alphaDAO = AlphaDAO(context!!)
        currentUser = alphaDAO.getUserByID((activity as MainActivity?)!!.currentUserID!!)

        return view
    }

    override fun onResume() {
        super.onResume()
        currentInvoice = alphaDAO.getInvoiceByInvoiceID(invoiceID)
        // Layout
        llCustomer.visibility = View.VISIBLE
        rcvInvoiceDetail!!.visibility = View.VISIBLE

        //edtPromotionCode!!.setText(invoice.promotion)
        tvInvoiceID.text = "Hóa đơn #${currentInvoice!!.invoiceID}"
        tvEmployeeName.text = "NV: ".plus(alphaDAO.getUserByID(currentInvoice!!.seller).fullname)
        tvCustomerName.text = "KH: ".plus(alphaDAO.getUserByID(currentInvoice!!.customer).fullname)
        tvDate.text = "Ngày GD: ".plus(currentInvoice!!.date)

        // Recyclerview
        invoiceDetailList.clear()
        invoiceDetailList.addAll(alphaDAO.getAllInvoiceDetailByInvoiceID(currentInvoice!!.invoiceID)!!)
        /* */
        rcvInvoiceDetail!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        cartProductAdapter =
            InvoiceDetailAdapter(context!!, invoiceDetailList)
        rcvInvoiceDetail!!.adapter = cartProductAdapter
        /*  */
        tempPrice = alphaDAO.getInvoiceTempPrice(currentInvoice!!)
        updateDiscount(currentInvoice!!.promotion)
        updateTempPrice(currentInvoice!!)
        updateTotal()


        //

        //
        updateTotal()

    }


    private fun updateTempPrice(invoice: Invoice) {
        tvTempPrice.text = df.format(alphaDAO.getInvoiceTempPrice(invoice))
    }

    private fun updateTotal() {
        tvTotal.text = df.format((tempPrice - discount))
    }

    private fun updateDiscount(code: String) {
        tvDiscount.text = df.format(alphaDAO.getPromotionByPromotionCode(code)!!.amount)
    }


}
