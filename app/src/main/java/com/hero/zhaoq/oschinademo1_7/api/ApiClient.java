package com.hero.zhaoq.oschinademo1_7.api;

import android.util.Log;

import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.bean.BlogList;
import com.hero.zhaoq.oschinademo1_7.bean.NewsList;
import com.hero.zhaoq.oschinademo1_7.bean.Result;
import com.hero.zhaoq.oschinademo1_7.bean.URLs;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.api
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  20:31
 * API:客户端  接口访问   用于访问网络数据
 */
public class ApiClient {


    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if(url.indexOf("?")<0)
            url.append('?');

        for(String name : params.keySet()){
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }

        Log.i("info", "请求  url:--"+url);
        return url.toString().replace("?&", "?");
    }

    private final static int RETRY_TIME = 3;//重新尝试 次数  尝试访问三次  获取数据

    /**
     * get 请求资源信息
     * @param appContext
     * @param url
     */
    private static InputStream http_get(AppContext appContext, String url) throws AppException {
        String cookie = getCookie(appContext); //获取cookie
        String userAgent = getUserAgent(appContext); //获取 请求头 信息

        HttpClient httpClient = null;
        GetMethod httpGet = null;

        String responseBody = null;
        int time = 0;

        do {
            try {
                httpClient = getHttpClinet();
                httpGet = getHttpGet(url, cookie, userAgent);
                int statusCode = httpClient.executeMethod(httpGet);

                Log.i("info",statusCode+"-------------");

                if(statusCode != HttpStatus.SC_OK){ //未连接成功  200
                    throw AppException.http(statusCode); //抛出异常
                }
                responseBody = httpGet.getResponseBodyAsString();
                break;
            } catch (HttpException e) {
                time++;
                if(time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {}
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                e.printStackTrace();
                throw AppException.http(e);
            } catch (IOException e) {
                time++;
                if(time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {}
                    continue;
                }
                // 发生网络异常
                e.printStackTrace();
                throw AppException.network(e);
            }
        }while(time < RETRY_TIME);

        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");  //获取到 请求体
        if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
            try {
                Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));
                if(res.getErrorCode() == 0){
                    appContext.Logout(); //用户注销
                    appContext.getUnLoginHandler().sendEmptyMessage(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //返回读取   到的数据
        return new ByteArrayInputStream(responseBody.getBytes());
    }

    //get 请求信息
    private static GetMethod getHttpGet(String url, String cookie, String userAgent) {
        GetMethod httpGet = new GetMethod(url);
        // 设置 请求超时时间
        httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
        httpGet.setRequestHeader("Host", URLs.HOST);//www.oschina.net
        //当服务器收到附带有Connection: Keep-Alive的请求时，
        // 它也会在响应头中添加一个同样的字段来使用Keep-Alive。
        // 这样一来，客户端和服务器之间的HTTP连接就会被保持，不会断开
        httpGet.setRequestHeader("Connection","Keep-Alive");
        httpGet.setRequestHeader("Cookie", cookie);
        httpGet.setRequestHeader("User-Agent", userAgent);
        return httpGet;
    }

    public static final String UTF_8 = "UTF-8"; //字符集
    private final static int TIMEOUT_CONNECTION = 20000;//连接超时  时间
    private final static int TIMEOUT_SOCKET = 20000; //读取  超时时间

    //获取   http请求基本信息：
    private static HttpClient getHttpClinet() {
        HttpClient httpClient = new HttpClient();
        //设置 HttpClient 接收Cookie 用于浏览器  一样的策略  设置策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        //设置  默认的超时  重试 处理策略
        httpClient.getParams().setParameter(
                HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        //设置 连接 超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
        //设置  读取数据超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
        //设置  字符集：
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static String appUserAgent;  //请求 信息

    /**
     * 获取  请求信息
     * @param appContext
     * @return
     */
    private static String getUserAgent(AppContext appContext) {
        if(appUserAgent == null || appUserAgent == ""){
            StringBuilder ua = new StringBuilder("OSChina.NET");
            //app版本  和 版本名
            //appContext.getPackageInfo().versionName     appContext.getPackageInfo().versionCode
            ua.append('/'+"1.7.4"+'_'+"18");
            ua.append("/Android");//手机系统平台
            ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
            ua.append("/"+android.os.Build.MODEL); //手机型号
            ua.append("/"+appContext.getAppId());//客户端唯一标识
            appUserAgent = ua.toString();
        }
        //userAgent:OSChina.NET/1.0_1/Android/5.1/sdk_phone_armv7/d698f02e-3752-40e5-9c3f-6744cf3553b6
        Log.i("info","---userAgent:" + appUserAgent);
        return appUserAgent;
    }

    private static String appCookie;

    /**
     * 获取 cookie信息
     * @param appContext
     * @return
     */
    private static String getCookie(AppContext appContext) {
        if(appCookie == null || appCookie == "") {
            appCookie = appContext.getProperty("cookie");
        }
        return appCookie;
    }


    public static void cleanCookie() {
        appCookie = "";
    }


    /***
     * 获取咨询列表中  最新咨询
     * @param appContext
     * @param catalog
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static NewsList getNewsList(AppContext appContext,final int catalog,
                                       final  int pageIndex,final int pageSize) throws AppException {
        //拼接参数数据
        String newUrl = _MakeURL(URLs.NEWS_LIST, new HashMap<String, Object>(){{
            put("catalog", catalog);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }}); // http://www.oschina.net/action/api/news_list?

        try{
            Log.i("info","get获取到的信息:"+http_get(appContext, newUrl).toString()+"-------");
            return NewsList.parse(http_get(appContext, newUrl));//get  请求方式
//            return null;
        }catch(Exception e){
            if(e instanceof AppException)
                throw (AppException)e;
            throw AppException.network(e);
        }
    }

    /**
     * 获取   最新博客信息
     * @param appContext
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static BlogList getBlogList(AppContext appContext, final String type, final int pageIndex, final int pageSize) throws AppException  {
        String newUrl = _MakeURL(URLs.BLOG_LIST, new HashMap<String, Object>(){{
            put("type", type);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }});

        try{
            return BlogList.parse(http_get(appContext, newUrl));
        }catch(Exception e){
            if(e instanceof AppException)
                throw (AppException)e;
            throw AppException.network(e);
        }
    }
}
