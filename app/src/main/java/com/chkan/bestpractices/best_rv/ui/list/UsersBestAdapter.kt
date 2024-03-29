package com.chkan.bestpractices.best_rv.ui.list

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chkan.bestpractices.R
import com.chkan.bestpractices.best_rv.data.models.User
import com.chkan.bestpractices.best_rv.data.models.UserListItem
import com.chkan.bestpractices.databinding.ItemUserBinding

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)

    fun onUserFire(user: User)

}

class UsersBestAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<UsersBestAdapter.UsersViewHolder>(), View.OnClickListener {

    var users: List<UserListItem> = emptyList()
        set(newValue) {
            val diffCallback = UsersDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val user = v.tag as User
        when (v.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userListItem = users[position]
        val user = userListItem.user

        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewButton.tag = user

            if (userListItem.isInProgress) {
                moreImageViewButton.visibility = View.INVISIBLE
                itemProgressBar.visibility = View.VISIBLE
                holder.binding.root.setOnClickListener(null)
            } else {
                moreImageViewButton.visibility = View.VISIBLE
                itemProgressBar.visibility = View.GONE
                holder.binding.root.setOnClickListener(this@UsersBestAdapter)
            }

            userNameTextView.text = user.name
            userCompanyTextView.text = if (user.company.isNotBlank()) user.company else "[Unemployed]"
            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
            } else {
                Glide.with(photoImageView.context).clear(photoImageView)
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
                // you can also use the following code instead of these two lines ^
                // Glide.with(photoImageView.context)
                //        .load(R.drawable.ic_user_avatar)
                //        .into(photoImageView)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val user = view.tag as User
        val position = users.indexOfFirst { it.user.id == user.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Move up").apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, "Move down").apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")
        if (user.company.isNotBlank()) {
            popupMenu.menu.add(0, ID_FIRE, Menu.NONE, "Fire")
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onUserMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onUserMove(user, 1)
                }
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
                ID_FIRE -> {
                    actionListener.onUserFire(user)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }
}

class UsersDiffCallback(
    private val oldList: List<UserListItem>,
    private val newList: List<UserListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.user.id == newUser.user.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser
    }

}