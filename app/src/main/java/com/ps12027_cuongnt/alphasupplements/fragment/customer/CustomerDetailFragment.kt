package com.ps12027_cuongnt.alphasupplements.fragment.customer

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.User
import de.hdodenhof.circleimageview.CircleImageView


class CustomerDetailFragment(var userID: Int) : Fragment() {

    //
    private lateinit var tvFullName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvType: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var ivAvatar: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvTitle2: TextView

    lateinit var alphaDAO: AlphaDAO
    lateinit var currentUser: User
    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_customer_detail, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        tvFullName = view.findViewById(R.id.tvFullName)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvType = view.findViewById(R.id.tvType)
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        ivAvatar = view.findViewById(R.id.ivAvatar)

        alphaDAO = AlphaDAO(context!!)

        (activity as MainActivity).showUI()

        return view
    }

    override fun onResume() {
        super.onResume()

        currentUser = alphaDAO.getUserByID(userID)


        tvTitle.text = "Thông tin khách hàng"
        tvTitle2.text = "#".plus(currentUser.userID.toString())
        when (currentUser.type) {
            1 -> tvType.text = "Quản lí"
            2 -> tvType.text = "Nhân viên"
            3 -> tvType.text = "Khách hàng"
        }
        tvFullName.text = currentUser.fullname
        tvAddress.text = currentUser.address
        tvPhoneNumber.text = currentUser.phoneNumber
/*
        val wordtoSpan: Spannable =
            SpannableString("Địa chỉ: ".plus(currentUser.address))
        wordtoSpan.setSpan(
            TypefaceSpan(Typeface.DEFAULT_BOLD),
            0,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )*/

        tvAddress.text = currentUser.address

        if (currentUser.image != null) {
            val bitmap =
                BitmapFactory.decodeByteArray(
                    currentUser.image,
                    0,
                    currentUser.image!!.size
                )
            ivAvatar.setImageBitmap(bitmap)
        } else {
            ivAvatar.setImageDrawable(context!!.getDrawable(R.drawable.image_missing))
        }

    }

}