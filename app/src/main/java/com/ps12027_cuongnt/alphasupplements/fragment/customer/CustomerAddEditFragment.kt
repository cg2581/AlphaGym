package com.ps12027_cuongnt.alphasupplements.fragment.customer

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.model.User

class CustomerAddEditFragment : Fragment {
    var userID: Int = 0
    var type: String = "View"

    constructor() : super()
    constructor(userID: Int, type: String) : super() {
        this.userID = userID
        this.type = type
    }

    lateinit var tvTitle: TextView
    lateinit var tvTitle2: TextView
    private lateinit var edtName: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnAdd: Button
    private lateinit var ivUserAvatar: ImageView
    private lateinit var alphaDAO: AlphaDAO
    lateinit var currentUser: User

    private val perm = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )
    val request_code = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customer_add_edit, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        edtName = view.findViewById(R.id.edtName)
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber)
        edtAddress = view.findViewById(R.id.edtAddress)
        ivUserAvatar = view.findViewById(R.id.ivUserAvatar)
        btnAdd = view.findViewById(R.id.btnAdd)
        alphaDAO = AlphaDAO(context!!)
        ivUserAvatar.setOnClickListener {
            (activity as MainActivity).openActionPicker(perm, request_code)
        }
        return view
    }

    override fun onResume() {
        super.onResume()


        when (type) {
            "Add" -> {
                tvTitle2.visibility = View.GONE
                tvTitle.text = "Thêm mới khách hàng"
                if ((activity as MainActivity).imgByteArray != null) {
                    setAvatar()
                }
                btnAdd.setOnClickListener {
                    val name = edtName.text.toString()
                    val phoneNumber = edtPhoneNumber.text.toString()
                    val address = edtAddress.text.toString()
                    val image = (activity as MainActivity).imageViewToByte(ivUserAvatar)
                    if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (alphaDAO.checkDupPhoneNumber(phoneNumber)) {
                        Toast.makeText(context, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT)
                            .show()
                    } else
                        if (!"0+[0-9]{9}".toRegex().matches(phoneNumber)) {
                            Toast.makeText(
                                context,
                                "Số điện thoại không hợp lệ!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            try {
                                val newUser =
                                    User(
                                        0,
                                        3,
                                        name,
                                        phoneNumber,
                                        address,
                                        image
                                    )
                                if (alphaDAO.addUser(newUser)) {
                                    Toast.makeText(
                                        activity,
                                        "Thêm khách hàng thành công!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    (activity as MainActivity).back()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Thêm khách hàng thất bại!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                }
            }
            "Edit" -> {
                tvTitle.text = "Cập nhật khách hàng"
                tvTitle2.text = "#".plus(userID)
                currentUser = alphaDAO.getUserByID(userID)
                edtPhoneNumber.setText(currentUser.phoneNumber)
                edtAddress.setText(currentUser.address)
                edtName.setText(currentUser.fullname)
                if ((activity as MainActivity).imgByteArray == null) {
                    if (currentUser.image != null) {
                        val bitmap =
                            BitmapFactory.decodeByteArray(
                                currentUser.image,
                                0,
                                currentUser.image!!.size
                            )
                        ivUserAvatar.setImageBitmap(bitmap)
                    } else {
                        ivUserAvatar.setImageDrawable(resources.getDrawable(R.drawable.image_missing))
                    }
                } else {
                    setAvatar()
                }
                btnAdd.setOnClickListener {
                    val name = edtName.text.toString()
                    val phoneNumber = edtPhoneNumber.text.toString()
                    val address = edtAddress.text.toString()
                    val image = (activity as MainActivity).imageViewToByte(ivUserAvatar)
                    if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (alphaDAO.checkDupPhoneNumberUpdate(
                            phoneNumber,
                            currentUser.phoneNumber
                        )
                    ) {
                        Toast.makeText(context, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!"0+[0-9]{9}".toRegex().matches(phoneNumber)) {
                        Toast.makeText(context, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        try {
                            val newUser =
                                User(
                                    currentUser.userID,
                                    currentUser.type,
                                    name,
                                    phoneNumber,
                                    address,
                                    image
                                )
                            if (alphaDAO.updateUser(newUser)) {

                                Toast.makeText(
                                    activity,
                                    "Cập nhật khách hàng thành công!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                (activity as MainActivity).back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Cập nhật khách hàng thất bại!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun setAvatar() {
        val byteArray = (activity as MainActivity).imgByteArray
        if (byteArray != null) {
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ivUserAvatar.setImageBitmap(bmp)
            (activity as MainActivity).imgByteArray = null
        } else {
            ivUserAvatar.setImageDrawable(resources.getDrawable(R.drawable.image_missing))
        }
    }


}