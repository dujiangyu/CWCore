package com.pingxundata.pxcore.autoupdate;


import com.pingxundata.pxcore.metadata.staticdatas.CacheData;

class Constants {


    // json {"url":"http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}

    static final String APK_DOWNLOAD_URL = "apkUrl";
    static final String APK_UPDATE_CONTENT = "版本更新";
    static final String APK_VERSION_CODE = "dataVersion";
    static final String STAUS="success";
    static final String DATA="data";
    static final String chanelNo= CacheData.chanelNo;
//    static final String chanelNo= InitDatas.CURRENT_MARKET+"-"+InitDatas.APP_NAME;


    static final int TYPE_NOTIFICATION = 2;

    static final int TYPE_DIALOG = 1;

    static final String TAG = "UpdateChecker";

    static final String UPDATE_URL = CacheData.UPDATE_URL;
}
