package com.pingxundata.pxcore.utils;


import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.pingxundata.pxcore.metadata.staticdatas.CacheData;
import com.pingxundata.pxmeta.utils.ObjectHelper;
import com.pingxundata.pxmeta.utils.StreamTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 忽略Https证书是否正确的Https Post请求工具类
 * <p/>
 * created by OuyangPeng on 2016/1/17.
 */
public class HttpsUtil {
    private static final String DEFAULT_CHARSET = "UTF-8"; // 默认字符集
    private static final String _GET = "GET"; // GET
    private static final String _POST = "POST";// POST

    private HttpsHander hander;

    private static HttpsUtil instance;

    private static Activity mContext;

    /**
     * 初始化http请求参数
     */
    private static HttpsURLConnection initHttps(String url, String method, Map<String, String> headers)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        //清空上次请求结果
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL _url = new URL(url);
        HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
        // 设置域名校验
        http.setHostnameVerifier(new TrustAnyHostnameVerifier());
        http.setSSLSocketFactory(ssf);
        // 连接超时
        http.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        http.setReadTimeout(25000);
        http.setRequestMethod(method);

//        // 设置User-Agent: Fiddler
//        http.setRequestProperty("ser-Agent", "Fiddler");
//        // 设置contentType
//        http.setRequestProperty("Content-Type", "application/json");

        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (ObjectHelper.isNotEmpty(CacheData.JSESSIONID)) {
            http.setRequestProperty("Cookie", CacheData.JSESSIONID);
        }
        if (null != headers && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        return http;
    }

    private static String getUnsafe(String requestUrl) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);

            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection.setRequestMethod("GET");
//                        httpsURLConnection.setHostnameVerifier();

            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            httpsURLConnection.setRequestProperty("Cookie", CacheData.JSESSIONID);
            httpsURLConnection.setHostnameVerifier(hostnameVerifier);
            InputStream in = httpsURLConnection.getInputStream();
            byte[] bt = StreamTool.readStream(in);
            String result = new String(bt);
            httpsURLConnection.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ERROR", "getUnSafeFromServer:", e.getCause());
            return null;
        }
    }

    /**
     * get请求
     */
    public static String doGet(String url, Map<String, String> params) {
        if (ObjectHelper.isNotEmpty(url)) {
            String requestUrl = initParams(url, params);
            Log.i("abc", "doGet: " + getUnsafe(requestUrl));
            return getUnsafe(requestUrl);
        } else {
            return null;
        }
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params) {
        if (ObjectHelper.isNotEmpty(url)) {
            return post(url, params, null);
        } else {
            return null;
        }
    }


    /**
     * post请求
     */
    private static String post(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuffer bufferRes = null;
        try {
            String jsonParams = "";
            if (ObjectHelper.isNotEmpty(params)) {
                jsonParams = JSON.toJSONString(params);
            }
            HttpURLConnection http = null;
            http = initHttps(url, _POST, headers);
            OutputStream out = http.getOutputStream();
            out.write(jsonParams.getBytes());
            out.flush();
            out.close();

            InputStream in = http.getInputStream();
            if (ObjectHelper.isNotEmpty(http.getHeaderFields().get("Set-Cookie"))) {
                List<String> cookies = http.getHeaderFields().get("Set-Cookie");
                for (String temp : cookies) {
                    if (ObjectHelper.isNotEmpty(cookies)) {
                        if (temp.contains("JSESSIONID")) {
                            CacheData.JSESSIONID = temp;
                        }
                    }
                }
            }
            BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
            String valueString = null;
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                http.disconnect();// 关闭连接
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化参数
     */
    private static String initParams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        sb.append(map2Url(params));
        return sb.toString();
    }

    /**
     * map转url参数
     */
    private static String map2Url(Map<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (null != value && !"".equals(value.trim())) {
                try {
                    url.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url.toString();
    }

    /**
     * 检测是否https
     */
    private static boolean isHttps(String url) {
        return url.startsWith("https");
    }

    /**
     * 不进行主机名确认
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 信任所有主机 对于任何证书都不做SSL检测
     * 安全验证机制，而Android采用的是X509验证
     */
    private static class MyX509TrustManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    public static HttpsUtil init(HttpsHander hander, Activity context) {
        if (ObjectHelper.isEmpty(instance)) {
            HttpsUtil ins = new HttpsUtil();
            instance = ins;
        }
        mContext = context;
        instance.hander = hander;
        return instance;
    }

    public void doPostWithHander(String url, Map<String, String> params, int channel) {
        new Thread(() -> {
            String resultStr = doPost(url, params);
            if (ObjectHelper.isNotEmpty(mContext)) {
                mContext.runOnUiThread(() -> instance.hander.onResult(resultStr, channel));
            } else {
                instance.hander.onResult(resultStr, channel);
            }
        }).start();
    }

    public void doGetWithHander(String url, Map<String, String> params, int channel) {
        new Thread(() -> {
            String resultStr = doGet(url, params);
            if (ObjectHelper.isNotEmpty(mContext)) {
                mContext.runOnUiThread(() -> instance.hander.onResult(resultStr, channel));
            } else {
                instance.hander.onResult(resultStr, channel);
            }
        }).start();
    }

    public interface HttpsHander {
        public void onResult(String jsonStr, int chanel);
    }
}
