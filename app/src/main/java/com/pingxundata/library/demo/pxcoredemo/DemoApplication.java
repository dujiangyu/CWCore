package com.pingxundata.library.demo.pxcoredemo;

import com.pingxundata.pxcore.applications.BaseApplication;

/**
 * Created by Administrator on 2017/12/5.
 */

public class DemoApplication extends BaseApplication {
    @Override
    protected void appInit() {
        bridgeClassName=DemoBridge.class.getName();
    }
}
