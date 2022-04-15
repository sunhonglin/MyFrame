package com.sunhonglin.base.theme

import android.content.res.ColorStateList
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunhonglin.base.R
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.skin.handler.QMUISkinRuleColorStateListHandler

class AppSkinRuleTintListHandler : QMUISkinRuleColorStateListHandler() {
    companion object {
        const val NAME = "appTintList"
    }

    override fun handle(view: View, name: String, colorStateList: ColorStateList?) {
        when (view) {
            is BottomNavigationView -> {
                view.itemIconTintList = colorStateList
                view.itemTextColor = colorStateList
            }
            is SwitchCompat -> {
                view.backgroundTintList = colorStateList
            }
            is AppCompatCheckBox -> {
                view.buttonTintList = colorStateList
            }
            is LinearLayoutCompat -> {
                view.backgroundTintList = colorStateList
            }
            else -> {
                QMUISkinHelper.warnRuleNotSupport(view, name)
            }
        }
    }
}

fun BottomNavigationView.setAppSkinRuleTintListHandler() {
    QMUISkinHelper.setSkinValue(
        this, QMUISkinValueBuilder.acquire().custom(
            AppSkinRuleTintListHandler.NAME, R.attr.app_skin_selector_color_cold_heat_999
        )
    )
}

fun SwitchCompat.setAppSkinRuleTintListHandler() {
    QMUISkinHelper.setSkinValue(
        this, QMUISkinValueBuilder.acquire().custom(
            AppSkinRuleTintListHandler.NAME, R.attr.app_skin_selector_color_cold_heat_dc
        )
    )
}

fun AppCompatCheckBox.setAppSkinRuleTintListHandler() {
    QMUISkinHelper.setSkinValue(
        this, QMUISkinValueBuilder.acquire().custom(
            AppSkinRuleTintListHandler.NAME, R.attr.app_skin_selector_color_cold_heat_999
        )
    )
}

fun LinearLayoutCompat.setAppSkinRuleTintListHandler() {
    QMUISkinHelper.setSkinValue(
        this, QMUISkinValueBuilder.acquire().custom(
            AppSkinRuleTintListHandler.NAME, R.attr.app_skin_color_base
        )
    )
}