package com.chkan.bestpractices.simple_paging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chkan.bestpractices.simple_paging.models.PassengersUIModel
import com.chkan.bestpractices.databinding.ItemRvPassengersBinding

/**
 * @author Dmytro Chkan on 26.05.2022.
 */

class ListPassengersAdapter (private val clickListener: PassListListener): RecyclerView.Adapter<ListPassengersAdapter.ViewHolder>() {

    private var data: MutableList<PassengersUIModel> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item,clickListener)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemRvPassengersBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PassengersUIModel, clickListener: PassListListener) {
            binding.pass = item
            binding.clickListener = clickListener
            binding.logoCompany.load(item.picture)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val bind = ItemRvPassengersBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(bind)
            }
        }
    }

    override fun getItemCount() = data.size

    fun setList(list: List<PassengersUIModel>, sizeList: Int){
        data.addAll(list)
        notifyItemRangeInserted(sizeList+1,list.size)
    }
}

class PassListListener(val clickListener: (id: String) -> Unit) {
    fun onClick(pass: PassengersUIModel) = clickListener(pass.id)
}