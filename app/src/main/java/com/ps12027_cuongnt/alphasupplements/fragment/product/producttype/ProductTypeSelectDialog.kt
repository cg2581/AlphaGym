package com.ps12027_cuongnt.alphasupplements.fragment.product.producttype

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.ProductTypeSelectAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.listener.ProductTypeChoose
import com.ps12027_cuongnt.alphasupplements.listener.ProductTypeSelect
import com.ps12027_cuongnt.alphasupplements.model.ProductType


class ProductTypeSelectDialog(
    val listener: ProductTypeChoose
) : DialogFragment(), ProductTypeSelect {
    private lateinit var rcv: RecyclerView
    private var productTypeArrayList: ArrayList<ProductType> = ArrayList()
    private lateinit var actv: AutoCompleteTextView
    lateinit var btnCancel: Button

    var alphaDAO: AlphaDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_product_type_select)

        rcv = dialog.findViewById(R.id.rcv)
        alphaDAO = AlphaDAO(context!!)
        actv = dialog.findViewById(R.id.actv)
        btnCancel = dialog.findViewById(R.id.btnCancel)
        productTypeArrayList.clear()
        productTypeArrayList.addAll(alphaDAO!!.getAllProductType())
        Log.d("aa", "onCreateDialog: ${productTypeArrayList.size}")
        rcv!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcv!!.setHasFixedSize(true);

        /* */
        val productTypeSelectAdapter =
            ProductTypeSelectAdapter(context!!, productTypeArrayList, this)
        rcv!!.adapter = productTypeSelectAdapter


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
                            productTypeSelectAdapter.filter!!.filter(s)
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

        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun select(id: Int) {
        listener.chooseType(id)
        dismiss()
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

}