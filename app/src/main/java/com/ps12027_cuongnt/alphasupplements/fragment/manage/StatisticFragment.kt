package com.ps12027_cuongnt.alphasupplements.fragment.manage

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.adapter.ProductTotalAdapter
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.product.product.ProductDetailFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticFragment : Fragment(), OnChartValueSelectedListener {
    lateinit var edtFrom: EditText
    lateinit var edtTo: EditText
    lateinit var btnShow: Button
    lateinit var alphaDao: AlphaDAO
    lateinit var tvTotal: TextView
    lateinit var tvProductCount: TextView
    lateinit var chart: PieChart
    lateinit var dataList: ArrayList<ChartItem>
    lateinit var productDataList: ArrayList<ProductData>
    lateinit var alphaDAO: AlphaDAO
    private lateinit var datePickerDialog: DatePickerDialog
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")
    lateinit var dateFrom: String
    lateinit var dateTo: String
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        alphaDAO = AlphaDAO(context!!)
        chart = view.findViewById(R.id.chart)
        edtFrom = view.findViewById(R.id.edtFrom)
        edtTo = view.findViewById(R.id.edtTo)
        btnShow = view.findViewById(R.id.btnShow)
        tvProductCount = view.findViewById(R.id.tvProductCount)
        tvTotal = view.findViewById(R.id.tvTotal)
        (activity as MainActivity?)!!.showUI()
        (activity as MainActivity?)!!.changeToolbarTitle("Thống kê")
        alphaDao = AlphaDAO(context!!)
        //

        edtFrom.setOnClickListener {
            // Get today
            val calendar = Calendar.getInstance()
            var d = calendar[Calendar.DAY_OF_MONTH]
            var m = calendar[Calendar.MONTH]
            var y = calendar[Calendar.YEAR]
            val selectedDay: String = edtFrom.text.toString()
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
            datePickerDialog = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    var formattedMonth = "" + month
                    var formattedDayOfMonth = "" + dayOfMonth
                    if (month < 10) {
                        formattedMonth = "0$month"
                    }
                    if (dayOfMonth < 10) {
                        formattedDayOfMonth = "0$dayOfMonth"
                    }
                    edtFrom.setText("$year-$formattedMonth-$formattedDayOfMonth")
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

        edtTo.setOnClickListener {
            // Get today
            val calendar = Calendar.getInstance()
            var d = calendar[Calendar.DAY_OF_MONTH]
            var m = calendar[Calendar.MONTH]
            var y = calendar[Calendar.YEAR]
            val selectedDay: String = edtTo.text.toString()
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
            datePickerDialog = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    var formattedMonth = "" + month
                    var formattedDayOfMonth = "" + dayOfMonth
                    if (month < 10) {
                        formattedMonth = "0$month"
                    }
                    if (dayOfMonth < 10) {
                        formattedDayOfMonth = "0$dayOfMonth"
                    }
                    edtTo.setText("$year-$formattedMonth-$formattedDayOfMonth")
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


        //
        btnShow.setOnClickListener {
            dateFrom = edtFrom.text.toString()
            dateTo = edtTo.text.toString()
            if (dateFrom.isEmpty() || dateTo.isEmpty()) {
                Toast.makeText(context, "Các trường không được để trống!!!", Toast.LENGTH_SHORT)
                    .show()
            } else if (dateFrom > dateTo) {
                Toast.makeText(
                    context,
                    "Ngày ngày bắt đầu phải trước ngày kết thúc",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dateFrom = dateFrom.plus(" 00:00:01")
                dateTo = dateTo.plus(" 23:59:59")
                val total = alphaDao.getStatisticTotal(dateFrom, dateTo)
                val count = alphaDao.getSoldProductCount(dateFrom, dateTo)
                tvTotal.text = df.format(total)
                tvProductCount.text = count.toString().plus(" sản phẩm")
                dataList = alphaDAO.getAllProductTypeCount(dateFrom, dateTo)
                setData(dataList)
            }

        }
        //
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.centerText = generateCenterSpannableText()
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 30f
        chart.transparentCircleRadius = 0f
        chart.setDrawCenterText(true)
        chart.rotationAngle = 0f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.setOnChartValueSelectedListener(this)
        // chart.spin(2000, 0, 360);
        //

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK)
//        chart.setEntryLabelTypeface(tfRegular)
        chart.setEntryLabelTextSize(10f)
        return view
    }

    private fun setData(dataList: ArrayList<ChartItem>) {
        val entries = ArrayList<PieEntry>()
        for (i in 0 until dataList.size) {
            entries.add(
                PieEntry(
                    dataList[i].value.toFloat(),  //parties[i % parties.length],
                    alphaDao.getProductTypeName(dataList[i].id)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Loại sản phẩm")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors


        // add a lot of colors
        val colors = ArrayList<Int>()
        colors.add(context!!.resources.getColor(R.color.OrangeRed))
        colors.add(context!!.resources.getColor(R.color.DodgerBlue))
        colors.add(context!!.resources.getColor(R.color.LimeGreen))
        colors.add(context!!.resources.getColor(R.color.Yellow))
        colors.add(context!!.resources.getColor(R.color.Orange))
        colors.add(context!!.resources.getColor(R.color.Blue))
/*
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)*/

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTextColor(Color.BLACK)
        //data.setValueTypeface(tfLight)
        chart.data = data

        chart.highlightValues(null)

        chart.invalidate()
    }


    private fun generateCenterSpannableText(): SpannableString? {
        //val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        val s = SpannableString("")
        /*
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)*/
        return s
    }

    override fun onNothingSelected() {
        //
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        showDialog(h!!.x.toInt(), dataList[h!!.x.toInt()].id)
    }

    private fun showDialog(position: Int, id: Int) {
        productDataList = alphaDAO.getAllProductTotalByProductID(id, dateFrom, dateTo)

        val dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_statistic_producttype_product_total)

        val tvProductName = dialog.findViewById<TextView>(R.id.tvProductTypeName)
        val tvProductTypeTotal = dialog.findViewById<TextView>(R.id.tvProductTypeTotal)
        val rcvList = dialog.findViewById<RecyclerView>(R.id.rcvList)
        rcvList!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val productTotalAdapter = ProductTotalAdapter(context!!, productDataList)
        rcvList.adapter = productTotalAdapter
        tvProductName!!.text = alphaDAO.getProductTypeName(id)
        tvProductTypeTotal!!.text = df.format(dataList[position].total)



        dialog.show()
    }

    class ChartItem(val id: Int, val value: Int, val total: Int)
    class ProductData(val name: String, val total: Int)

}
