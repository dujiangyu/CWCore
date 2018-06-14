package com.pingxundata.pxcore.metadata.staticdatas;


import java.util.HashMap;
import java.util.Map;

public class CacheData {

    /**
     * 服务器session
     */
    public static String JSESSIONID;

    /**
     * 渠道编号
     */
    public static String chanelNo;

    /**
     * 更新链接
     */
    public static String UPDATE_URL;

    /**
     * 全局变量
     */
    public static Map<String, String> GLOBALPARAMS = null;

    static {
        GLOBALPARAMS=new HashMap<>();
    }
}
