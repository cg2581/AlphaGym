package com.ps12027_cuongnt.alphasupplements.fragment.cart

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.UserSelectAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.listener.CustomerChoose
import com.ps12027_cuongnt.alphasupplements.listener.CustomerSelect
import com.ps12027_cuongnt.alphasupplements.model.User

class CustomerSelectDialog(
    val listener: CustomerChoose
) : DialogFragment(), CustomerSelect {

    lateinit var rcv: RecyclerView
    private lateinit var alphaDAO: AlphaDAO
    private var customerList: ArrayList<User> = ArrayList()
    private lateinit var userSelectAdapter: UserSelectAdapter
    private lateinit var actv: AutoCompleteTextView
    private lateinit var btnCancel: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_customer_select)

        btnCancel = dialog.findViewById(R.id.btnCancel)
        rcv = dialog.findViewById(R.id.rcv)
        alphaDAO = AlphaDAO(context!!)
        actv = dialog.findViewById(R.id.actv)
        customerList.clear()
        customerList.addAll(alphaDAO.getAllCustomerSelect())
        rcv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcv.setHasFixedSize(true);

        /* */
        userSelectAdapter =
            UserSelectAdapter(context!!, customerList, this)
        rcv.adapter = userSelectAdapter
        btnCancel.setOnClickListener { dismiss() }


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
                            userSelectAdapter.filter!!.filter(s)
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

    override fun select(position: Int) {
        listener.chooseType(position)
        dismiss()
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }


}