package com.pingxundata.pxcore.absactivitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.adapters.RecoListAdapter;
import com.pingxundata.pxcore.applications.BaseApplication;
import com.pingxundata.pxcore.metadata.entity.ProductRecommend;
import com.pingxundata.pxcore.metadata.enums.ENUM_REQUEST_URL;
import com.pingxundata.pxmeta.http.PXHttp;
import com.pingxundata.pxmeta.pojo.RequestResult;
import com.pingxundata.pxmeta.utils.AppUtils;
import com.pingxundata.pxmeta.utils.ObjectHelper;
import com.pingxundata.pxmeta.utils.SharedPrefsUtil;
import com.pingxundata.pxmeta.utils.ToastUtils;
import com.pingxundata.pxmeta.views.PXGridView;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Away
 * @version V1.0
 * @Title: PXRecommendActivity.java
 * @Description: 平讯基础推荐控制器
 * @date 2017/10/20 16:31
 * @copyright 重庆平讯数据
 */
public class PXRecommendActivity extends AppCompatActivity implements PXHttp.OnResultHandler {

    protected TextView tv_topview_title;

    protected RelativeLayout iv_topview_back;

    protected ImageView top_back_btn;

    protected SmartRefreshLayout recommend_refresh;

    protected TextView success_msg;

    protected PXGridView recommendList;

    protected ScrollView containerScroll;

    protected String appName;

    protected String productId;

    protected String sourceProductId;

    protected String productName;

    protected String channelNo;

    protected String applyArea;

    protected String actualDetailActivity;

    protected int backImg;

    protected int titleColor;

    protected int topBack;

    int WEB_SIN = 101;

    RecoListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullTransparency();
        setContentView(R.layout.recommend_page);
        BaseApplication.addActivity(this);

        Bundle bundle = this.getIntent().getExtras();
        productId = bundle.getString("productId");
        productName = bundle.getString("productName");
        appName = bundle.getString("appName");
        channelNo = bundle.getString("channelNo");
        applyArea = bundle.getString("applyArea");
        sourceProductId = bundle.getString("sourceProductId");
        actualDetailActivity=bundle.getString("actualDetailActivity");

        writeDataToSp(bundle);
        tv_topview_title = (TextView) findViewById(R.id.tv_topview_title);
        tv_topview_title.setText("相似产品");
        iv_topview_back = (RelativeLayout) findViewById(R.id.iv_topview_back);
        iv_topview_back.setOnClickListener(view -> BaseApplication.clearActivity());
        top_back_btn = (ImageView) findViewById(R.id.top_back_btn);
        success_msg = (TextView) findViewById(R.id.success_msg);
        recommendList = (PXGridView) findViewById(R.id.recommendList);
        containerScroll = (ScrollView) findViewById(R.id.containerScroll);
        recommend_refresh = (SmartRefreshLayout) findViewById(R.id.recommend_refresh);
        recommend_refresh.setRefreshHeader(new PhoenixHeader(this));
        recommend_refresh.setOnRefreshListener(refreshlayout -> initData());
        recommend_refresh.autoRefresh();
    }

    public void initData() {
        getSpdata();
        setResources();
        containerScroll.smoothScrollTo(0, 0);
        success_msg.setFocusable(true);
        success_msg.setFocusableInTouchMode(true);
        success_msg.requestFocus();
        success_msg.setText("感谢您使用" + productName);
        doGetRecomendData(productId);
    }

    /**
     * @Author: Away
     * @Description: 写入数据到sp
     * @Param: bundle
     * @Return void
     * @Date 2017/11/9 14:14
     * @Copyright 重庆平讯数据
     */
    private void writeDataToSp(Bundle bundle){
        if(ObjectHelper.isEmpty(bundle.getString("productId")))return;
        SharedPrefsUtil.putValue(this,"recoTempData","productId",bundle.getString("productId"));
        SharedPrefsUtil.putValue(this,"recoTempData","productName",bundle.getString("productName"));
        SharedPrefsUtil.putValue(this,"recoTempData","appName",bundle.getString("appName"));
        SharedPrefsUtil.putValue(this,"recoTempData","channelNo",bundle.getString("channelNo"));
        SharedPrefsUtil.putValue(this,"recoTempData","applyArea",bundle.getString("applyArea"));
        SharedPrefsUtil.putValue(this,"recoTempData","sourceProductId",bundle.getString("sourceProductId"));
        SharedPrefsUtil.putValue(this,"recoTempData","actualDetailActivity",bundle.getString("actualDetailActivity"));
        SharedPrefsUtil.putValue(this,"recoTempData","backImg",bundle.getInt("backImg"));
        SharedPrefsUtil.putValue(this,"recoTempData","titleColor",bundle.getInt("titleColor"));
        SharedPrefsUtil.putValue(this,"recoTempData","topBack",bundle.getInt("topBack"));
    }

    private void getSpdata(){
        productId=SharedPrefsUtil.getValue(this,"recoTempData","productId","");
        productName=SharedPrefsUtil.getValue(this,"recoTempData","productName","");
        appName=SharedPrefsUtil.getValue(this,"recoTempData","appName","");
        channelNo=SharedPrefsUtil.getValue(this,"recoTempData","channelNo","");
        applyArea=SharedPrefsUtil.getValue(this,"recoTempData","applyArea","");
        sourceProductId=SharedPrefsUtil.getValue(this,"recoTempData","sourceProductId","");
        actualDetailActivity=SharedPrefsUtil.getValue(this,"recoTempData","actualDetailActivity","");
        backImg=SharedPrefsUtil.getValue(this,"recoTempData","backImg",0);
        titleColor=SharedPrefsUtil.getValue(this,"recoTempData","titleColor",0);
        topBack=SharedPrefsUtil.getValue(this,"recoTempData","topBack",0);
    }

    @Override
    public void onResult(RequestResult requestResult, String jsonStr, int flag) {
        switch (flag) {
            case 0:
                if (requestResult.isSuccess()) {
                    if (ObjectHelper.isEmpty(adapter)) {
                        adapter = new RecoListAdapter(this, object -> {
                            ProductRecommend lineData = (ProductRecommend) object;
                            productId = lineData.getId() + "";
                            productName = lineData.getName();
                            if(ObjectHelper.isNotEmpty(actualDetailActivity)){
                                try {
                                    Intent intent = new Intent(PXRecommendActivity.this, Class.forName(actualDetailActivity));
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", lineData.getUrl());
                                    bundle.putString("productName", lineData.getName());
                                    bundle.putString("sourceProductId", sourceProductId);
                                    bundle.putString("productId", productId);
                                    bundle.putString("applyArea", applyArea);
                                    bundle.putString("channelNo", channelNo);
                                    bundle.putString("appName",appName);
                                    bundle.putInt("backImg",backImg);
                                    bundle.putInt("titleColor",titleColor);
                                    bundle.putInt("topBack",topBack);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, 2006);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        recommendList.setAdapter(adapter);
                        adapter.setData((List<ProductRecommend>) requestResult.getResultList());
                    } else {
                        adapter.setData((List<ProductRecommend>) requestResult.getResultList());
                    }
                }
                recommend_refresh.finishRefresh();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (ObjectHelper.isNotEmpty(requestCode) && requestCode == 2006) {
//            recommend_refresh.autoRefresh();
//        }
    }

    @Override
    public void onError(int flag) {
        ToastUtils.showToast(this, "网络请求出错");
    }

    public void doGetRecomendData(String productId) {
        Map<String, String> params = new HashMap<>();
        params.put("productId", productId);
        params.put("versionNo", appName+ AppUtils.getVersionCode(getApplicationContext()));
        params.put("channelNo", channelNo);
        PXHttp.getInstance().setHandleInterface(this).getJson(ENUM_REQUEST_URL.DOMAIN + ENUM_REQUEST_URL.APPLY_RECOMMEND, params, 0, ProductRecommend.class);
    }

    @Override
    public void onBackPressed() {
        BaseApplication.clearActivity();
    }

    /**
     * 5.0以上的手机设置全透明状态栏
     */
    private void setFullTransparency() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    private void setResources(){
        try{
            ImageView backBtn=(ImageView) findViewById(R.id.top_back_btn);
            if(ObjectHelper.isNotEmpty(backImg)){
                backBtn.setImageResource(backImg);
            }
            TextView title=(TextView) findViewById(R.id.tv_topview_title);
            if(ObjectHelper.isNotEmpty(titleColor)){
                title.setTextColor(titleColor);
            }
            RelativeLayout top_container=(RelativeLayout) findViewById(R.id.top_container);
            if(ObjectHelper.isNotEmpty(topBack)){
                top_container.setBackgroundResource(topBack);
            }
        }catch (Exception e){
            Log.e("100032","",e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recommend_refresh.autoRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
