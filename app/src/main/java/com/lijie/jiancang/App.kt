package com.lijie.jiancang

import ando.file.core.FileOperator
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        FileOperator.init(this, BuildConfig.DEBUG)
    }

    companion object {
        lateinit var appContext: Application
    }

}