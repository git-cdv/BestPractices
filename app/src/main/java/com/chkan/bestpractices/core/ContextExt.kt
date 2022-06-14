package com.chkan.bestpractices.core

import android.content.Context
import android.content.res.Configuration

/**
 * @author Dmytro Chkan on 31.05.2022.
 */
fun Context.maxElementForXLarge(normal: Int, xlarge: Int) : Int = if (resources.configuration.screenLayout and
    Configuration.SCREENLAYOUT_SIZE_MASK ==
    Configuration.SCREENLAYOUT_SIZE_XLARGE
) xlarge else normal