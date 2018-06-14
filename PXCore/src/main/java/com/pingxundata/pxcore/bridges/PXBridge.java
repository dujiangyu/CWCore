package com.pingxundata.pxcore.bridges;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.pingxundata.pxcore.applications.BaseApplication;
import com.pingxundata.pxmeta.utils.ObjectHelper;
import com.pingxundata.pxmeta.utils.ToastUtils;

import java.lang.reflect.Method;


/**
 * @author Away
 * @version V1.0
 * @Title: PXBridge.java
 * @Description: 用于html页面与android之间的交互桥梁
 * @date 2017/11/27 16:31
 * @copyright 重庆平讯数据
 */
public class PXBridge {

    public Object extenders;

    public Activity mActivity;

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @JavascriptInterface
    public void doVoid() {

    }

    @JavascriptInterface
    public void excute(String methodeName) {
        if (ObjectHelper.isNotEmpty(mActivity)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (ObjectHelper.isNotEmpty(BaseApplication.bridgeClassName)) {
                            extenders = Class.forName(BaseApplication.bridgeClassName).newInstance();
                            Method method=extenders.getClass().getMethod(methodeName);
                            Method method1=extenders.getClass().getMethod("setActivity",Activity.class);
                            method1.invoke(extenders,mActivity);
                            method.invoke(extenders);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
