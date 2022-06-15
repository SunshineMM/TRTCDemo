package com.example.trtcdemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.permissionx.guolindev.PermissionX
import com.tencent.rtmp.ui.TXCloudVideoView
import com.tencent.trtc.TRTCCloud
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()

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