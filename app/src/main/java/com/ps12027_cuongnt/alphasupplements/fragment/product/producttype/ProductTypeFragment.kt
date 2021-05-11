package com.ps12027_cuongnt.alphasupplements.fragment.product.producttype

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.ProductTypeAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.listener.ListenFromActivity
import com.ps12027_cuongnt.alphasupplements.listener.RecyclerTouchListener
import com.ps12027_cuongnt.alphasupplements.model.ProductType


class ProductTypeFragment : Fragment, ProductTypeAdapter.ProductTypeEventListener {
    private var rcv: RecyclerView? = null
    private var productTypeArrayList: ArrayList<ProductType> = ArrayList()
    private lateinit var actv: AutoCompleteTextView
    lateinit var productTypeAdapter: ProductTypeAdapter
    var fab: FloatingActionButton? = null
    lateinit var btnAdd: Button

    var alphaDAO: AlphaDAO? = null

    //
    var layoutType = 0
    lateinit var llHead: LinearLayout

    //
    constructor() : super()
    constructor(layoutType: Int) : super() {
        this.layoutType = layoutType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rcv, container, false)
        //
        llHead = view.findViewById(R.id.llHead)
        if (layoutType != 0) {
            llHead.visibility = View.GONE
        }
        //
        btnAdd = view.findViewById(R.id.btnAdd)
        rcv = view.findViewById(R.id.rcv)
        alphaDAO = AlphaDAO(context!!)
        actv = view.findViewById(R.id.actv)
        fab = view.findViewById(R.id.fab)
        productTypeArrayList.clear()
        productTypeArrayList.addAll(alphaDAO!!.getAllProductType()!!)
        rcv!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        /* */
        productTypeAdapter =
            ProductTypeAdapter(context!!, productTypeArrayList, this)
        rcv!!.adapter = productTypeAdapter



        if ((alphaDAO!!.getUserByID((activity as MainActivity?)!!.currentUserID!!)).type != 1) {
            btnAdd!!.visibility = View.GONE
        } else {
            btnAdd!!.setOnClickListener {
                (activity as MainActivity?)!!.switchFragment(
                    ProductTypeAddDetailEditFragment(0, "Add"),
                    "Replace", "ProductTypeAddDetailEdit"
                )

            }
        }
        actv.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    actv.addTextChangedListener(object : TextWatcher {
                        override fun onTextChanged(
                            s: CharSequence,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            productTypeAdapter.filter!!.filter(s)
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
                }
            }
        }
        return view
    }

    override fun removeProductType(productType: ProductType) {
        productTypeArrayList.remove(productType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}