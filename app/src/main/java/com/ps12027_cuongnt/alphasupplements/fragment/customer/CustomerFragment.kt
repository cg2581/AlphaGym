package com.ps12027_cuongnt.alphasupplements.fragment.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.CustomerAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.User

class CustomerFragment : Fragment() {
    private var rcv: RecyclerView? = null
    private var alphaDAO: AlphaDAO? = null
    lateinit var btnAdd: Button
    private var userList: ArrayList<User> = ArrayList()
    private var customerAdapter: CustomerAdapter? = null
    private lateinit var actv: AutoCompleteTextView
    lateinit var tvTitle: TextView
    lateinit var tvTitle2: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rcv, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        tvTitle.text = "Danh sách khách hàng"
        tvTitle2.visibility = View.GONE
        actv = view.findViewById(R.id.actv)
        btnAdd = view.findViewById(R.id.btnAdd)
        rcv = view.findViewById(R.id.rcv)
        alphaDAO = AlphaDAO(context!!)
        rcv!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        (activity as MainActivity?)!!.changeToolbarTitle("QL Khách hàng")
        if ((alphaDAO!!.getUserByID((activity as MainActivity?)!!.currentUserID!!)).type != 1) {
            btnAdd!!.visibility = View.GONE
        } else {
            btnAdd!!.setOnClickListener {
                (activity as MainActivity?)!!.switchFragment(
                    CustomerAddEditFragment(0, "Add"),
                    "Replace", "CustomerAddEdi"
                )

            }
        }
        actv.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                actv.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        customerAdapter!!.filter!!.filter(s)
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

        return view
    }

    override fun onResume() {
        super.onResume()
        userList.clear()
        userList.addAll(alphaDAO!!.getAllCustomer())
        customerAdapter = CustomerAdapter(context!!, userList)
        rcv!!.adapter = customerAdapter

    }


}