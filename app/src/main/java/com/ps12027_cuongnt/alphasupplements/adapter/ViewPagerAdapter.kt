package com.ps12027_cuongnt.alphasupplements.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ps12027_cuongnt.alphasupplements.fragment.cart.CartFragment
import com.ps12027_cuongnt.alphasupplements.fragment.invoice.InvoiceFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.producttype.ProductTypeFragment


class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, type: String) :
    FragmentStateAdapter(fm, lifecycle) {
    private var type: String? = type
    private var registeredFragments =
        SparseArray<Fragment>()


    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments[position]
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (type == "Product") {
            when (position) {
                0 -> {
                    return ProductFragment(1)
                }
                1 -> {
                    return ProductTypeFragment(1)
                }
            }
        } else if (type == "Invoice") {
            when (position) {
                0 -> {
                    return CartFragment()
                }
                1 -> {
                    return InvoiceFragment()
                }
            }
        }
        return Fragment()
    }



}
