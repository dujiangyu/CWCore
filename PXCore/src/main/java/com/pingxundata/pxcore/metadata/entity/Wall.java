package com.pingxundata.pxcore.metadata.entity;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;

import com.pingxun.library.commondialog.CommomDialog;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.enums.ENUM_REQUEST_URL;
import com.pingxundata.pxcore.views.WallPopupView;
import com.pingxundata.pxmeta.http.PXHttp;
import com.pingxundata.pxmeta.pojo.*;
import com.pingxundata.pxmeta.pojo.RequestResult;
import com.pingxundata.pxmeta.utils.MD5Utils;
import com.pingxundata.pxmeta.utils.MIUIUtil;
import com.pingxundata.pxmeta.utils.ObjectHelper;
import com.pingxundata.pxmeta.views.DragFloatActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/27.
 */

public class Wall {

    private Activity mContext;

    private WallPopupView wallPop;

    private String mAppName;

    private String imei16;

    private boolean isSend = false;

    private DragFloatActionButton mButton;

    static final int SEND_WALL = 1001001;

    public Wall(Activity context, DragFloatActionButton button, String appName, int isOpen) {
        this.mContext = context;
        this.mAppName = appName;
        this.mButton = button;
        if (ObjectHelper.isNotEmpty(button)) {
            if (ObjectHelper.isNotEmpty(isOpen)) {
                if (isOpen == 0) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
            } else {
                button.setVisibility(View.GONE);
            }
        }
    }

    public Wall() {

    }

    public void doWall() {
        if (ObjectHelper.isNotEmpty(mButton) && ObjectHelper.isNotEmpty(this.mContext)) {
            mButton.setmOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doWallClick();
                }
            });
        }
    }

    public void doWallClick() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                List<String> needRequestPers = new ArrayList<String>();
                if (mContext.shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    needRequestPers.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (needRequestPers.size() > 0) {
                    new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                        mContext.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 701);
                        dialog.dismiss();
                    }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                } else {
                    mContext.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 701);
                }
            } else {
                //针对小米手机的权限检查
                int MODE = MIUIUtil.checkAppops(mContext, AppOpsManager.OPSTR_READ_PHONE_STATE);
                if (MODE == MIUIUtil.MODE_ASK) {
                    mContext.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 701);
                } else if (MODE == MIUIUtil.MODE_IGNORED) {
                    new CommomDialog(mContext, R.style.dialog, "请开启获取手机信息权限，以便提高服务质量", (dialog, confirm) -> {
                        MIUIUtil.jumpToPermissionsEditorActivity(mContext);
                        dialog.dismiss();
                    }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                } else {
                    doPost();
                    doPop();
                }
            }
        } else {
            doPost();
            doPop();
        }
    }

    private void doPost() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        String imeiStr = tm.getDeviceId();
        if (ObjectHelper.isNotEmpty(imeiStr) && !imeiStr.equalsIgnoreCase("null")) {
            imei16 = MD5Utils.encode(imeiStr).substring(8, 24).toUpperCase();
            String encryptStr = MD5Utils.encode(imeiStr + imei16 + "pingxundata1234567890");
            //发送数据
            Map<String, String> conditions = new HashMap<>();
            conditions.put("deviceNo", imeiStr);
            conditions.put("code", imei16);
            conditions.put("encryptStr", encryptStr);
            conditions.put("appName", mAppName);
            if (!isSend) {
                PXHttp.getInstance().setHandleInterface(onHander).upJson(ENUM_REQUEST_URL.DOMAIN + ENUM_REQUEST_URL.WALL, new JSONObject(conditions), SEND_WALL, String.class);
            }
        }
    }

    private void doPop() {
        if (ObjectHelper.isEmpty(wallPop)) {
            wallPop = new WallPopupView((Activity) mContext, imei16);
        }
        wallPop.setPopupWindowFullScreen(true);
        if (wallPop.isShowing()) {
            wallPop.dismiss();
        } else {
            wallPop.showPopupWindow();
        }
    }

    //    private PXHttp.OnResultHandler onHander = new PXHttp.OnResultHandler() {
//        @Override
//        public void onResult(RequestResult requestResult, String jsonStr, int flag) {
//            isSend=true;
//        }
//
//        @Override
//        public void onError(int flag) {
//
//        }
//    };
    private PXHttp.OnResultHandler onHander = new PXHttp.OnResultHandler() {
        @Override
        public void onResult(RequestResult requestResult, String jsonStr, int flag) {
            isSend = true;
        }

        @Override
        public void onError(int flag) {

        }
    };
}
