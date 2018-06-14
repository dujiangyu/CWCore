package com.pingxundata.pxcore.autoupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.utils.HttpsUtil;
import com.pingxundata.pxmeta.utils.ObjectHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author feicien (ithcheng@gmail.com)
 * @since 2016-07-05 19:21
 */
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private static final String url = Constants.UPDATE_URL;

    CheckUpdateTask(Context context, int type, boolean showProgressDialog) {

        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;

    }


    protected void onPreExecute() {
//        if (mShowProgressDialog) {
//            dialog = new ProgressDialog(mContext);
//            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
//            dialog.show();
//        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (ObjectHelper.isNotEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONObject obj = new JSONObject(result);
            String status=obj.getString(Constants.STAUS);
            if(status.equalsIgnoreCase("true")){
                JSONObject dataJson=obj.getJSONObject(Constants.DATA);
                String updateMessage = dataJson.getString("memo");
                String apkUrl = dataJson.getString(Constants.APK_DOWNLOAD_URL);
                int apkCode = dataJson.getInt(Constants.APK_VERSION_CODE);

                int versionCode = AppUtils.getVersionCode(mContext);

                if (apkCode > versionCode) {
                    if (mType == Constants.TYPE_NOTIFICATION) {
                        showNotification(mContext, updateMessage, apkUrl);
                    } else if (mType == Constants.TYPE_DIALOG) {
                        showDialog(mContext, updateMessage, apkUrl);
                    }
                } /*else if (mShowProgressDialog) {
                Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
            }*/
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }


    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }

    /**
     * Show Notification
     */
    private void showNotification(Context context, String content, String apkUrl) {
        Intent myIntent = new Intent(context, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = context.getApplicationInfo().icon;
        Notification notify = new NotificationCompat.Builder(context)
                .setTicker(context.getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle(context.getString(R.string.android_auto_update_notify_content))
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11911, notify);
    }

    @Override
    protected String doInBackground(Void... args) {
        Map<String,String> conditions=new HashMap<String, String>();
        conditions.put("channelNo",Constants.chanelNo);
        return HttpsUtil.doGet(url,conditions);
    }

}
