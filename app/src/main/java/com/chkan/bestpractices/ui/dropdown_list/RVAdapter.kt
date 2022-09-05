package com.chkan.bestpractices.ui.dropdown_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chkan.bestpractices.R

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
class RVAdapter(
    private val callback: ClickCallback
) : RecyclerView.Adapter<DropDownViewHolder>() {

    private val list: MutableList<DropDownUi> = mutableListOf()

    fun update(newList: List<DropDownUi>) {
        val callback = DiffUtilCallback(list, newList)
        val result = DiffUtil.calculateDiff(callback)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int) = list[position].viewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DropDownViewHolder(
        LayoutInflater.from(parent.context).inflate(viewType, parent, false), callback
    )

    override fun onBindViewHolder(holder: DropDownViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size
}

class DropDownViewHolder(
    view: View,
    private val callback: ClickCallback
) : RecyclerView.ViewHolder(view) {

    private val textView: TextView = itemView.findViewById(R.id.item_textView)

    fun bind(item: DropDownUi) = with(itemView) {
        item.show(textView)
        setOnClickListener {
            item.handleClick(callback)
        }
    }
}