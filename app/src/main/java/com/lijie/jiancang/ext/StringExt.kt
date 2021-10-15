package com.lijie.jiancang.ext

import android.annotation.SuppressLint
import android.widget.Toast
import com.lijie.jiancang.App

var toast: Toast? = null

@SuppressLint("ShowToast")
fun String.toast() {
    toast?.let {
        it.setText(this)
        it.show()

    } ?: run {
        toast = Toast.makeText(App.appContext, this, Toast.LENGTH_SHORT)
        toast?.show()
    }
}