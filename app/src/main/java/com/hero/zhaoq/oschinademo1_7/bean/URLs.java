package com.hero.zhaoq.oschinademo1_7.bean;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/22  15:11
 *
 * url 路径
 */
public class URLs {

    public final static String HOST = "www.oschina.net";//125.39.6.139  www.oschina.net

    public final static String HTTP = "http://";
    private final static String URL_SPLITTER = "/";

    private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER; //  http://www.oschina.net/
    public final static String NEWS_LIST = URL_API_HOST+"action/api/news_list";

    public final static String BLOG_LIST = URL_API_HOST+"action/api/blog_list";




}
