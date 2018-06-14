package com.pingxundata.library.demo.pxcoredemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pingxundata.pxcore.absactivitys.PXWebViewActivity;
import com.pingxundata.pxmeta.utils.ActivityUtil;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DemoWebActivity extends PXWebViewActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.demo_webview);
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goForward(this,MainActivity.class,true);
    }
}
