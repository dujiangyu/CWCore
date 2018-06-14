package com.pingxundata.pxcore.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.webkit.WebSettings;

import com.pingxundata.pxcore.bridges.PXBridge;
import com.pingxundata.pxmeta.views.NestWebView;
import com.pingxundata.pxmeta.views.SwipeRefreshLayout;


/**
* @Title: BaseWebView.java
* @Description: 基本原件
* @author Away
* @date 2017/9/12 19:03
* @copyright 重庆平讯数据
* @version V1.0
*/
public class BaseWebView extends NestWebView implements SwipeRefreshLayout.CanScrollUpListener {

    boolean mIgnoreTouchCancel;

    protected PXBridge pxSimpleBridge;

    protected Context mContext;

    public BaseWebView(Context context) {
        this(context, null);
        mContext=context;
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    private void init() {
        pxSimpleBridge=new PXBridge();
        try{
            pxSimpleBridge.setActivity((Activity) mContext);
        }catch (Exception e){
            Log.e("20010001","桥设置activity出错",e);
        }
        addJavascriptInterface(pxSimpleBridge,"PXWeb");

        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setDatabaseEnabled(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setSupportMultipleWindows(true);
        webSetting.setNeedInitialFocus(true);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            webSetting.setLoadsImagesAutomatically(true);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webSetting.setLoadsImagesAutomatically(false);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewParent parent = this;
        do {
            if (parent instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) parent).setCanScrollUpListener(this);
                break;
            }
        } while ((parent = parent.getParent()) != null);
    }

    public void ignoreTouchCancel(boolean val) {
        mIgnoreTouchCancel = val;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
                break;

        }
        if (action == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        getParent().requestDisallowInterceptTouchEvent(false);
        // 第一种不完美解决方案
            /*
            boolean ret = super.onTouchEvent(ev);
            if (mPreventParentTouch) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        requestDisallowInterceptTouchEvent(true);
                        ret = true;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        requestDisallowInterceptTouchEvent(false);
                        mPreventParentTouch = false;
                        break;
                }
            }
            return ret;
             */
        // 第二种完美解决方案
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean canScrollUp() {
        return getScrollY() > 0;
    }


}
