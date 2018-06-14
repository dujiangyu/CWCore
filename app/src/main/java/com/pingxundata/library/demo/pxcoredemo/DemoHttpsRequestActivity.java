package com.pingxundata.library.demo.pxcoredemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.pingxundata.pxcore.utils.HttpsUtil;

/**
 * Created by Administrator on 2017/9/14.
 */

public class DemoHttpsRequestActivity extends Activity implements HttpsUtil.HttpsHander {

    private int get=0;

    @Override
    public void onResult(String jsonStr, int chanel) {
//        Button button=(Button)findViewById(R.id.testButton);
//        button.setText(jsonStr);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpsUtil.init(this,DemoHttpsRequestActivity.this).doGetWithHander("http://120.79.255.186/front/product/findRecommendProduct.json",null,get);
    }
}

