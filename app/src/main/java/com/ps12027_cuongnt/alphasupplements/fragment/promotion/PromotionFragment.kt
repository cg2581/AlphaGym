package com.ps12027_cuongnt.alphasupplements.fragment.promotion

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.InvoiceAdapter
import com.ps12027_cuongnt.alphasupplements.adapter.PromotionAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.producttype.ProductTypeAddDetailEditFragment
import com.ps12027_cuongnt.alphasupplements.listener.RecyclerTouchListener
import com.ps12027_cuongnt.alphasupplements.model.Invoice
import com.ps12027_cuongnt.alphasupplements.model.Promotion

class PromotionFragment : Fragment() {
    private var rcv: RecyclerView? = null
    private var alphaDAO: AlphaDAO? = null
    private var promotionList: ArrayList<Promotion> = ArrayList()
    private lateinit var actv: AutoCompleteTextView
    private lateinit var btnAdd: Button
    private lateinit var tvTitle: TextView
    private lateinit var tvTitle2: TextView
    var promotionAdapter: PromotionAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rcv, container, false)

        (activity as MainActivity?)!!.changeToolbarTitle("Khuyến mãi")
        actv = view.findViewById(R.id.actv)
        rcv = view.findViewById(R.id.rcv)
        btnAdd = view.findViewById(R.id.btnAdd)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        alphaDAO = AlphaDAO(context!!)

        tvTitle.text = "Danh sách khuyến mãi"
        tvTitle2.visibility = View.GONE
        rcv!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        promotionList = alphaDAO!!.getAllPromotion()!!
        promotionAdapter = PromotionAdapter(context!!, promotionList)
        rcv!!.adapter = promotionAdapter
        actv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                promotionAdapter!!.filter!!.filter(s)
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
        if ((alphaDAO!!.getUserByID((activity as MainActivity?)!!.currentUserID)).type != 1) {
            btnAdd.visibility = View.GONE
        } else {
            btnAdd.setOnClickListener {
                (activity as MainActivity?)!!.switchFragment(
                    PromotionAddDetailEditFragment(0, "Add"),
                    "Replace", "PromotionAddDetailEdit"
                )

            }
        }
        return view
    }

}