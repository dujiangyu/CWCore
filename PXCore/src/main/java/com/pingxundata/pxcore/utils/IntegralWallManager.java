package com.pingxundata.pxcore.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;

import com.pingxun.library.commondialog.CommomDialog;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.metadata.enums.ENUM_REQUEST_URL;
import com.pingxundata.pxcore.metadata.interfaces.IFunction;
import com.pingxundata.pxcore.services.FloatViewServiceManager;
import com.pingxundata.pxcore.views.WallPopupView;
import com.pingxundata.pxmeta.http.PXHttp;
import com.pingxundata.pxmeta.utils.MD5Utils;
import com.pingxundata.pxmeta.utils.MIUIUtil;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Away
 * @version V1.0
 * @Title: IntegralWallManager.java
 * @Description: 积分墙管理器
 * @date 2017/10/27 9:49
 * @copyright 重庆平讯数据
 */
public class IntegralWallManager {


    private static Activity mContext;

    static WallPopupView wallPop;

    static final int SEND_WALL=1001001;

    static String mAppName;


    public static void integralWindow(int switchCode,Activity context,String appName) {
        if(ObjectHelper.isNotEmpty(context)){
            mContext = context;
            mAppName=appName;
            if(ObjectHelper.isNotEmpty(switchCode)&&switchCode==0){
                permissionCheckAndDo(mContext);
            }
        }
    }

    private static void permissionCheckAndDo(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                List<String> needRequestPers=new ArrayList<String>();
                if (context.shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    needRequestPers.add(Manifest.permission.READ_PHONE_STATE);
                }
                if(context.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    needRequestPers.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if(needRequestPers.size()>0){
                    new CommomDialog(context, R.style.dialog, "请开启获取手机信息和定位权限，以便提高服务质量", (dialog, confirm) -> {
                        context.requestPermissions((String[])(needRequestPers.toArray()), 701);
                        dialog.dismiss();
                    }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
                }
                else {
                    context.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION}, 701);
                }
            }
            //针对小米手机的权限检查
            int MODE = MIUIUtil.checkAppops(context, AppOpsManager.OPSTR_READ_PHONE_STATE);
            if (MODE == MIUIUtil.MODE_ASK) {
                context.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 701);
            } else if (MODE == MIUIUtil.MODE_IGNORED) {
//                new CommomDialog(this, R.style.dialog, "请在 系统设置-权限管理 中开启定位权限，以便能及时获取您的位置", (dialog, confirm) -> {
//                    if (confirm) {
//                        com.pingxun.library.guomeilibrary.meijie.MIUIUtil.jumpToPermissionsEditorActivity(this);
//                        dialog.dismiss();
//                    }
//                }).setTitle("权限").setContentPosition(Gravity.CENTER).show();
            }else {
                readIMEIAndDoPop(context);
            }
        }else{
            readIMEIAndDoPop(context);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, IFunction iFunction){
        boolean success = grantResults.length > 0;
        if(requestCode==701){
            if(success){
                for(int i=0;i<grantResults.length;i++){
                    String perName=permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        int MODE = MIUIUtil.checkAppops(mContext, AppOpsManager.OPSTR_READ_PHONE_STATE);
                        if (MODE == MIUIUtil.MODE_ASK) {
                            //TODO 系统权限设置为询问
                        } else if (MODE == MIUIUtil.MODE_IGNORED) {
                            //TODO 系统权限设置为忽略
                        } else {
                            if(perName.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)){
                                readIMEIAndDoPop(mContext);
                            }
                        }
                    }
                }
                iFunction.doFunction("");
            }
        }
    }

    private static void readIMEIAndDoPop(Activity context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imeiStr=tm.getDeviceId();
        if(ObjectHelper.isNotEmpty(imeiStr)&&!imeiStr.equalsIgnoreCase("null")){
            String imei16= MD5Utils.encode(imeiStr).substring(8,24).toUpperCase();
            String encryptStr=MD5Utils.encode(imeiStr+imei16+"pingxundata1234567890");
            //发送数据
            Map<String,String> conditions=new HashMap<>();
            conditions.put("deviceNo",imeiStr);
            conditions.put("code",imei16);
            conditions.put("encryptStr",encryptStr);
            conditions.put("appName",mAppName);
            PXHttp.getInstance().setHandleInterface(onHander).upJson(ENUM_REQUEST_URL.DOMAIN+ENUM_REQUEST_URL.WALL,new JSONObject(conditions),SEND_WALL,String.class);
            FloatViewServiceManager.showFloatView(context);
            FloatViewServiceManager.setOnClickCallBack(view -> {
                if(ObjectHelper.isEmpty(wallPop)){
                    wallPop = new WallPopupView(context,imei16);
                }
                wallPop.setPopupWindowFullScreen(true);
                if(wallPop.isShowing()){
                    wallPop.dismiss();
                }else{
                    wallPop.showPopupWindow();
                }
            });
        }
    }

    private static PXHttp.OnResultHandler onHander=new PXHttp.OnResultHandler() {
        @Override
        public void onResult(com.pingxundata.pxmeta.pojo.RequestResult requestResult, String s, int i) {

        }

        @Override
        public void onError(int i) {

        }
    };
}
