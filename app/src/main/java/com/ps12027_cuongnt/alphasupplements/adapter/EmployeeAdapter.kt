package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.dao.AlphaDAO
import com.ps12027_cuongnt.alphasupplements.fragment.customer.CustomerDetailFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeAddEditFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeDetailFragment
import com.ps12027_cuongnt.alphasupplements.model.User
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class EmployeeAdapter(
    val context: Context, userList: ArrayList<User>
) :
    RecyclerView.Adapter<EmployeeAdapter.UserViewHolder?>(), Filterable {


    var userList: ArrayList<User> = ArrayList()
    var filteredList: ArrayList<User> = ArrayList()
    private var customFilter: CustomFilter? = null
    var alphaDAO: AlphaDAO
    private var df: DecimalFormat = DecimalFormat("###,### VNĐ")


    init {
        this.userList = userList
        filteredList = userList
        customFilter = CustomFilter(context)
        alphaDAO = AlphaDAO(context)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val currentItem = filteredList[position]
        holder.tvUserName.text = currentItem.fullname
        holder.tvUserPhoneNumber.text = currentItem.phoneNumber
        holder.tvAddress.text = currentItem.address

        if (currentItem.image != null) {
            val bitmap =
                BitmapFactory.decodeByteArray(currentItem.image, 0, currentItem.image!!.size)
            holder.ivUserAvatar.setImageBitmap(bitmap)
        } else {
            holder.ivUserAvatar.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }
        holder.clUser.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                EmployeeDetailFragment(currentItem.userID),
                "Replace","EmployeeDetail"
            )
        }

        holder.clUser.setOnLongClickListener {
            showDialog(currentItem)
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter? {
        return customFilter
    }

    inner class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val ivUserAvatar: ImageView = itemView.findViewById(R.id.ivUserAvatar)
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvUserPhoneNumber: TextView = itemView.findViewById(R.id.tvUserPhoneNumber)
        val clUser: ConstraintLayout = itemView.findViewById(R.id.clUser)

    }

    inner class CustomFilter(val context: Context) : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            val newList =
                userList
            val resultList: ArrayList<User> = ArrayList()
            val searchValue = constraint.toString().toLowerCase(Locale.ROOT)
            for (i in newList.indices) {
                val currentItemFilter = newList[i]
                val title = currentItemFilter.fullname.plus(currentItemFilter.phoneNumber)
                    .plus(currentItemFilter.userID)
                    .plus(currentItemFilter.address)
                if (title.toLowerCase(Locale.ROOT).contains(searchValue)) {
                    resultList.add(currentItemFilter)
                }
            }
            filterResults.count = resultList.size
            filterResults.values = resultList
            return filterResults
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            filteredList =
                results.values as ArrayList<User>
            notifyDataSetChanged()
        }
    }

    fun showDialog(currentItem: User) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        dialog.setContentView(R.layout.dialog_longclick_action_employee)

        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvView = dialog.findViewById<TextView>(R.id.tvView)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        val tvFire = dialog.findViewById<TextView>(R.id.tvFire)
        val ivAvatar = dialog.findViewById<ImageView>(R.id.ivAvatar)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)

        tvName!!.text = currentItem.fullname
        if (currentItem.image != null) {
            ivAvatar!!.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    currentItem.image,
                    0,
                    currentItem.image!!.size
                )
            )
        } else {
            ivAvatar!!.setImageDrawable(context.getDrawable(R.drawable.image_missing))
        }
        tvView!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                EmployeeDetailFragment(currentItem.userID),
                "Replace","EmployeeDetail"
            )
            dialog.dismiss()
        }
        tvEdit!!.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(
                EmployeeAddEditFragment(currentItem.userID, "Edit"),
                "Replace","EmployeeAddEdit"
            )
            dialog.dismiss()
        }
        tvFire!!.setOnClickListener {
            tvFire.text = "Ấn 1 lần nữa để xác nhận"
            tvFire.setOnClickListener {
                currentItem.type = 3
                if (alphaDAO.updateUser(currentItem) && alphaDAO.deleteAccount(currentItem.userID)) {
                    Toast.makeText(
                        context,
                        "Đã sa thải!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    userList.remove(currentItem)
                    notifyDataSetChanged()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Sa thải thất bại!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        ivClose!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
