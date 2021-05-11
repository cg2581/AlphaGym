package com.ps12027_cuongnt.alphasupplements.fragment.product.product

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.ProductTypeACTVAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.producttype.ProductTypeSelectDialog
import com.ps12027_cuongnt.alphasupplements.listener.ProductTypeChoose
import com.ps12027_cuongnt.alphasupplements.model.Product
import com.ps12027_cuongnt.alphasupplements.model.ProductType
import kotlinx.android.synthetic.main.fragment_product_type_add_detail_edit.*
import java.io.ByteArrayOutputStream

class ProductAddEditFragment :
    Fragment, ProductTypeChoose {
    var productID: Int = 0
    var type: String = "View"

    constructor() : super()

    constructor(productID: Int, type: String) : super() {
        this.productID = productID
        this.type = type
    }

    /*  */
    lateinit var btnAdd: Button
    lateinit var edtPrice: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var actvType: EditText
    lateinit var ivProductLogo: ImageView
    lateinit var ivDoneName: ImageView
    lateinit var ivDoneDescription: ImageView
    lateinit var tvTitle: TextView

    lateinit var alphaDAO: AlphaDAO
    var productTypelist = ArrayList<ProductType>()
    private var currentProductTypeID: Int = 0

    private val perm = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )
    val request_code = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_product_add_edit, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        edtPrice = view.findViewById(R.id.edtPrice)
        edtName = view.findViewById(R.id.edtName)
        edtDescription = view.findViewById(R.id.edtDescription)
        btnAdd = view.findViewById(R.id.btnAdd)
        actvType = view.findViewById(R.id.actvType)
        ivProductLogo = view.findViewById(R.id.ivProductLogo)
        ivDoneName = view.findViewById(R.id.ivDoneName)
        ivDoneDescription = view.findViewById(R.id.ivDoneDescription)
        ivProductLogo.setOnClickListener {
            (activity as MainActivity).openActionPicker(perm, request_code)
        }

        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()

        edtDescription.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    ivDoneDescription.visibility = View.VISIBLE
                    ivDoneDescription.setOnClickListener {
                        edtPrice.requestFocus()
                        edtPrice.setSelection(edtPrice.length())
                    }
                }
                false -> {
                    ivDoneDescription.visibility = View.GONE
                }
            }
        }
        edtName.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    ivDoneName.visibility = View.VISIBLE
                    ivDoneName.setOnClickListener {
                        edtDescription.requestFocus()
                        edtDescription.setSelection(edtDescription.length())
                    }
                }
                false -> {
                    ivDoneName.visibility = View.GONE
                }
            }
        }

        actvType.setOnClickListener {
            val dialog = ProductTypeSelectDialog(this)
            dialog.show(childFragmentManager, "ProductTypeSelectDialog")
        }

        when (type) {
            "Add" -> {
                tvTitle.text = "Thêm mới sản phẩm"
                if ((activity as MainActivity?)!!.imgByteArray != null) {
                    setLogo()
                }
                btnAdd.setOnClickListener {
                    Log.d("asd", "onResume: ${(activity as MainActivity).imgByteArray}")
                    val name = edtName.text.toString()
                    val description = edtDescription.text.toString()
                    val price = edtPrice.text.toString()
                    val type = currentProductTypeID
                    val image = (activity as MainActivity).imageViewToByte(ivProductLogo)
                    if (name.isEmpty() || description.isEmpty() || price.isEmpty() || type == null) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            val newProduct =
                                Product(
                                    0,
                                    type,
                                    name,
                                    description,
                                    price.toInt(),
                                    image,
                                    1
                                )
                            if (alphaDAO.addProduct(newProduct)) {
                                Toast.makeText(
                                    activity,
                                    "Thêm sản phẩm thành công!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Thêm sản phẩm thất bại!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
            }
            "Edit" -> {
                tvTitle.text = "Cập nhật sản phẩm"
                val currentProduct = alphaDAO.getProductByID(productID)
                currentProductTypeID = currentProduct.type
                // Fill data
                edtName.setText(currentProduct.name)
                edtPrice.setText(currentProduct.price.toString())
                edtDescription.setText(currentProduct.description)
                actvType.setText(alphaDAO.getProductTypeName(currentProductTypeID))
                if ((activity as MainActivity).imgByteArray == null) {
                    if (currentProduct.image != null) {
                        val bitmap =
                            BitmapFactory.decodeByteArray(
                                currentProduct.image,
                                0,
                                currentProduct.image!!.size
                            )
                        ivProductLogo.setImageBitmap(bitmap)
                    } else {
                        ivProductLogo.setImageDrawable(resources.getDrawable(R.drawable.image_missing))
                    }
                } else {
                    setLogo()
                }
                /* */
                btnAdd.setOnClickListener {

                    val name = edtName.text.toString()
                    val description = edtDescription.text.toString()
                    val price = edtPrice.text.toString()
                    val image = (activity as MainActivity).imageViewToByte(ivProductLogo)
                    if (name.isEmpty() || description.isEmpty() || price.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            val newProduct =
                                Product(
                                    productID,
                                    currentProductTypeID,
                                    name,
                                    description,
                                    price.toInt(),
                                    image,
                                    currentProduct.use
                                )
                            if (alphaDAO.updateProduct(newProduct)) {

                                /* */
                                Toast.makeText(
                                    activity,
                                    "Cập nhật sản phẩm thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Cập nhật sản phẩm thất bại!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                }
            }
        }

    }

    private fun setLogo() {
        val byteArray = (activity as MainActivity).imgByteArray
        if (byteArray != null) {
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ivProductLogo.setImageBitmap(bmp)
            (activity as MainActivity).imgByteArray = null
        } else {
            ivProductLogo.setImageDrawable(resources.getDrawable(R.drawable.image_missing))
        }
    }

    override fun chooseType(id: Int) {
        currentProductTypeID = id
        actvType.setText(alphaDAO.getProductTypeName(id))
        edtName.requestFocus()
        edtName.setSelection(edtName.length())
    }
}