package com.pingxundata.pxcore.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.views.WeChatPopupView;
import com.pingxundata.pxmeta.utils.ObjectHelper;

/**
* @Title: WechatBanner.java
* @Description: 微信banner帮助类
* @author Away
* @date 2017/10/19 10:24
* @copyright 重庆平讯数据
* @version V1.0
*/
public class WechatBanner {

    /**
     * 容器
     */
    private Activity mContext;

    /**
     * banner图
     */
    private ImageView bannerImg;

    private static WechatBanner self;

    /**
     * @Author: Away
     * @Description: 初始化方法
     * @Param: context
     * @Param: url
     * @Return com.pingxundata.pxcore.utils.WechatBanner
     * @Date 2017/10/19 10:44
     * @Copyright 重庆平讯数据
     */
    public static WechatBanner with(Activity context,String url){
        self=new WechatBanner();
        if(ObjectHelper.isNotEmpty(context)&&ObjectHelper.isNotEmpty(url)){
            self.mContext=context;
            self.bannerImg=(ImageView)self.mContext.findViewById(R.id.wechat_banner);
            if(ObjectHelper.isNotEmpty(self.bannerImg)){
                Glide.with(self.mContext).load(url).into(self.bannerImg);
            }
        }
        return self;
    }

    /**
     * @Author: Away
     * @Description: popwindow的显示
     * @Param: popImgUrl
     * @Return void
     * @Date 2017/10/19 10:52
     * @Copyright 重庆平讯数据
     */
    public void pop(String popImgUrl){
        if(ObjectHelper.isNotEmpty(self)&&ObjectHelper.isNotEmpty(self.bannerImg)&&ObjectHelper.isNotEmpty(popImgUrl)){
            self.bannerImg.setOnClickListener(view1 -> {
                WeChatPopupView weChatPopupView = new WeChatPopupView(self.mContext,popImgUrl);
                weChatPopupView.setPopupWindowFullScreen(true);
                weChatPopupView.showPopupWindow();
            });
        }
    }
}
