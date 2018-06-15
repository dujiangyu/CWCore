package com.pingxundata.library.demo.pxcoredemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.pingxundata.pxmeta.views.basepopupview.BasePopupWindow;


/**
 * Created by LH on 2017/8/6.
 * 联系我们dialog
 */

public class ContactUsPopupView extends BasePopupWindow implements View.OnClickListener{

    private TextView ok;
    private TextView cancel;




    public ContactUsPopupView(Activity context) {
        super(context);
        ok= (TextView) findViewById(R.id.tv_right);
        cancel= (TextView) findViewById(R.id.tv_left);
        setViewClickListener(this,ok,cancel);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }


    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.dialog_call_phone);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
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
        switch (v.getId()){
            case R.id.tv_left:
                dismiss();
                break;
            case R.id.tv_right:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15723372456"));
                getContext().startActivity(phoneIntent);
                break;
            default:

                break;
        }


    }
}
