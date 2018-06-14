package com.pingxundata.pxcore.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxmeta.utils.DensityUtil;

public class FloatViewService extends Service {

	private static final String TAG = "FloatViewService";
	// 定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private LayoutParams wmParams;
	private LinearLayout.LayoutParams layoutParams;
	// 创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;

	private ImageButton mFloatView;
	private int screenHeight;
	private int screenWidth;
	private MyCountDownTimer myCountDownTimer;

	private static OnClickListener onClick;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		screenHeight = ScreenParam.getInstance().height;
		screenWidth = ScreenParam.getInstance().width;
		createFloatView();
		myCountDownTimer = new MyCountDownTimer(2500, 1000);
		myCountDownTimer.start();
	}

	private void createFloatView() {
		wmParams = new LayoutParams();
		// 通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_TOAST;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.RIGHT;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = -400;
		// 设置悬浮窗口长宽数据
		wmParams.width = LayoutParams.WRAP_CONTENT;
		wmParams.height = LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(
				R.layout.alert_window_menu, null);
		mFloatLayout.setPadding(0,0, DensityUtil.dip2px(this,-25),0);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// 浮动窗口按钮
		mFloatView = (ImageButton) mFloatLayout
				.findViewById(R.id.alert_window_imagebtn);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		// 设置监听浮动窗口的触摸移动
		mFloatView.setOnTouchListener(new OnTouchListener() {

			boolean isClick;
			private int leftDistance;
			private float rawX;
			private float rawY;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mFloatLayout.setPadding(0,0,0,0);
					wmParams.windowAnimations=0;
					Log.e(TAG, "ACTION_DOWN");
					mFloatLayout.setAlpha(1.0f);
					myCountDownTimer.cancel();
					
					rawX = event.getRawX();
					rawY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					mFloatLayout.setPadding(0,0,0,0);
					Log.e(TAG, "ACTION_MOVE");
					// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
					int distanceX = (int) (event.getRawX()-rawX);
					int distanceY = (int) (event.getRawY()-rawY);
					leftDistance = (int) event.getRawX()
							+ mFloatView.getMeasuredWidth() / 2;
					
					wmParams.x = wmParams.x-distanceX;
					wmParams.y = wmParams.y+distanceY;
					// 刷新
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					rawX = event.getRawX();
					rawY = event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Log.e(TAG, "ACTION_UP");
					myCountDownTimer.start();
					if(wmParams.x>leftDistance){
						wmParams.x = screenWidth-mFloatView.getMeasuredWidth() / 2;
					}else{
						wmParams.x = 0;
					}
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					if(wmParams.x>leftDistance){
						mFloatLayout.setPadding(DensityUtil.dip2px(FloatViewService.this,-25),0,0,0);
					}else{
						mFloatLayout.setPadding(0,0,DensityUtil.dip2px(FloatViewService.this,-25),0);
					}
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					break;
				}
				return false;
			}
		});

		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
//		        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//		                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
//				Toast.makeText(FloatViewService.this, "hello!",
//						Toast.LENGTH_SHORT).show();
				onClick.onClick(v);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mFloatLayout != null) {
			// 移除悬浮窗口
			mWindowManager.removeView(mFloatLayout);
		}
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
        	mFloatLayout.setAlpha(0.4f);
        }
    }

    public static void setOnClick(OnClickListener onClickListener){
		onClick=onClickListener;
	}

}
