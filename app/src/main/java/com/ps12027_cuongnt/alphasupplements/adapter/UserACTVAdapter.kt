package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.model.User
import java.util.*
import kotlin.collections.ArrayList


class UserACTVAdapter(
    context: Context,
    userList: List<User>
) :
    ArrayAdapter<User?>(context, 0, userList) {
    private val userListFull: List<User>

    init {
        userListFull = ArrayList(userList)
    }

    override fun getFilter(): Filter {
        return userFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.item_autotextcomplete, parent, false)
        }

        val text_view_name: TextView = view!!.findViewById(R.id.text_view_name)


        val countryItem: User? = getItem(position)
        if (countryItem != null) {
            var phoneNumber: String = countryItem.phoneNumber
            if (phoneNumber.isEmpty()) {
                phoneNumber = "Chưa có sđt"
            }
            text_view_name.text =
                countryItem.fullname.plus(" - ")
                    .plus(phoneNumber)
                    .plus(" - #")
                    .plus(countryItem.userID)
        }



        return view
    }

    private val userFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results: FilterResults = FilterResults()
            val suggestuions = ArrayList<User>()

            if (constraint == null || constraint.isEmpty()) {
                suggestuions.addAll(userListFull)
            } else {
                val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()

                userListFull.forEach { item: User ->
                    if (item.fullname.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        suggestuions.add(item)
                    }
                }
            }
            results.values = suggestuions
            results.count = suggestuions.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results!!.values as ArrayList<User>)
            notifyDataSetChanged()

        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as User).fullname
        }


    }

    override fun getItem(position: Int): User? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}