package com.ps12027_cuongnt.alphasupplements.fragment.product.product

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.cart.CartFragment
import com.ps12027_cuongnt.alphasupplements.model.InvoiceDetail
import com.ps12027_cuongnt.alphasupplements.model.Product
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat


class ProductDetailFragment(var productID: Int) : Fragment() {
    var alphaDAO: AlphaDAO? = null
    lateinit var currentProduct: Product
    /* */

    private lateinit var tvID: TextView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnAdd: Button
    private lateinit var ivLogo: ImageView
    /* */

    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)

        tvID = view.findViewById(R.id.tvID)
        tvName = view.findViewById(R.id.tvName)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvPrice = view.findViewById(R.id.tvPrice)
        ivLogo = view.findViewById(R.id.ivLogo)
        btnAdd = view.findViewById(R.id.btnAdd)
        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()
        currentProduct = alphaDAO!!.getProductByID(productID)
        val id = currentProduct.productID
        val name = currentProduct.name
        val price = currentProduct.price
        val description = currentProduct.description
        val image = currentProduct.image

        /* */
        tvID.text = "#".plus(id.toString())
        tvName.text = name
        tvDescription.text = description
        tvPrice.text = df.format(price)

        if (image == null) {
            ivLogo!!.setImageDrawable(context!!.getDrawable(R.drawable.image_missing))
        } else {
            ivLogo!!.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    currentProduct.image,
                    0,
                    currentProduct.image!!.size
                )
            )
        }

        if (currentProduct.use != 1) {
            btnAdd.text = "Ngưng kinh doanh"
            btnAdd.setBackgroundColor(resources.getColor(R.color.Gray))
            btnAdd.isEnabled = false
        }




        btnAdd!!.setOnClickListener {
            if ((activity as MainActivity?)!!.currentInvoiceID == null) {
                Toast.makeText(context, "Chưa tạo đơn hàng!", Toast.LENGTH_SHORT).show()
            } else {
                val invoiceDetail = InvoiceDetail(
                    0,
                    (activity as MainActivity?)!!.currentInvoiceID!!,
                    currentProduct.productID,
                    1
                )
                if (alphaDAO!!.addInvoiceDetail(invoiceDetail)) {
                    val dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
                    dialog.setContentView(R.layout.dialog_product_added)

                    val ivProductLogo = dialog.findViewById<ImageView>(R.id.ivProductLogo)
                    val tvProductName = dialog.findViewById<TextView>(R.id.tvName)
                    val tvProductPrice = dialog.findViewById<TextView>(R.id.tvProductPrice)
                    val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
                    val btnViewCart = dialog.findViewById<Button>(R.id.btnViewCart)

                    tvProductName!!.text = name
                    tvProductPrice!!.text = df.format(price)
                    if (image == null) {
                        ivProductLogo!!.setImageDrawable(context!!.getDrawable(R.drawable.image_missing))
                    } else {
                        ivProductLogo!!.setImageBitmap(
                            BitmapFactory.decodeByteArray(
                                currentProduct.image,
                                0,
                                currentProduct.image!!.size
                            )
                        )
                    }
                    ivClose!!.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnViewCart!!.setOnClickListener {
                        (activity as MainActivity).switchFragment(CartFragment(), "Replace", "Cart")
                        dialog.dismiss()
                    }

                    dialog.show()
                } else {
                    Toast.makeText(
                        context,
                        "Sản phẩm đã tồn tại trong giỏ hàng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }


}