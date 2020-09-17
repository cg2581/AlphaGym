package com.ps12027_cuongnt.alphagym.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dshantanu.androidsquareslib.AndroidSquares
import com.ps12027_cuongnt.alphagym.R

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val asAccountSetting: AndroidSquares = view.findViewById(R.id.asAccountSetting)
        asAccountSetting.setOnClickListener {
            Toast.makeText(activity, "Chức năng số 1", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}