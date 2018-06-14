package com.pingxundata.pxcore.callbacks;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pingxundata.pxcore.metadata.staticdatas.CacheData;
import com.pingxundata.pxcore.services.FloatViewServiceManager;
import com.pingxundata.pxmeta.utils.ObjectHelper;

/**
* @Title: AppLifecycleCallbacks.java
* @Description: activity生命周期监听接口
* @author Away
* @date 2017/10/26 17:26
* @copyright 重庆平讯数据
* @version V1.0
*/
public class AppLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private int activeNum;
    private int timeSpan = 15;
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }
    public void onActivityStarted(Activity activity) {
    }
    public void onActivityResumed(Activity activity) {
        Log.e("onActivityResumed====",activity.getLocalClassName());
        //全局悬浮按钮
        String floatViewActivityName= CacheData.GLOBALPARAMS.get("FloatView");
        if(ObjectHelper.isNotEmpty(floatViewActivityName)&&floatViewActivityName.equalsIgnoreCase(activity.getLocalClassName())){
            FloatViewServiceManager.showFloatView(activity);
        }
    }
    public void checkTimeout(Context context) {

    }
    @Override
    public void onActivityPaused(Activity activity) {
    }
    public void onActivityStopped(Activity activity) {
        //全局悬浮按钮
        String floatViewActivityName= CacheData.GLOBALPARAMS.get("FloatView");
        if(ObjectHelper.isNotEmpty(floatViewActivityName)&&floatViewActivityName.equalsIgnoreCase(activity.getLocalClassName())){
            FloatViewServiceManager.terminateFloatView(activity);
        }
    }
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
    public void onActivityDestroyed(Activity activity) {
    }
}
