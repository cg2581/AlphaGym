package com.ps12027_cuongnt.alphasupplements.fragment.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.LoginFragment
import com.ps12027_cuongnt.alphasupplements.fragment.customer.CustomerAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.customer.CustomerDetailFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeDetailFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.ChangePasswordFragment

class UserSettingsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    lateinit var tvTitle: TextView
    lateinit var tvInfo: TextView
    lateinit var tvUpdateInfo: TextView
    lateinit var tvChangePassword: TextView
    lateinit var tvLogout: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)
        (activity as MainActivity).changeToolbarTitle("QL tài khoản")
        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle.text = AlphaDAO(context!!).getUserName((activity as MainActivity).currentUserID)
        tvInfo = view.findViewById(R.id.tvInfo)
        tvUpdateInfo = view.findViewById(R.id.tvUpdateInfo)
        tvChangePassword = view.findViewById(R.id.tvChangePassword)
        tvLogout = view.findViewById(R.id.tvLogout)

        tvInfo.setOnClickListener {
            (activity as MainActivity).switchFragment(
                EmployeeDetailFragment((activity as MainActivity).currentUserID),
                "Replace",
                "EmployeeDetail"
            )
        }
        tvUpdateInfo.setOnClickListener {
            (activity as MainActivity).switchFragment(
                EmployeeAddEditFragment((activity as MainActivity).currentUserID, "Edit"),
                "Replace",
                "EmployeeDetail"
            )
        }
        tvChangePassword.setOnClickListener {
            (activity as MainActivity).switchFragment(
                ChangePasswordFragment(),
                "Replace",
                "ChangePassword"
            )
        }
        tvLogout.setOnClickListener {
            val prefAccount =
                (activity as MainActivity).getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
            val editorAccount = prefAccount!!.edit()
            editorAccount.putBoolean("logged", false)
            editorAccount.apply()
            (activity as MainActivity).clearFragmentBackStack()
            (activity as MainActivity).switchFragment(LoginFragment(), "ReplaceN", "Login")
        }





        return view
    }

}