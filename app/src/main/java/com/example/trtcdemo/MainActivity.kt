package com.example.trtcdemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.trtcdemo.extensions.setSingleClick
import com.example.trtcdemo.trtc.TRTCManager
import com.permissionx.guolindev.PermissionX
import com.tencent.rtmp.ui.TXCloudVideoView
import com.tencent.trtc.TRTCCloud
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /**
     * 这边为了模拟停车场出入口监控效果，房间号和场地终端（主播）用户名设置为默认，观察端（观众）的用户名自定义，这边设置为随机，模拟可多台设备同时勘察场地的场景
     *
     * 注意：TRTC 不支持同一个 userId 在两台不同的设备上同时进入房间，否则会相互干扰。
     *
     */
    val mRoomId = 10241024 // 房间号
    val anchorUserId = "LiuJian0715" // 主播（场地终端）
    var audienceUserId = "" //观众，用户名这边设置为随机

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        initParameter()
        initListener()
    }

    /**
     * 初始化参数
     */
    private fun initParameter() {
        // 设置观众用户名
        audienceUserId = System.currentTimeMillis().toString()
    }

    /**
     * 初始化监听事件
     */
    private fun initListener() {
        //点击选择主播
        btn_anchor.setSingleClick {
            /**
             * 主播进入房间 如果不需要展示视频 第四个参数（mTXCloudVideoView）不传或者传null即可
             */
            TRTCManager.enterRoom(TRTCManager.USER_ANCHOR, anchorUserId, mRoomId, video_view)
        }

        //点击选择观众
        btn_audience.setSingleClick {
            /**
             * 观众进入房间
             */
            TRTCManager.enterRoom(TRTCManager.USER_AUDIENCE, audienceUserId, mRoomId, video_view)
        }

        //退出房间
        btn_exit.setSingleClick {
            TRTCManager.exitRoom()
        }
    }


    /**
     * 检查权限 含android 6.0及以上设备需授权：相机，录音
     */
    private fun checkPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .request { allGranted, _, deniedList ->
                if (!allGranted) {
                    Toast.makeText(this, "以下必要权限被拒绝: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        TRTCManager.exitRoom()
    }
}