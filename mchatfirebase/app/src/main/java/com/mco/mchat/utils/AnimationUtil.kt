package com.mco.mchat.utils

import androidx.navigation.NavOptions
import com.mco.mchat.R

val animationOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_left)
    .setExitAnim(R.anim.wait_anim)
    .setPopEnterAnim(R.anim.wait_anim)
    .setPopExitAnim(R.anim.slide_right).build()


val refreshToken = NavOptions.Builder()
    .setLaunchSingleTop(true)
    .setEnterAnim(R.anim.slide_left)
    .setExitAnim(R.anim.wait_anim)
    .setPopEnterAnim(R.anim.wait_anim)
    .setPopExitAnim(R.anim.slide_right).build()

