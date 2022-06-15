package com.example.trtcdemo.trtc

import com.example.trtcdemo.MyApplication
import com.tencent.liteav.base.ContextUtils.getApplicationContext
import com.tencent.trtc.TRTCCloud

/**
 * TRTC 管理类
 */
object TRTCManager {

    val mTRTCCloud by lazy { TRTCCloud.sharedInstance(MyApplication.getContext()) }


    fun init(){

    }
}