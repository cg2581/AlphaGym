package com.ps12027_cuongnt.alphasupplements.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.R


// TODO: Rename parameter arguments, choose names that match
class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        return view
    }

    fun ReplaceFrameLayout(fragment: Fragment) {
        val fm = activity?.supportFragmentManager
    }

}