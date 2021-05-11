package com.ps12027_cuongnt.alphasupplements.fragment.product.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.ProductAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.Product


class ProductFragment : Fragment, ProductAdapter.ProductEventListener {

    lateinit var btnAdd: Button
    lateinit var tvTitle: TextView
    lateinit var tvTitle2: TextView
    lateinit var rcv: RecyclerView
    private lateinit var actv: AutoCompleteTextView
    var fab: FloatingActionButton? = null
    private var productArrayList: ArrayList<Product> = ArrayList()
    var alphaDAO: AlphaDAO? = null
    var productAdapter: ProductAdapter? = null

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
        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        tvTitle2.visibility = View.GONE
        if (layoutType != 0) {
            llHead.visibility = View.GONE
        } else {
            tvTitle.text = "Danh sách sản phẩm"
            (activity as MainActivity).changeToolbarTitle("QL Sản Phẩm")
        }
        //
        btnAdd = view.findViewById(R.id.btnAdd)
        rcv = view.findViewById(R.id.rcv)
        actv = view.findViewById(R.id.actv)
        fab = view.findViewById(R.id.fab)
        alphaDAO = AlphaDAO(context!!)
        productArrayList = if ((activity as MainActivity).currentUserType == 1) {
            alphaDAO!!.getAllProduct()!!
        } else {
            alphaDAO!!.getAllProductUse()!!
        }
        rcv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        /* */
        productAdapter = ProductAdapter(context!!, productArrayList, this)
        rcv.adapter = productAdapter

        if ((alphaDAO!!.getUserByID((activity as MainActivity?)!!.currentUserID)).type != 1) {
            btnAdd.visibility = View.GONE
        } else {
            btnAdd.setOnClickListener {
                (activity as MainActivity).switchFragment(
                    ProductAddEditFragment(0, "Add"),
                    "Replace", "ProductAddEdit"
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
                            productAdapter!!.filter!!.filter(s)
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


    override fun removeProduct(product: Product) {
        productArrayList.remove(product)
        productAdapter = ProductAdapter(context!!, productArrayList, this)
        rcv.adapter = productAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}