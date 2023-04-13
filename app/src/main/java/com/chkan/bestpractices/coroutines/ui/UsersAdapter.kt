package com.chkan.bestpractices.coroutines.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chkan.bestpractices.coroutines.domain.User
import com.chkan.bestpractices.databinding.ItemRvUserBinding

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var data: MutableList<User> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemRvUserBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: User) {
            binding.name.text = item.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val bind = ItemRvUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(bind)
            }
        }
    }

    override fun getItemCount() = data.size

    fun setList(list: List<User>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}