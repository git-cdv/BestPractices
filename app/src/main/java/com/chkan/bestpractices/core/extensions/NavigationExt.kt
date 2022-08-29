package com.chkan.bestpractices.core.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.chkan.bestpractices.R

/**
 * @author Dmytro Chkan on 26.08.2022.
 */
fun Fragment.activityNavController() = requireActivity().findNavController(R.id.nav_host_fragment)

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}