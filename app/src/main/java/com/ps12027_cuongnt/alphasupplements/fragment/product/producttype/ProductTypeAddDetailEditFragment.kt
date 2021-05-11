package com.ps12027_cuongnt.alphasupplements.fragment.product.producttype

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.Product
import com.ps12027_cuongnt.alphasupplements.model.ProductType

class ProductTypeAddDetailEditFragment(var productTypeID: Int, var type: String?) :
    Fragment() {
    lateinit var alphaDAO: AlphaDAO


    /*  */
    lateinit var tvTitle: TextView
    lateinit var edtProductTypeName: EditText
    lateinit var edtProductCount: EditText
    lateinit var btnAdd: Button
    lateinit var llProductCount: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_product_type_add_detail_edit, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        edtProductCount = view.findViewById(R.id.edtProductCount)
        edtProductTypeName = view.findViewById(R.id.edtProductTypeName)
        btnAdd = view.findViewById(R.id.btnAdd)
        llProductCount = view.findViewById(R.id.llProductCount)


        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()

        when (type) {
            "Add" -> {
                tvTitle.text = "Thêm loại sản phẩm"
                llProductCount!!.visibility = View.GONE
                btnAdd!!.text = "Thêm loại sản phẩm"
                btnAdd!!.setOnClickListener {
                    val name = edtProductTypeName!!.text.toString()
                    if (name.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        try {
                            val newProductType =
                                ProductType(0, name)
                            if (alphaDAO!!.addProductType(newProductType)) {

                                Toast.makeText(activity, "Thêm thành công!", Toast.LENGTH_SHORT)
                                    .show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(activity, "Thêm thất bại!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
            }
            "Edit" -> {
                tvTitle.text = "Cập nhật loại sản phẩm"
                llProductCount!!.visibility = View.GONE
                val currentItem = alphaDAO!!.getProductTypeByID(productTypeID)
                btnAdd!!.text = "Cập nhật loại sản phẩm"
                edtProductCount!!.isFocusableInTouchMode = false
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                // Fill data
                edtProductTypeName!!.setText(currentItem.name)
                /* */
                btnAdd!!.setOnClickListener {

                    val name = edtProductTypeName!!.text.toString()
                    if (name.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            val newProductType =
                                ProductType(
                                    productTypeID,
                                    name
                                )
                            if (alphaDAO!!.updateProductType(newProductType)) {

                                /* */
                                Toast.makeText(
                                    activity,
                                    "Cập nhật thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Cập nhật thất bại!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                }
            }
            "View" -> {
                edtProductTypeName.isFocusableInTouchMode = false
                edtProductTypeName.isEnabled = false
                tvTitle.text = "Thông tin loại sản phẩm"
                llProductCount!!.visibility = View.VISIBLE
                val currentItem = alphaDAO!!.getProductTypeByID(productTypeID)
                edtProductCount!!.setText(
                    alphaDAO.countProductByProductID(productTypeID).toString()
                )
                edtProductTypeName!!.setText(currentItem.name)
                btnAdd!!.visibility = View.GONE
            }
        }

    }

}