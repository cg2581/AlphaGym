package com.ps12027_cuongnt.alphasupplements.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.MenuAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.cart.CartFragment
import com.ps12027_cuongnt.alphasupplements.fragment.customer.CustomerFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.CartManageFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.ProductManageFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.StatisticFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductFragment
import com.ps12027_cuongnt.alphasupplements.fragment.promotion.PromotionFragment
import com.ps12027_cuongnt.alphasupplements.model.Menu
import com.ps12027_cuongnt.alphasupplements.model.User
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainFragment : Fragment() {
    private lateinit var rcvMenu: RecyclerView
    private lateinit var menuList: ArrayList<Menu>
    private lateinit var civAvatar: CircleImageView
    private lateinit var tvName: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvInvoiceCount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitle2: TextView
    private lateinit var alphaDAO: AlphaDAO
    private lateinit var currentUser: User
    private lateinit var layoutManager: GridLayoutManager
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private var df: DecimalFormat = DecimalFormat("###,###")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DAO
        alphaDAO = AlphaDAO(context!!)
        currentUser = alphaDAO.getUserByID((activity as MainActivity?)!!.currentUserID!!)
        rcvMenu = view.findViewById(R.id.rcvMenu)
        civAvatar = view.findViewById(R.id.civAvatar)
        tvName = view.findViewById(R.id.tvName)
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        tvTotal = view.findViewById(R.id.tvTotal)
        tvInvoiceCount = view.findViewById(R.id.tvInvoiceCount)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvRole = view.findViewById(R.id.tvRole)
        // UI
        tvTitle2.visibility = View.GONE
        (activity as MainActivity).showUI()
        (activity as MainActivity).currentMenu = R.menu.menu_main
        (activity as MainActivity).invalidateOptionsMenu()
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).changeToolbarTitle("Trang chủ")
        // Current Invoice
        val currentInvoice =
            alphaDAO.getInvoiceBySellerIDAndStatus((activity as MainActivity).currentUserID!!, 0)
        if (currentInvoice != null) {
            (activity as MainActivity).currentInvoiceID = currentInvoice.invoiceID
        }

        // Menu
        menuList = ArrayList<Menu>()
        if (currentUser.type == 1) {
            menuList.add(
                Menu(
                    R.drawable.account,
                    "Sản phẩm",
                    ProductManageFragment(),
                    "ProductManage"
                )
            )
            menuList.add(Menu(R.drawable.product, "Khách hàng", CustomerFragment(), "Customer"))
            menuList.add(
                Menu(
                    R.drawable.invoice, "Hoá đơn",
                    CartManageFragment(), "CartManage"
                )
            )
            menuList.add(Menu(R.drawable.promotion, "Khuyến mãi", PromotionFragment(), "Promotion"))
            menuList.add(Menu(R.drawable.statistic, "Thống kê", StatisticFragment(), "Statistic"))
            menuList.add(Menu(R.drawable.employee, "Nhân viên", EmployeeFragment(), "Employee"))
            layoutManager = GridLayoutManager(context, 2)

        } else if (currentUser.type == 2) {
            menuList.add(Menu(R.drawable.product, "Sản phẩm", ProductFragment(), "Product"))
            menuList.add(Menu(R.drawable.customer, "Khách hàng", CustomerFragment(), "Employee"))
            menuList.add(
                Menu(
                    R.drawable.cart, "Giỏ hàng",
                    CartFragment(), "CartManage"
                )
            )
            layoutManager = GridLayoutManager(context, 1)
        }
        // Admin
        rcvMenu.layoutManager = layoutManager
        val menuAdapter = MenuAdapter(context!!, menuList)
        rcvMenu.adapter = menuAdapter

    }

    override fun onResume() {
        super.onResume()
        // User info
        tvTitle.text = "Xin chào, ${currentUser.fullname}"
        tvName.text = currentUser.fullname
        tvPhoneNumber.text = currentUser.phoneNumber
        when (currentUser.type) {
            1 -> tvRole.text = "Quản lí"
            2 -> tvRole.text = "Nhân viên"
            3 -> tvRole.text = "Khách hàng"
        }
        if (currentUser.image != null) {
            val bitmap =
                BitmapFactory.decodeByteArray(
                    currentUser.image,
                    0,
                    currentUser.image!!.size
                )
            civAvatar.setImageBitmap(bitmap)
        } else {
            civAvatar.setImageDrawable(resources.getDrawable(R.drawable.customer))
        }
        tvInvoiceCount.text = alphaDAO.getInvoiceCount().toString()
        tvTotal.text = df.format(alphaDAO.getStatisticTotalToday())


    }


}
