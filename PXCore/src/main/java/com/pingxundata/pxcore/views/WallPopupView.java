package com.pingxundata.pxcore.views;

import android.app.Activity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxmeta.views.basepopupview.BasePopupWindow;


/**
* @Title: WallPopupView.java
* @Description: 积分墙弹窗
* @author Away
* @date 2017/10/27 11:17
* @copyright 重庆平讯数据
* @version V1.0
*/
public class WallPopupView extends BasePopupWindow implements View.OnClickListener{

    private TextView unionCode;

    private Activity mContext;



    public WallPopupView(Activity context, String codeStr) {
        super(context);
        this.mContext=context;
        unionCode= (TextView) findViewById(R.id.unionCode);
        unionCode.setText(codeStr);
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
        return createPopupById(R.layout.wall_pop);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.wallParentContent);
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

    }
}
