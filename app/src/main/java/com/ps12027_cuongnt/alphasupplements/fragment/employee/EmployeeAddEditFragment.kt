package com.ps12027_cuongnt.alphasupplements.fragment.employee

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
import com.ps12027_cuongnt.alphasupplements.fragment.cart.CustomerSelectDialog
import com.ps12027_cuongnt.alphasupplements.listener.CustomerChoose
import com.ps12027_cuongnt.alphasupplements.model.Account
import com.ps12027_cuongnt.alphasupplements.model.User
import kotlinx.android.synthetic.main.fragment_employee_add_edit.*

class EmployeeAddEditFragment : Fragment, CustomerChoose {
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
    private lateinit var edtPassword: EditText
    private lateinit var btnAdd: Button
    private lateinit var ivUserAvatar: ImageView
    private lateinit var tvChoose: TextView
    private lateinit var tvPassword: TextView
    private lateinit var alphaDAO: AlphaDAO
    lateinit var currentUser: User
    lateinit var currentAccount: Account
    var customerID: Int? = null

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
        val view = inflater.inflate(R.layout.fragment_employee_add_edit, container, false)

        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle2 = view.findViewById(R.id.tvTitle2)
        edtName = view.findViewById(R.id.edtName)
        tvChoose = view.findViewById(R.id.tvChoose)
        tvPassword = view.findViewById(R.id.tvPassword)
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber)
        edtPassword = view.findViewById(R.id.edtPassword)
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
                tvTitle.text = "Thêm mới nhân viên"
                ivUserAvatar.isClickable = false
                edtName.isEnabled = false
                edtAddress.isEnabled = false
                edtPhoneNumber.isEnabled = false
                tvPassword.visibility = View.GONE
                edtPassword.visibility = View.GONE
                if ((activity as MainActivity).imgByteArray != null) {
                    setAvatar()
                }
                (activity as MainActivity).changeToolbarTitle("Thêm mới nhân viên")
                tvChoose.setOnClickListener {
                    val dialog = CustomerToEmployeeSelectDialog(this)
                    dialog.show(childFragmentManager, "CustomerSelectDialog")
                }
                btnAdd.setOnClickListener {
                    if (customerID != null) {
                        //
                        val currentCustomer = alphaDAO.getUserByID(customerID!!)
                        currentCustomer.type = 2
                        if (alphaDAO.updateUser(currentCustomer)) {
                            Toast.makeText(context, "Thêm nhân viên thành công", Toast.LENGTH_SHORT)
                                .show()
                            (activity as MainActivity).back()
                            alphaDAO.addAccount(
                                Account(
                                    0,
                                    currentCustomer.phoneNumber,
                                    customerID!!
                                )
                            )
                        } else {
                            Toast.makeText(context, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Vui lòng chọn người dùng!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            "Edit" -> {
                if ((activity as MainActivity).currentUserType == 1 || (activity as MainActivity).currentUserType == 2) {
                    tvPassword.visibility = View.GONE
                    edtPassword.visibility = View.GONE
                }
                tvTitle.text = "Cập nhật nhân viên"
                tvChoose.visibility = View.GONE
                tvTitle2.visibility = View.GONE
                currentUser = alphaDAO.getUserByID(userID)
                currentAccount = alphaDAO.getAccountByUserID(currentUser.userID)!!
                (activity as MainActivity).changeToolbarTitle("QL nhân viên")
                edtPhoneNumber.setText(currentUser.phoneNumber)
                edtAddress.setText(currentUser.address)
                edtName.setText(currentUser.fullname)
                edtPassword.setText(currentAccount.password)
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
                    val password = edtPassword.text.toString()
                    if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || password.isEmpty()) {
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
                                    "Cập nhật nhân viên thành công!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                currentAccount.password = password
                                alphaDAO.updateAccount(currentAccount)
                                (activity as MainActivity).back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Cập nhật nhân viên thất bại!",
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

    override fun chooseType(userID: Int) {
        customerID = userID
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
    }


}