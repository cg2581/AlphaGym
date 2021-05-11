package com.ps12027_cuongnt.alphasupplements.fragment.invoice

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.InvoiceAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.producttype.ProductTypeAddDetailEditFragment
import com.ps12027_cuongnt.alphasupplements.listener.RecyclerTouchListener
import com.ps12027_cuongnt.alphasupplements.model.Invoice


class InvoiceFragment : Fragment() {


    private var rcv: RecyclerView? = null
    private var alphaDAO: AlphaDAO? = null
    private var invoiceList: ArrayList<Invoice> = ArrayList()
    private var invoiceAdapter: InvoiceAdapter? = null
    private lateinit var actv: AutoCompleteTextView
    private lateinit var llHead: LinearLayout
    private lateinit var btnAdd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rcv, container, false)
        actv = view.findViewById(R.id.actv)
        alphaDAO = AlphaDAO(context!!)
        llHead = view.findViewById(R.id.llHead)
        btnAdd = view.findViewById(R.id.btnAdd)
        llHead.visibility = View.GONE
        btnAdd.visibility = View.GONE
        rcv = view.findViewById(R.id.rcv)
        rcv!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        invoiceList = alphaDAO!!.getAllInvoice()!!
        invoiceAdapter = InvoiceAdapter(context!!, invoiceList)
        rcv!!.adapter = invoiceAdapter
        val touchListener = RecyclerTouchListener(context as Activity?, rcv)
        touchListener
            .setClickable(object : RecyclerTouchListener.OnRowClickListener {
                override fun onRowClicked(position: Int) {
                    (activity as MainActivity).switchFragment(
                        InvoiceDetailFragment(invoiceList[position].invoiceID),
                        "Replace",
                        "InvoiceDetail"
                    )
                }

                override fun onIndependentViewClicked(
                    independentViewID: Int,
                    position: Int
                ) {
                }
            })

        rcv!!.addOnItemTouchListener(touchListener)
        actv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                invoiceAdapter!!.filter!!.filter(s)
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        return view
    }

    override fun onResume() {
        super.onResume()
        val newInvoiceList = alphaDAO!!.getAllInvoice()!!
        if (newInvoiceList != invoiceList) {
            invoiceAdapter = InvoiceAdapter(context!!, newInvoiceList)
            rcv!!.adapter = invoiceAdapter
        }
    }

    fun update() {
        rcv!!.adapter = invoiceAdapter
    }

}