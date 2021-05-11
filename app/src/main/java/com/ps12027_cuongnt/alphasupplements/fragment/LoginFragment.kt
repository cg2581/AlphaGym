package com.ps12027_cuongnt.alphasupplements.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO

class LoginFragment : Fragment() {
    var TAG = "LOGIN LOG"
    var edtPhoneNumber: EditText? = null
    var edtPassword: EditText? = null
    var btnLogin: Button? = null
    var alphaDAO: AlphaDAO? = null
    var chkRemember: CheckBox? = null

    var prefAccount: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideUI()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        chkRemember = view.findViewById(R.id.chkRemember)
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber)
        edtPassword = view.findViewById(R.id.edtPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        alphaDAO = AlphaDAO(context!!)







        return view
    }

    override fun onResume() {
        super.onResume()
        prefAccount =
            context!!.getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        var editorAccount = prefAccount!!.edit()
        var check: Boolean = prefAccount!!.getBoolean("check", false)
        var logged: Boolean = prefAccount!!.getBoolean("logged", false)
        var username = prefAccount!!.getString("username", "")
        var password = prefAccount!!.getString("password", "")
        if (check) {
            edtPhoneNumber!!.setText(username)
            edtPassword!!.setText(password)
            chkRemember!!.isChecked = prefAccount!!.getBoolean("check", false)
        }
        if (logged) {
            val user = alphaDAO!!.login(username!!, password!!)
            if (user != null) {
                (activity as MainActivity?)!!.switchFragment(MainFragment(), "ReplaceN", "Main")
                (activity as MainActivity?)!!.currentUserID = user.userID
                (activity as MainActivity?)!!.currentUserType = user.type
                editorAccount.putBoolean("logged", true);
                editorAccount.apply()
            }
        }


        (activity as MainActivity?)!!.drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        btnLogin!!.setOnClickListener {
            val user =
                alphaDAO!!.login(edtPhoneNumber!!.text.toString(), edtPassword!!.text.toString())

            if (user != null) {
                if (chkRemember!!.isChecked) {
                    editorAccount.putString("username", edtPhoneNumber!!.text.toString());
                    editorAccount.putString("password", edtPassword!!.text.toString());
                    editorAccount.putBoolean("check", true);
                } else {
                    editorAccount.clear()
                }
                editorAccount.putBoolean("logged", true);
                editorAccount.apply()
                (activity as MainActivity?)!!.switchFragment(MainFragment(), "ReplaceN", "Main")
                (activity as MainActivity?)!!.currentUserID = user.userID
                (activity as MainActivity?)!!.currentUserType = user.type
            } else {
                Toast.makeText(context, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show()
            }


        }

    }


}