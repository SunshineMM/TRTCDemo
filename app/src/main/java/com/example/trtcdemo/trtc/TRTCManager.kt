package com.example.trtcdemo.trtc

import android.os.Bundle
import android.widget.Toast
import com.example.trtcdemo.MyApplication
import com.tencent.liteav.TXLiteAVCode
import com.tencent.rtmp.ui.TXCloudVideoView
import com.tencent.trtc.TRTCCloud
import com.tencent.trtc.TRTCCloudDef
import com.tencent.trtc.TRTCCloudListener

/**
 * TRTC 管理类
 */
object TRTCManager {

    lateinit var mTRTCCloud: TRTCCloud

    const val USER_ANCHOR = 1 // 主播
    const val USER_AUDIENCE = 2 // 观众

    val mIsFrontCamera = false //是否为前置摄像头

    /**
     * 创建房间并进入
     * isAnchor 是否为主播
     * mUserId 用户名
     * mRoomId 房间号
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
    fun enterRoom(
        userType: Int,
        mUserId: String,
        mRoomId: Int,
        mTXCloudVideoView: TXCloudVideoView? = null
    ) {
        mTRTCCloud = TRTCCloud.sharedInstance(MyApplication.getContext())//创建 TRTCCloud 单例
        val mTRTCParams = TRTCCloudDef.TRTCParams().apply {
            sdkAppId = GenerateTestUserSig.SDKAPPID
            userId = mUserId
            roomId = mRoomId
            userSig = GenerateTestUserSig.genTestUserSig(userId)
            role =
                if (isAnchor(userType)) TRTCCloudDef.TRTCRoleAnchor else TRTCCloudDef.TRTCRoleAudience
        }

        /**
         * 主播端直接开启音频，视频推流
         * mTXCloudVideoView 主播如果不展示视频 直接传 null
         */
        if (isAnchor(userType)) {
            mTRTCCloud.startLocalPreview(mIsFrontCamera, mTXCloudVideoView)
            mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT)
        }

        /**
         * 设置监听
         */
        mTRTCCloud.setListener(object : TRTCCloudListener() {
            /**
             * 当远端用户开启或关闭麦克风时，您可以通过监听 onUserAudioAvailable(userId，boolean) 来感知到这个状态的变化。
             */
            override fun onUserVideoAvailable(userId: String?, available: Boolean) {
                /**
                 * 为了实现停车场监控，这边主播端不展示视频，则主播端不监听其他用户进入房间用户的视频流
                 */
                if (!isAnchor(userType)) {
                    if (available) {
                        mTRTCCloud.startRemoteView(
                            userId,
                            TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL,
                            mTXCloudVideoView
                        )
                    } else {
                        mTRTCCloud.stopRemoteView(userId)
                    }
                }

            }

            /**
             * 错误返回
             */
            override fun onError(errCode: Int, errMsg: String?, extraInfo: Bundle?) {
                /**
                 * 这里可打印错误日志，Toast可删除
                 */
                Toast.makeText(
                    MyApplication.getContext(),
                    "错误码：$errCode 错误信息：$errMsg",
                    Toast.LENGTH_SHORT
                ).show()

                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom()
                }
            }
        })

        mTRTCCloud.enterRoom(mTRTCParams, TRTCCloudDef.TRTC_APP_SCENE_LIVE)
    }


    /**
     * 退出房间
     */
    fun exitRoom() {
        if (::mTRTCCloud.isInitialized) {
            mTRTCCloud.stopLocalAudio()
            mTRTCCloud.stopLocalPreview()
            mTRTCCloud.exitRoom()
            mTRTCCloud.setListener(null)
        }
        TRTCCloud.destroySharedInstance()//销毁 TRTCCloud单例
    }

    /**
     * 判断是否为主播
     */
    private fun isAnchor(userType: Int) = userType == USER_ANCHOR
}