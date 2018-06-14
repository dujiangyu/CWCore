package com.pingxundata.pxcore.absactivitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pingxundata.pxcore.applications.BaseApplication;
import com.pingxundata.pxmeta.utils.ActivityUtil;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import java.util.List;

/**
* @Title: BaseProductDetailActivity.java
* @Description: 产品详情基础activity
* @author Away
* @date 2017/10/20 14:18
* @copyright 重庆平讯数据
* @version V1.0
*/
public abstract class BaseProductDetailActivity extends AppCompatActivity {

    /**
     * webview回调信号值
     */
    public static final int WEBVIEW_RESULT=2005;

    public String productName;

    public String productId;

    public String appName;

    public String channelNo;

    public String applyArea;

    public String sourceProductId="";

    int backImg;

    int titleColor;

    int topBack;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ObjectHelper.isNotEmpty(requestCode)&&requestCode==WEBVIEW_RESULT){
            //用Bundle携带数据
            Bundle bundleJump = new Bundle();
            //传递name参数为tinyphp
            bundleJump.putString("productId", productId);
            bundleJump.putString("sourceProductId", data.getExtras().getString("sourceProductId"));
            bundleJump.putString("productName", productName);
            bundleJump.putString("appName", appName);
            bundleJump.putString("channelNo",channelNo);
            bundleJump.putString("applyArea",applyArea );
            bundleJump.putString("actualDetailActivity",this.getClass().getName());
            bundleJump.putInt("backImg",this.backImg);
            bundleJump.putInt("titleColor",this.titleColor);
            bundleJump.putInt("topBack",this.topBack);
            ActivityUtil.goForward(this,PXRecommendActivity.class,bundleJump,false);
        }
    }

    /**
     * @Author: Away
     * @Description: 启动webview（通过推荐参数值决定是否激活推荐功能）
     * @Param: recommendCd
     * @Param: dataBundle
     * @Return void
     * @Date 2017/10/20 15:11
     * @Copyright 重庆平讯数据
     */
    public void startWebForRecommend(int recommendCd,Bundle dataBundle,int backImg,int titleColor,int topBack){
        this.productName = dataBundle.getString("productName");
        this.productId = dataBundle.getString("productId");
        this.appName=dataBundle.getString("appName");
        this.sourceProductId=ObjectHelper.isNotEmpty(getIntent().getExtras())?getIntent().getExtras().getString("sourceProductId"):"";
        this.backImg=backImg;
        this.titleColor=titleColor;
        this.topBack=topBack;
        dataBundle.putInt("backImg",this.backImg);
        dataBundle.putInt("titleColor",this.titleColor);
        dataBundle.putInt("topBack",this.topBack);
        dataBundle.putString("sourceProductId",this.sourceProductId);
        //清理已经打开的详情页面
        closeRecommendActivity();
        //添加当前页面
        BaseApplication.addActivity(this);
        //如果开关为推荐
        if(ObjectHelper.isNotEmpty(recommendCd)&&recommendCd==0){
            ActivityUtil.goForward(this, PXSimpleWebViewActivity.class, WEBVIEW_RESULT, dataBundle);
        }else{//一般流程
            ActivityUtil.goForward(this,PXSimpleWebViewActivity.class,dataBundle,true);
        }
    }

    private void closeRecommendActivity(){
        try{
            List<Activity> allActivitys=BaseApplication.activitys;
            if(ObjectHelper.isNotEmpty(allActivitys)){
                for(Activity temp:allActivitys){
                    if(temp.getClass().getName().equalsIgnoreCase(PXRecommendActivity.class.getName())
                            ||temp.getClass().getName().equalsIgnoreCase(this.getClass().getName())){
                        temp.finish();
                    }
                }
            }
        }catch (Exception e){
            Log.e("1001001","立即申请关闭推荐activity出错",e);
        }
    }

}
