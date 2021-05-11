package com.ps12027_cuongnt.alphasupplements.fragment.manage

import android.os.Bundle
import android.util.Log
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
import com.ps12027_cuongnt.alphasupplements.adapter.ViewPagerAdapter
import com.ps12027_cuongnt.alphasupplements.fragment.invoice.InvoiceFragment


class CartManageFragment : Fragment() {
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
        (activity as MainActivity?)!!.changeToolbarTitle("Hoá đơn")
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, "Invoice")
        viewPager.adapter = viewPagerAdapter
        tabLayout.getTabAt(viewPager.currentItem)!!.select()

        var names: Array<String> = arrayOf("Giỏ hàng", "Hoá đơn")

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
                if (position == 2) {

                    val myFragment = InvoiceFragment()
                    myFragment.update()
                }
            }
        })
/*
        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            // override desired callback functions
        })

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
                check()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })*/


    }

    private fun check(position: Int) {

        val myFragment = childFragmentManager.findFragmentByTag("f$position")!!


        when (position) {
            0 -> {
                //(activity as MainActivity?)!!.changeToolbarTitle("Giỏ hàng")
            }
            1 -> {
                //(activity as MainActivity?)!!.changeToolbarTitle("Hoá đơn")
                (myFragment as InvoiceFragment).update()
            }
        }
    }

}