package com.ps12027_cuongnt.alphasupplements.fragment.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.CartProductAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.producttype.ProductTypeSelectDialog
import com.ps12027_cuongnt.alphasupplements.listener.CustomerChoose
import com.ps12027_cuongnt.alphasupplements.model.Invoice
import com.ps12027_cuongnt.alphasupplements.model.InvoiceDetail
import com.ps12027_cuongnt.alphasupplements.model.Product
import com.ps12027_cuongnt.alphasupplements.model.User
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CartFragment : Fragment(), CartProductAdapter.EventListener, CustomerChoose {

    lateinit var llHead: LinearLayout

    //
    var rcvInvoiceDetail: RecyclerView? = null
    var invoiceDetailList: ArrayList<InvoiceDetail> = ArrayList()
    var cartProductAdapter: CartProductAdapter? = null
    var currentInvoice: Invoice? = null

    private lateinit var llNew: LinearLayout
    private lateinit var llCustomer: LinearLayout
    private lateinit var tvInvoiceID: TextView
    private lateinit var tvCustomerName: TextView
    private lateinit var tvChange: TextView
    private lateinit var tvTempPrice: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var llPromotionTotal: LinearLayout
    private lateinit var btnPromotionApply: Button
    private lateinit var btnPay: Button
    private lateinit var btnNew: Button
    private lateinit var llPay: LinearLayout
    private lateinit var edtPromotionCode: EditText
    private lateinit var alphaDAO: AlphaDAO
    private lateinit var currentUser: User
    private var invoice: Invoice? = null

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
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        //
        llHead = view.findViewById(R.id.llHead)
        llHead.visibility = View.GONE
        //
        rcvInvoiceDetail = view.findViewById(R.id.rcv)
        llNew = view.findViewById(R.id.llNew)
        tvInvoiceID = view.findViewById(R.id.tvInvoiceID)
        tvCustomerName = view.findViewById(R.id.tvCustomerName)
        tvChange = view.findViewById(R.id.tvChange)
        tvTempPrice = view.findViewById(R.id.tvTempPrice)
        tvDiscount = view.findViewById(R.id.tvDiscount)
        tvTotal = view.findViewById(R.id.tvTotal)
        llPromotionTotal = view.findViewById(R.id.llPromotionTotal)
        llPay = view.findViewById(R.id.llPay)
        btnPromotionApply = view.findViewById(R.id.btnPromotionApply)
        btnPay = view.findViewById(R.id.btnPay)
        llCustomer = view.findViewById(R.id.llCustomer)
        edtPromotionCode = view.findViewById(R.id.edtPromotionCode)
        btnNew = view.findViewById(R.id.btnNew)
        alphaDAO = AlphaDAO(context!!)
        currentUser = alphaDAO.getUserByID((activity as MainActivity?)!!.currentUserID!!)

        return view
    }

    override fun onResume() {
        super.onResume()
        check()

    }

    private fun check() {

        invoice = alphaDAO.getInvoiceBySellerIDAndStatus(
            currentUser.userID,
            0
        )

        if (invoice == null) {
            llNew.visibility = View.VISIBLE
            llPay.visibility = View.GONE
            llCustomer.visibility = View.GONE
            rcvInvoiceDetail!!.visibility = View.GONE
            btnNew.setOnClickListener {
                //(activity as MainActivity?)!!.switchFragment(Cart_New_Fragment(), "Replace")
                val invoice = Invoice(
                    0,
                    LocalDateTime.now().format(formatter),
                    "",
                    (activity as MainActivity?)!!.currentUserID!!,
                    0,
                    0
                )
                alphaDAO.addInvoice(invoice)
                check()
            }

        } else {
            currentInvoice = invoice
            (activity as MainActivity?)!!.currentInvoiceID = invoice!!.invoiceID
            // Layout
            llNew.visibility = View.GONE
            llPay.visibility = View.VISIBLE
            llCustomer.visibility = View.VISIBLE
            rcvInvoiceDetail!!.visibility = View.VISIBLE
            //

            //edtPromotionCode!!.setText(invoice.promotion)
            tvInvoiceID.text = "Đơn hàng #${invoice!!.invoiceID}"
            tvCustomerName.text = alphaDAO.getUserByID(invoice!!.customer).fullname

            // Recyclerview
            invoiceDetailList.clear()
            invoiceDetailList.addAll(alphaDAO.getAllInvoiceDetailByInvoiceID(invoice!!.invoiceID)!!)
            /* */
            rcvInvoiceDetail!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            cartProductAdapter =
                CartProductAdapter(context!!, invoiceDetailList, this)
            rcvInvoiceDetail!!.adapter = cartProductAdapter
            /*  */
            edtPromotionCode.setText(invoice!!.promotion)
            tempPrice = alphaDAO.getInvoiceTempPrice(invoice!!)
            val promotion = alphaDAO.getPromotionByPromotionCode(currentInvoice!!.promotion)
            if (promotion != null) {
                discount = promotion.amount
            } else {
                discount = 0
            }
            updateDiscount(discount)
            updateTempPrice(invoice!!)
            updateTotal()


            //

            tvChange.setOnClickListener {
                val dialog = CustomerSelectDialog(this)
                dialog.show(childFragmentManager, "CustomerSelectDialog")
            }

            //
            btnPromotionApply.setOnClickListener {
                val promotion =
                    alphaDAO.getPromotionByPromotionCode(edtPromotionCode.text.toString())
                if (promotion != null) {
                    discount = promotion.amount
                    if (promotion.startDate > LocalDateTime.now().format(formatter)
                    ) {
                        Log.d("Time", "END: ${promotion.startDate} ${LocalDateTime.now().format(formatter)}")
                        Toast.makeText(context, "Mã giảm giá đã hết hạn!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (promotion.endDate < LocalDateTime.now().format(formatter)) {
                        Log.d("Time", "END: ${promotion.endDate} ${LocalDateTime.now().format(formatter)}")
                        Toast.makeText(context, "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (alphaDAO.getInvoiceTempPrice(invoice!!) == 0) {
                        Toast.makeText(
                            context,
                            "Chưa có sản phẩm trong giỏ hàng",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        invoice!!.promotion = edtPromotionCode.text.toString()
                        alphaDAO.updateInvoice(invoice!!)
                        tvDiscount.setTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.LimeGreen
                            )
                        )
                        updateDiscount(discount)
                    }
                } else {
                    discount = 0
                    Toast.makeText(
                        context,
                        "Mã giảm giá không hợp lệ!",
                        Toast.LENGTH_SHORT
                    ).show()
                    tvDiscount.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.Black
                        )
                    )
                }
                updateTotal()

                (activity as MainActivity).hideKeyboard()
            }
            updateTotal()

        }
        btnPay.setOnClickListener {
            invoice!!.date = LocalDateTime.now().format(formatter)
            if (invoiceDetailList.size != 0) {
                if (alphaDAO.confirmInvoice(invoice!!)) {
                    Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show()
                    check()
                } else {
                    Toast.makeText(context, "Lỗi không xác định", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Chưa có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


    private fun updateTempPrice(invoice: Invoice) {
        tvTempPrice.text = df.format(alphaDAO.getInvoiceTempPrice(invoice))
    }

    private fun updateTotal() {
        tvTotal.text = df.format((tempPrice - discount))
    }

    private fun updateDiscount(discount: Int) {
        tvDiscount.text = df.format(discount)
    }

    override fun callUpdate() {
        tempPrice = alphaDAO.getInvoiceTempPrice(currentInvoice!!)
        updateTempPrice(currentInvoice!!)
        updateTotal()
    }

    override fun chooseType(userID: Int) {
        invoice!!.customer = userID
        alphaDAO.updateInvoice(invoice!!)
        tvCustomerName.text = alphaDAO.getUserName(userID)
    }


}
