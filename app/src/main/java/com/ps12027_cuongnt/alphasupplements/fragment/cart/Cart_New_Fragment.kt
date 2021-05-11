package com.ps12027_cuongnt.alphasupplements.fragment.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.UserACTVAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.CountryItem
import com.ps12027_cuongnt.alphasupplements.model.Invoice
import com.ps12027_cuongnt.alphasupplements.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Cart_New_Fragment : Fragment(), AdapterView.OnItemClickListener {
    var actv: AutoCompleteTextView? = null
    var btnNew: Button? = null
    var customerID: Int? = null

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = LocalDateTime.now().format(formatter)
    private lateinit var alphaDAO: AlphaDAO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart_new, container, false)
        actv = view.findViewById(R.id.actv)
        btnNew = view.findViewById(R.id.button)
        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.changeToolbarTitle("Thêm hoá đơn mới")
        val alphaDAO = AlphaDAO(context!!)
        val userList = alphaDAO.getAllUserByType(3)
        //
        val userACTVAdapter = UserACTVAdapter(context!!, userList!!)
        actv!!.setAdapter(userACTVAdapter)
        actv!!.onItemClickListener = this

        btnNew!!.setOnClickListener {
            if (customerID != null) {
                val invoice = Invoice(
                    0,
                    LocalDateTime.now().format(formatter),
                    "",
                    (activity as MainActivity?)!!.currentUserID!!,
                    customerID!!,
                    1
                )
                alphaDAO.addInvoice(invoice)
                (activity as MainActivity?)!!.back()
            } else {
                Toast.makeText(context, "Lỗi!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val user = actv!!.adapter.getItem(position) as User
        Toast.makeText(context, user.userID.toString(), Toast.LENGTH_SHORT).show()
        customerID = user.userID
        (activity as MainActivity?)!!.hideKeyboard()
    }


}