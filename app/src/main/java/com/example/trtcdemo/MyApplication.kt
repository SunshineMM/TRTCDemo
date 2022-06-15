package com.example.trtcdemo

import android.app.Application
import android.content.Context

class MyApplication : Application(){
    companion object{
        lateinit var mContext: Application
        fun getContext(): Context{
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}