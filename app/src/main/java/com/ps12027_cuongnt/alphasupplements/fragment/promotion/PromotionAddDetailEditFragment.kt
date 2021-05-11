package com.ps12027_cuongnt.alphasupplements.fragment.promotion

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.ps12027_cuongnt.alphasupplements.model.Promotion
import java.text.DecimalFormat
import java.util.*
import kotlin.time.milliseconds

class PromotionAddDetailEditFragment(private val promotionID: Int, val type: String?) :
    Fragment() {
    private lateinit var alphaDAO: AlphaDAO

    /*  */
    private lateinit var edtPromotionEndDate: EditText
    private lateinit var edtPromotionStartDate: EditText
    private lateinit var edtPromotionAmount: EditText
    private lateinit var edtPromotionCode: EditText
    private lateinit var btnAdd: Button
    private lateinit var tvTitle: TextView
    private lateinit var ivDoneName: ImageView
    private lateinit var datePickerDialog: DatePickerDialog
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_promotion_add_detail_edit, container, false)

        ivDoneName = view.findViewById(R.id.ivDoneName)
        tvTitle = view.findViewById(R.id.tvTitle)
        edtPromotionEndDate = view.findViewById(R.id.edtPromotionEndDate)
        edtPromotionStartDate = view.findViewById(R.id.edtPromotionStartDate)
        edtPromotionAmount = view.findViewById(R.id.edtPromotionAmount)
        edtPromotionCode = view.findViewById(R.id.edtPromotionCode)
        btnAdd = view.findViewById(R.id.btnAdd)

        alphaDAO = AlphaDAO(context!!)
        return view
    }

    override fun onResume() {
        super.onResume()

        when (type) {
            "Add" -> {
                tvTitle.text = "Thêm mới khuyến mãi"
                btnAdd.setOnClickListener {
                    val amount = edtPromotionAmount.text.toString()
                    val code = edtPromotionCode.text.toString()
                    val endDate = edtPromotionEndDate.text.toString()
                    val startDate = edtPromotionStartDate.text.toString()
                    if (code.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (startDate > endDate) {
                        Toast.makeText(
                            context,
                            "Ngày ngày bắt đầu phải trước ngày kết thúc",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            val newPromotion =
                                Promotion(0, code, amount.toInt(), startDate, endDate)
                            if (alphaDAO.addPromotion(newPromotion)) {

                                Toast.makeText(activity, "Thêm thành công!", Toast.LENGTH_SHORT)
                                    .show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(activity, "Thêm thất bại!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
            }
            "Edit" -> {
                tvTitle.text = "Cập nhật khuyến mãi"
                val currentItem = alphaDAO.getPromotionByPromotionID(promotionID)
                // Fill data
                edtPromotionAmount.setText(currentItem.amount.toString())
                edtPromotionCode.setText(currentItem.code)
                edtPromotionEndDate.setText(currentItem.endDate)
                edtPromotionStartDate.setText(currentItem.startDate)
                /* */
                btnAdd.setOnClickListener {
                    val amount = edtPromotionAmount.text.toString()
                    val code = edtPromotionCode.text.toString()
                    val startDate = edtPromotionStartDate.text.toString()
                    val endDate = edtPromotionEndDate.text.toString()
                    if (code.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Các trường không được để trống!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (startDate > endDate) {
                        Toast.makeText(
                            context,
                            "Ngày ngày bắt đầu phải trước ngày kết thúc",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            val newPromotion =
                                Promotion(
                                    currentItem.id,
                                    code,
                                    amount.toInt(),
                                    startDate,
                                    endDate
                                )
                            if (alphaDAO.updatePromotion(newPromotion)) {

                                /* */
                                Toast.makeText(
                                    activity,
                                    "Cập nhật thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                (activity as MainActivity?)!!.back()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Cập nhật thất bại!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                }
            }
            "View" -> {
                tvTitle.text = "Thông tin khuyến mãi"
                edtPromotionCode.isEnabled = false
                edtPromotionAmount.isEnabled = false
                edtPromotionEndDate.isEnabled = false
                edtPromotionStartDate.isEnabled = false
                val currentItem = alphaDAO.getPromotionByPromotionID(promotionID)
                edtPromotionCode.setText(currentItem.code)
                edtPromotionAmount.setText(df.format(currentItem.amount))
                edtPromotionStartDate.setText(currentItem.startDate)
                edtPromotionEndDate.setText(currentItem.endDate)
                btnAdd.visibility = View.GONE
            }
        }

        edtPromotionEndDate.setOnClickListener {
            // Get today
            val calendar = Calendar.getInstance()
            var d = calendar[Calendar.DAY_OF_MONTH]
            var m = calendar[Calendar.MONTH]
            var y = calendar[Calendar.YEAR]
            val selectedDay: String = edtPromotionEndDate.text.toString()
            Log.i("", "Selected day: $selectedDay")
            val splitArr =
                selectedDay.split("-".toRegex()).toTypedArray()
            val currentDay = "$y/$m/$d"
            Log.i("", "Current day: $currentDay")
            if (selectedDay != currentDay && selectedDay.isNotEmpty()) {
                y = splitArr[0].toInt()
                m = splitArr[1].toInt() - 1
                d = splitArr[2].toInt()
            }
            // Chọn ngày
            // Chọn ngày
            datePickerDialog = DatePickerDialog(
                context!!,
                OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    var formattedMonth = "" + month
                    var formattedDayOfMonth = "" + dayOfMonth
                    if (month < 10) {
                        formattedMonth = "0$month"
                    }
                    if (dayOfMonth < 10) {
                        formattedDayOfMonth = "0$dayOfMonth"
                    }
                    edtPromotionEndDate.setText("$year-$formattedMonth-$formattedDayOfMonth")
                }, y, m, d
            )
            datePickerDialog.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                "Xác nhận",
                datePickerDialog
            )
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Huỷ", datePickerDialog)
            datePickerDialog.show()
        }
        edtPromotionStartDate.setOnClickListener {
            // Get today
            val calendar = Calendar.getInstance()
            var d = calendar[Calendar.DAY_OF_MONTH]
            var m = calendar[Calendar.MONTH]
            var y = calendar[Calendar.YEAR]
            val selectedDay: String = edtPromotionStartDate.text.toString()
            Log.i("", "Selected day: $selectedDay")
            val splitArr =
                selectedDay.split("-".toRegex()).toTypedArray()
            val currentDay = "$y/$m/$d"
            Log.i("", "Current day: $currentDay")
            if (selectedDay != currentDay && selectedDay.isNotEmpty()) {
                y = splitArr[0].toInt()
                m = splitArr[1].toInt() - 1
                d = splitArr[2].toInt()
            }
            // Chọn ngày
            // Chọn ngày
            datePickerDialog = DatePickerDialog(
                context!!,
                OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    var formattedMonth = "" + month
                    var formattedDayOfMonth = "" + dayOfMonth
                    if (month < 10) {
                        formattedMonth = "0$month"
                    }
                    if (dayOfMonth < 10) {
                        formattedDayOfMonth = "0$dayOfMonth"
                    }
                    edtPromotionStartDate.setText("$year-$formattedMonth-$formattedDayOfMonth")
                }, y, m, d
            )
            datePickerDialog.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                "Xác nhận",
                datePickerDialog
            )
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Huỷ", datePickerDialog)
            datePickerDialog.show()
        }

    }

}