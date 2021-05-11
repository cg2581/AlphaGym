package com.ps12027_cuongnt.alphasupplements.fragment.manage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.LoginFragment

class ChangePasswordFragment : Fragment() {
    private var edtOldPassword: EditText? = null
    private var edtNewPassword: EditText? = null
    private var edtRepeatNewPassword: EditText? = null
    private var btnChangePassword: Button? = null
    private lateinit var alphaDAO: AlphaDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        (activity as MainActivity?)!!.changeToolbarTitle("Đổi mật khẩu")
        edtOldPassword = view.findViewById(R.id.edtOldPassword)
        edtNewPassword = view.findViewById(R.id.edtNewPassword)
        edtRepeatNewPassword = view.findViewById(R.id.edtRepeatNewPassword)
        btnChangePassword = view.findViewById(R.id.btnChangePassword)
        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()
        val prefAccount =
            context!!.getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        val editorAccount = prefAccount!!.edit()
        (activity as MainActivity?)!!.supportActionBar!!.title = "Đổi mật khẩu"
        (activity as MainActivity?)!!.showUI()
        btnChangePassword!!.setOnClickListener {
            if (edtRepeatNewPassword!!.text.toString() != edtNewPassword!!.text.toString()) {
                Toast.makeText(context, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show()
            } else {
                if (edtOldPassword!!.text.toString().trim()
                        .isEmpty() || edtNewPassword!!.text.toString().trim().isEmpty()
                ) {

                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT)
                        .show()
                } else if (alphaDAO.changePassword(
                        (activity as MainActivity?)!!.currentUserID!!,
                        edtOldPassword!!.text.toString(),
                        edtNewPassword!!.text.toString()
                    )
                ) {
                    Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                    editorAccount.putBoolean("logged", false)
                    editorAccount.putBoolean("check", false)
                    editorAccount.apply()
                    (activity as MainActivity?)!!.clearFragmentBackStack()
                    (activity as MainActivity?)!!.switchFragment(LoginFragment(), "ReplaceN","Login")
                } else {
                    Toast.makeText(context, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}