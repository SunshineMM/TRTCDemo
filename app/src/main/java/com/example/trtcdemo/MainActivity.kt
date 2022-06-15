package com.example.trtcdemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.trtcdemo.extensions.setSingleClick
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
     * 创建房间的参数：
     * SDKAppID	应用 ID	您可以在 实时音视频控制台 中找到这个 SDKAppID，如果没有就单击“创建应用”按钮创建一个新的应用。	数字	1400000123
     * userId	用户 ID	即用户名，只允许包含大小写英文字母（a-z、A-Z）、数字（0-9）及下划线和连词符。注意 TRTC 不支持同一个 userId 在两台不同的设备上同时进入房间，否则会相互干扰。	字符串	“denny” 或者 “123321”
     * userSig	进房鉴权票据	您可以使用 SDKAppID 和 userId 计算出 userSig，计算方法请参见 如何计算及使用 UserSig 。	字符串	eJyrVareCeYrSy1SslI...
     * roomId	房间号	数字类型的房间号。注意如果您想使用字符串类型的房间号，请使用 strRoomId 字段，而不要使用 roomId 字段，因为 strRoomId 和 roomId 不可以混用。	数字	29834
     * strRoomId	房间号	字符串类型的房间号。注意 strRoomId 和 roomId 不可以混用，“123” 和 123 在 TRTC 后台服务看来并不是同一个房间。	数字	29834
     * role	角色	分为“主播”和“观众”两种角色，只有当 TRTCAppScene 被指定为 TRTCAppSceneLIVE 或 TRTCAppSceneVoiceChatRoom 这两种直播场景时才需要指定该字段。	枚举值	TRTCRoleAnchor 或 TRTCRoleAudience
     * 详情查看：https://cloud.tencent.com/document/product/647/74634
     *
     */
    val mRoomId = 102410241024 // 房间号
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

        }

        //点击选择观众
        btn_audience.setSingleClick {

        }
    }


    /**
     * 检查权限 含android 6.0及以上设备需授权：相机，录音
     */
    private fun checkPermission(){
        PermissionX.init(this)
            .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .request { allGranted, _, deniedList ->
                if (!allGranted) {
                    Toast.makeText(this, "以下必要权限被拒绝: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }
}