package com.pingxundata.pxcore.absactivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.views.PXWebView;
import com.pingxundata.pxmeta.utils.AndroidBug5497Workaround;
import com.pingxundata.pxmeta.utils.FileUtils;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Away
 * @version V1.0
 * @Title: PXWebViewActivity.java
 * @Description: 平讯webView抽象activity
 * @date 2017/9/12 15:24
 * @copyright 重庆平讯数据
 */
public abstract class PXWebViewActivity extends Activity {

    public PXWebView pxWebView;

    private ProgressBar progressBar;

    private LinearLayout webviewTools;

    private CircleProgress circle_progress;

    private String mCM;
    private String filePath = "";
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    String compressPath = "";

    public void init(int resourceId) {
        //设置全透明顶部工具栏
        setFullTransparency();
        setContentView(resourceId);
        AndroidBug5497Workaround.assistActivity(this);
        pxWebView = (PXWebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webviewTools = (LinearLayout) findViewById(R.id.webviewTools);
        circle_progress = findViewById(R.id.circle_progress);
        circle_progress.setUnfinishedColor(Color.parseColor("#50000000"));
        circle_progress.setFinishedColor(Color.parseColor("#95000000"));
        if (ObjectHelper.isEmpty(pxWebView)) {
            return;
        }
        pxWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                selectImage();
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e("fileChooseException", "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        filePath = photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        pxWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String ur) {
                if(ur.toLowerCase().startsWith("http")){
                    return false;//设为true使用WebView加载网页而不调用外部浏览器
                }else{
                    return true;
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
            }
        });
        pxWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                webviewTools.setVisibility(View.VISIBLE);
//                DownloadManager.getInstance(getApplication()).download(url, new DownLoadObserver() {
//                    @Override
//                    public void onNext(DownloadInfo value) {
//                        super.onNext(value);
//                        Long total = value.getTotal();
//                        int progress = (int) (value.getProgress() * 100L / total);
//                        updateUi(progress);
//                    }
//                    @Override
//                    public void onComplete() {
//                        webviewTools.setVisibility(View.GONE);
//                        if (downloadInfo != null) {
//                            try {
//                                installAPkWithProvider(new File(downloadInfo.getFilePath()));
//                            } catch (Exception e) {
//                                Log.e("自动安装失败", "webview下载自动安装APK失败", e);
//                            }
//                        }
//                        updateUi(0);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("2100","20002",e);
//                    }
//                });
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //新页面接收数据
        Bundle bundle = getIntent().getExtras();
        if (ObjectHelper.isNotEmpty(bundle)) {
            //接收data值
            String mUrl = bundle.getString("url");
            pxWebView.loadUrl(mUrl);
        }
    }

    /**
     * @Author: Away
     * @Description: 刷新UI
     * @Param: progress
     * @Return void
     * @Date 2017/9/13 19:28
     * @Copyright 重庆平讯数据
     */
    void updateUi(int progress) {
        this.runOnUiThread(() -> {
//            downloadProgress.setProgress(progress);
//            progressStr.setText("(" + progress + "%)");
            circle_progress.setProgress(progress);
        });
    }

    /**
     * 打开图库,同时处理图片
     */
    private void selectImage() {
        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(compressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        compressPath = compressPath + File.separator + "compress.png";
        File image = new File(compressPath);
        if (image.exists()) {
            image.delete();
        }
    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new Date().getTime() + "";
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            // results = new Uri[]{Uri.parse(mCM)};
                            results = new Uri[]{afterChosePic(filePath, compressPath)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                            Log.d("tag", intent.toString());
//              String realFilePath = getRealFilePath(Uri.parse(dataString));
//              results = new Uri[]{afterChosePic(realFilePath, compressPath)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    /**
     * 选择照片后结束
     */
    private Uri afterChosePic(String oldPath, String newPath) {
        File newFile;
        try {
            newFile = FileUtils.compressFile(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }

    private void installAPkWithProvider(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".demoprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && pxWebView.canGoBack()) {
            pxWebView.goBack();
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK && !pxWebView.canGoBack()){
            Bundle recevdBun=getIntent().getExtras();
            if(ObjectHelper.isNotEmpty(recevdBun)&&ObjectHelper.isNotEmpty(recevdBun.getInt("intentFlag"))){
                setResult(recevdBun.getInt("intentFlag"));
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isExternalApplicationUrl(String url) {
        return url.startsWith("vnd.") ||
                url.startsWith("rtsp://") ||
                url.startsWith("itms://") ||
                url.startsWith("itpc://");
    }
}
