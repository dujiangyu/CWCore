package com.pingxundata.pxcore.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.view.Gravity;

import com.pingxun.library.commondialog.CommomDialog;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.entity.Wall;
import com.pingxundata.pxmeta.utils.MIUIUtil;
import com.pingxundata.pxmeta.views.DragFloatActionButton;

/**
 * @author Away
 * @version V1.0
 * @Title: IntegralWallManager.java
 * @Description: 积分墙管理器
 * @date 2017/10/27 9:49
 * @copyright 重庆平讯数据
 */
public class WallManager {

    private static Activity mContext;

    private static Wall wall;

    public static Wall with(Activity context, DragFloatActionButton button, String appName, int isOpen) {
        mContext = context;
        wall=new Wall(context, button, appName,isOpen);
        return wall;
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean success = grantResults.length > 0;
        if (requestCode == 701) {
            if (success) {
                for (int i = 0; i < grantResults.length; i++) {
                    String perName = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        int MODE = MIUIUtil.checkAppops(mContext, AppOpsManager.OPSTR_READ_PHONE_STATE);
                        if (MODE == MIUIUtil.MODE_ASK) {
                            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
                            String imeiStr = tm.getDeviceId();
                            //TODO 系统权限设置为询问
//                            if(perName.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)){
//                                wall.doWallClick();
//                                return;
//                            }
                        } else if (MODE == MIUIUtil.MODE_IGNORED) {
                            new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                                if (confirm) {
                                    MIUIUtil.jumpToPermissionsEditorActivity(mContext);
                                    dialog.dismiss();
                                }
                            }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                        } else {
                            if (perName.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)) {
                                wall.doWallClick();
                                return;
                            }
                        }
                    } else {
                        new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                            if(confirm){
                                MIUIUtil.jumpToPermissionsEditorActivity(mContext);
                                dialog.dismiss();
                            }
                        }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                    }
                }
            }
        }
    }

}
