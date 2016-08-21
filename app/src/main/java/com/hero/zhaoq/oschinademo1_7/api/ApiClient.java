package com.hero.zhaoq.oschinademo1_7.api;

import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.bean.NewsList;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.HashMap;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.api
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  20:31
 * API:客户端  接口访问   用于访问网络数据
 */
public class ApiClient {


    /***
     *
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
        }});

        try{
            return NewsList.parse(http_get(appContext, newUrl));
        }catch(Exception e){
            if(e instanceof AppException)
                throw (AppException)e;
            throw AppException.network(e);
        }
    }

    private final static int RETRY_TIME = 3;//重新尝试 次数  尝试访问三次  获取数据

    /**
     * get 请求资源信息
     * @param appContext
     * @param url
     */
    private static void http_get(AppContext appContext, String url) throws AppException {
        String cookie = getCookie(appContext); //获取cookie
        String userAgent = getUserAgent(appContext);

        HttpClient httpClient = null;
        GetMethod httpGet = null;

        String responseBody = null;
        int time = 0;

        do {
            httpClient = getHttpClinet();
            httpClient = getHttpClient();
            httpGet = getHttpGet(url, cookie, userAgent);
        }while(time < RETRY_TIME);


    }

    //获取 http请求基本信息：
    private static HttpClient getHttpClinet() {
        HttpClient httpClient = new HttpClient();
        //设置 HttpClient 接收Cookie 用于浏览器  一样的策略  设置策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        return null;
    }

    private static String getUserAgent(AppContext appContext) {
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


}
