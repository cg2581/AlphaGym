package com.ps12027_cuongnt.alphasupplements.fragment.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.ViewPager2PageChangeCallback
import com.ps12027_cuongnt.alphasupplements.adapter.ViewPagerAdapter
import com.ps12027_cuongnt.alphasupplements.fragment.invoice.InvoiceFragment


class ProductManageFragment : Fragment() {
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View =
            inflater.inflate(R.layout.fragment_product_manage, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, "Product")
        viewPager.adapter = viewPagerAdapter
        tabLayout.getTabAt(viewPager.currentItem)!!.select()

        var names: Array<String> = arrayOf("Sản phẩm", "Loại sản phẩm")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                check(position)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)!!.changeToolbarTitle("QL Sản phẩm")
    }


    override fun onResume() {
        super.onResume()

    }

    private fun check(position: Int) {
        val myFragment = childFragmentManager.findFragmentByTag("f$position")
        when (position) {
            0 -> {
                //(activity as MainActivity).changeToolbarTitle("Sản phẩm")
            }
            1 -> {
                //(activity as MainActivity).changeToolbarTitle("Loại sản phẩm")
            }
        }
    }


}