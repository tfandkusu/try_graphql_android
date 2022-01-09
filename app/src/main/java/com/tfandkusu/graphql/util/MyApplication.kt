package com.tfandkusu.graphql.util

import android.app.Application
import com.tfandkusu.graphql.BuildConfig
import com.tfandkusu.graphql.model.AppInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppInfo.versionName = BuildConfig.VERSION_NAME
    }
}
