package com.pingxundata.pxcore.views;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxmeta.views.basepopupview.BasePopupWindow;


/**
 * Created by LH on 2017/8/6.
 * 联系我们dialog
 */

public class WeChatPopupView extends BasePopupWindow implements View.OnClickListener{

    private ImageView cancel_button;
    private ImageView wechat_image;

    private Activity mContext;



    public WeChatPopupView(Activity context, String imgUrl) {
        super(context);
        this.mContext=context;
        cancel_button= (ImageView) findViewById(R.id.cancel_button);
        wechat_image= (ImageView) findViewById(R.id.wechat_image);
        Glide.with(context).load(imgUrl).into(wechat_image);
        setViewClickListener(this,cancel_button,wechat_image);
    }

    @Override
    protected Animation initShowAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2F, 1.0f);
        alphaAnimation.setDuration(90);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.7F,1.05F,0.7F,1.05F,ScaleAnimation.INFINITE,0.5F,ScaleAnimation.INFINITE,0.5F);
        scaleAnimation.setDuration(135);
        ScaleAnimation scaleAnimation2=new ScaleAnimation(1.05F,0.95F,1.05F,0.95F,ScaleAnimation.INFINITE,0.5F,ScaleAnimation.INFINITE,0.5F);
        scaleAnimation2.setDuration(105);
        scaleAnimation2.setStartOffset(135);
        ScaleAnimation scaleAnimation3=new ScaleAnimation(0.95F,1F,0.95F,1F,ScaleAnimation.INFINITE,0.5F,ScaleAnimation.INFINITE,0.5F);
        scaleAnimation3.setDuration(60);
        scaleAnimation3.setStartOffset(240);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(scaleAnimation2);
        animationSet.addAnimation(scaleAnimation3);
        return animationSet;
    }


    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.wechat_pop);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.wechatParentContent);
    }

//    @Override
//    protected Animation initShowAnimation() {
//        //摇一摇动画
//        AnimationSet set=new AnimationSet(false);
//        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        shakeAnima.setInterpolator(new CycleInterpolator(5));
//        shakeAnima.setDuration(400);
//        set.addAnimation(getDefaultAlphaAnimation());
//        set.addAnimation(shakeAnima);
//        return set;
//    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_button) {
            dismiss();
        } else if (i == R.id.wechat_image) {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
            }

        } else {
        }


    }
}
