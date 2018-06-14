package com.pingxundata.pxcore.autoupdate;

import android.content.Context;
import android.util.Log;

import com.pingxundata.pxcore.metadata.staticdatas.CacheData;
import com.pingxundata.pxmeta.utils.ObjectHelper;

public class UpdateChecker {


    public static void checkForDialog(Context context,String chanelNo,String updateUrl) {
        if (context != null&& ObjectHelper.isNotEmpty(chanelNo)&&ObjectHelper.isNotEmpty(updateUrl)) {
            CacheData.chanelNo=chanelNo;
            CacheData.UPDATE_URL=updateUrl;
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }

    }


}
