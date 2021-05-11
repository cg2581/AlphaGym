package com.ps12027_cuongnt.alphasupplements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ps12027_cuongnt.alphasupplements.MainActivity
import com.ps12027_cuongnt.alphasupplements.R
import com.ps12027_cuongnt.alphasupplements.fragment.MainFragment
import com.ps12027_cuongnt.alphasupplements.model.Menu


class MenuAdapter(val context: Context, val menuList: ArrayList<Menu>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = menuList[position]

        holder.tvTitle.text = currentItem.title
        holder.ivIcon.setImageResource(currentItem.drawable)
        holder.cv.setOnClickListener {
            (context as MainActivity?)!!.switchFragment(currentItem.fragment, "Replace",currentItem.tag)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val cv: CardView = itemView.findViewById(R.id.cv)

    }


}