package com.ps12027_cuongnt.alphagym.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ps12027_cuongnt.alphagym.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val edtUsername: EditText = view.findViewById(R.id.edtUsername)
        val edtPassword: EditText = view.findViewById(R.id.edtPassword)
        val btnLogin: Button = view.findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            if (edtUsername.text.toString().equals("admin", true) && (edtPassword.text.toString()
                    .equals("admin", false))
            ) {
                val mainFragment = MainFragment()
                val frm = activity?.supportFragmentManager
                if (frm != null) {
                    frm.beginTransaction()
                        .replace(R.id.frameLayout, mainFragment)
                        .commit()
                }
            } else {
                Toast.makeText(context, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

}