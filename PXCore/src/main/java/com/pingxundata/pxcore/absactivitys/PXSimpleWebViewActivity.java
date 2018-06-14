package com.pingxundata.pxcore.absactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.applications.BaseApplication;
import com.pingxundata.pxcore.metadata.enums.ENUM_REQUEST_URL;
import com.pingxundata.pxmeta.http.PXHttp;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 网页浏览控件
 */
public class PXSimpleWebViewActivity extends PXWebViewActivity {


    private TextView topTitle;

    private RelativeLayout top_container;

    private RelativeLayout iv_topview_back;

    private ImageView top_back_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.pxsimple_web_view);
        BaseApplication.addActivity(this);
        initDatas();
    }

    void initDatas() {
        top_container = (RelativeLayout) findViewById(R.id.top_container);
        iv_topview_back = (RelativeLayout) findViewById(R.id.iv_topview_back);
        top_back_btn = (ImageView) findViewById(R.id.top_back_btn);
        iv_topview_back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("sourceProductId", this.getIntent().getExtras().getString("productId"));
            setResult(2005, intent);
            finish();
        });
        topTitle = (TextView) findViewById(R.id.tv_topview_title);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收data值
        String url = bundle.getString("url");
        String productName = bundle.getString("productName");
        setResources(bundle);
        doDataPoint(bundle);
        //隐藏后退键
        topTitle.setText(productName);
    }

    private void setResources(Bundle bundle) {
        try {
            int backImg = bundle.getInt("backImg");
            int titleColor = bundle.getInt("titleColor");
            int topBack = bundle.getInt("topBack");
            ImageView backBtn = findViewById(R.id.top_back_btn);
            if (ObjectHelper.isNotEmpty(backImg)) {
                backBtn.setImageResource(backImg);
            }
            TextView title = findViewById(R.id.tv_topview_title);
            if (ObjectHelper.isNotEmpty(titleColor)) {
                title.setTextColor(titleColor);
            }
            RelativeLayout top_container = findViewById(R.id.top_container);
            if (ObjectHelper.isNotEmpty(topBack)) {
                top_container.setBackgroundResource(topBack);
            }
        } catch (Exception e) {
            Log.e("100032", "", e);
        }
    }

    /**
     * 数据埋点
     */
    void doDataPoint(Bundle bundle) {
        try {
            //数据埋点地址
            String url = ENUM_REQUEST_URL.DOMAIN + ENUM_REQUEST_URL.DATAPOINT;
            //手机型号
            String model = android.os.Build.MODEL;
            //设备厂商
            String carrier = android.os.Build.MANUFACTURER;
            //市场编号
            String marketCode = bundle.getString("channelNo");
            String productId = bundle.getString("productId");
            String applyArea = bundle.getString("applyArea");
            String appName = bundle.getString("appName");
            String sourceProductId = bundle.getString("sourceProductId");
            String deviceNumber=bundle.getString("deviceNumber");
            Map<String, String> params = new HashMap<String, String>();
            params.put("productId", productId);
            params.put("deviceNumber", ObjectHelper.isEmpty(deviceNumber)?model + "(" + carrier + ")":deviceNumber);
            params.put("applyArea", applyArea);
            params.put("channelNo", marketCode);
            params.put("appName", appName);
            params.put("sourceProductId", sourceProductId);
            PXHttp.getInstance().setHandleInterface(null).upJson(url, new org.json.JSONObject(params), 10, String.class);
        } catch (Exception e) {
            Log.e("100010", "产品申请数据埋点出错", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && pxWebView.canGoBack()) {
            pxWebView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && !pxWebView.canGoBack()) {
            Intent intent = new Intent();
            intent.putExtra("sourceProductId", this.getIntent().getExtras().getString("productId"));
            setResult(2005, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
