package com.pingxundata.pxcore.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.pingxundata.pxcore.metadata.staticdatas.CacheData;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import java.util.List;

/**
 * @author Away
 * @version V1.0
 * @Title: FloatViewServiceManager.java
 * @Description: 浮动窗口管理器
 * @date 2017/10/26 16:15
 * @copyright 重庆平讯数据
 */
public class FloatViewServiceManager {


    private static  View.OnClickListener onClickCallBack=null;

    public static View.OnClickListener getOnClickCallBack() {
        return onClickCallBack;
    }

    public static void setOnClickCallBack(View.OnClickListener onClickCallBack) {
        FloatViewServiceManager.onClickCallBack = onClickCallBack;
    }

    /**
     * @Author: Away
     * @Description: 显示悬浮按钮
     * @Param: context
     * @Param: onclick
     * @Return void
     * @Date 2017/10/26 16:45
     * @Copyright 重庆平讯数据
     */
    public static void showFloatView(Activity context) {
        if (ObjectHelper.isNotEmpty(context)) {
            ScreenParam.getInstance().init(context);
            // TODO Auto-generated method stub
            FloatViewService.setOnClick(view ->{
                if(ObjectHelper.isNotEmpty(onClickCallBack)){
                    onClickCallBack.onClick(view);
                }
            });
            Intent intent = new Intent(context, FloatViewService.class);
            //启动FloatViewService
            context.startService(intent);
            if(CacheData.GLOBALPARAMS!=null){
                if(!CacheData.GLOBALPARAMS.containsKey("FloatView")){
                    CacheData.GLOBALPARAMS.put("FloatView",context.getLocalClassName());
                }
            }
        }
    }

    /**
     * @Author: Away
     * @Description: 结束service
     * @Param: context
     * @Return void
     * @Date 2017/10/26 16:49
     * @Copyright 重庆平讯数据
     */
    public static void terminateFloatView(Activity context) {
        if (ObjectHelper.isNotEmpty(context)) {
            if (isServiceWork(context, FloatViewService.class.getName())) {
                context.stopService(new Intent(context, FloatViewService.class));
            }
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        try {
            boolean isWork = false;
            ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
            if (myList.size() <= 0) {
                return false;
            }
            for (int i = 0; i < myList.size(); i++) {
                String mName = myList.get(i).service.getClassName().toString();
                if (mName.equals(serviceName)) {
                    isWork = true;
                    break;
                }
            }
            return isWork;
        } catch (Exception e) {
            return false;
        }
    }

}
