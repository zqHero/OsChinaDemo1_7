package com.hero.zhaoq.oschinademo1_7;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hero.zhaoq.oschinademo1_7.api.ApiClient;
import com.hero.zhaoq.oschinademo1_7.bean.BlogList;
import com.hero.zhaoq.oschinademo1_7.bean.CommentList;
import com.hero.zhaoq.oschinademo1_7.bean.News;
import com.hero.zhaoq.oschinademo1_7.bean.NewsList;
import com.hero.zhaoq.oschinademo1_7.bean.Notice;
import com.hero.zhaoq.oschinademo1_7.common.StringUtils;
import com.hero.zhaoq.oschinademo1_7.common.UIHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  20:21
 */
public class AppContext extends Application{

    public static final int NETTYPE_WIFI = 0x01; //网络连接  标识 wifi
    public static final int NETTYPE_CMWAP = 0x02;//CMWAP 和 CMNET 只是中国移动人为划分的两个GPRS接入方式。前者是为手机WAP上网而设立的，
    public static final int NETTYPE_CMNET = 0x03;// 后者则主要是为PC、笔记本电脑、PDA等利用GPRS上网服务


    public final static String CONF_SCROLL = "perf_scroll";

    public static final int PAGE_SIZE = 20; //默认分页大小

    private boolean login = false;	//登录状态
    private int loginUid = 0;	//登录用户的id

    private Handler unLoginHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                //发送到  主线程    更改ui界面
                UIHelper.ToastMessage(AppContext.this, getString(R.string.msg_login_error));
//                UIHelper.showLoginDialog(AppContext.this);  //弹出  登录界面
            }
        }
    };

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!StringUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


    /**
     * 保存对象
     * @param ser
     * @param file
     * @return
     */
    public boolean saveObject(Serializable ser, String file){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try {
                oos.close();
            } catch (Exception e) {}
            try {
                fos.close();
            } catch (Exception e) {}
        }
    }


    /**
     * 读取对象
     * @param file
     * @return
     */
    private Serializable readObject(String file) {
        if(!isExistDataCache(file)){
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if(e instanceof InvalidClassException){
                File data = getFileStreamPath(file);
                data.delete();
            }
            e.printStackTrace();
        }finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 判断缓存数据是否可读
     * @param cachefile
     * @return
     */
    private boolean isReadDataCache(String cachefile)
    {
        return readObject(cachefile) != null;
    }

    /**
     * 判断  缓存是否存在
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile){
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if(data.exists()){
            exist = true;
        }
        return exist;
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if(info == null) info = new PackageInfo();
        return info;
    }

    //判断  是否包含 属性值
    public boolean containsProperty(String key){
        Properties props = getProperties();
        return props.containsKey(key);
    }

    //获取  所有  属性信息
    public Properties getProperties(){
        return AppConfig.getAppConfig(this).get();
    }

    //获取配置文件 属性内容
    public String getProperty(String key){
        return AppConfig.getAppConfig(this).get(key);
    }
    //设置配置文件 属性内容：
    public void setProperty(String key,String value){
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取App唯一标识
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if(StringUtils.isEmpty(uniqueID)){
            uniqueID = UUID.randomUUID().toString(); //获取硬件 id
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID); //设置  属性信息
        }
        return uniqueID;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        ApiClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie()
    {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 移除  属性信息
     * @param key
     */
    public void removeProperty(String...key){
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 未登录或修改密码后的处理
     */
    public Handler getUnLoginHandler() {
        return this.unLoginHandler;
    }

    /**
     * 是否加载显示文章图片
     * @return
     */
    public boolean isLoadImage()
    {
        String perf_loadimage = getProperty("perf_loadimage");
        //默认是加载的
        if(StringUtils.isEmpty(perf_loadimage))
            return true;
        else
            return StringUtils.toBool(perf_loadimage);
    }

    /**
     * 加载   资讯列表的信息
     * @param catalog
     * @param pageIndex
     * @param isRefresh
     * @return
     * url:http://www.oschina.net/action/api/news_list?&catalog=1&pageSize=20&pageIndex=2
     */
    public NewsList getNewsList(int catalog, int pageIndex, boolean isRefresh)throws AppException {
        NewsList list = null;
        String key = "newslist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
        //网络 已经连接   并且缓存存在     或者 正刷新
        //isNetworkConnected() &&
        if((!isReadDataCache(key) || isRefresh)) {
            try{
                //请求  数据
                list = ApiClient.getNewsList(this, catalog, pageIndex, PAGE_SIZE);
                if(list != null && pageIndex == 0){
                    Notice notice = list.getNotice();
                    list.setNotice(null);
                    list.setCacheKey(key);
                    //写进  缓存
                    saveObject(list, key);
                    list.setNotice(notice);
                }
            }catch(Exception e){
                list = (NewsList)readObject(key);
                if(list == null)
                    throw e;
            }
        } else {
            //读取   缓存信息
            list = (NewsList)readObject(key);
            if(list == null)
                list = new NewsList();
        }
        return list;
    }

    /**
     * 获取  最新 博客信息
     * @param type
     * @param pageIndex
     * @param isRefresh
     * @return
     */
    public BlogList getBlogList(String type, int pageIndex, boolean isRefresh) {
        BlogList list = null;
        String key = "bloglist_"+type+"_"+pageIndex+"_"+PAGE_SIZE;

        //判断  网络连接
        if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {//  网络已连接  并且 读取缓存数据不为null  可以刷新
            try{
                //请求
                list = ApiClient.getBlogList(this, type, pageIndex, PAGE_SIZE);

                if(list != null && pageIndex == 0){
                    Notice notice = list.getNotice();
                    list.setNotice(null);
                    list.setCacheKey(key);
                    saveObject(list, key);  //缓存对象
                    list.setNotice(notice);
                }
            }catch(AppException e){
                list = (BlogList)readObject(key);
                if(list == null)
                ;
            }
        } else {
            list = (BlogList)readObject(key);
            if(list == null)
                list = new BlogList();
        }
        return list;
    }

    /**
     * 评论列表
     * @param catalog 1新闻 2帖子 3动弹 4动态
     * @param id 某条新闻，帖子，动弹的id 或者某条留言的friendid
     * @param pageIndex
     * @return
     * @throws AppException
     */
    public CommentList getCommentList(int catalog, int id, int pageIndex, boolean isRefresh) throws AppException {
        CommentList list = null;
        String key = "commentlist_"+catalog+"_"+id+"_"+pageIndex+"_"+PAGE_SIZE;
        if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
            try{
                //获取 评论列表
                list = ApiClient.getCommentList(this, catalog, id, pageIndex, PAGE_SIZE);
                if(list != null && pageIndex == 0){
                    Notice notice = list.getNotice();
                    list.setNotice(null);
                    list.setCacheKey(key);
                    saveObject(list, key);
                    list.setNotice(notice);
                }
            }catch(AppException e){
                list = (CommentList)readObject(key);
                if(list == null)
                    throw e;
            }
        } else {
            list = (CommentList)readObject(key);
            if(list == null)
                list = new CommentList();
        }
        return list;
    }

    /**
     * 新闻  详情
     * @param news_id
     * @return
     * http://www.oschina.net/action/api/news_list?&catalog=1&pageSize=20&pageIndex=2
     */
    public News getNews(int news_id, boolean isRefresh) throws AppException {
        News news = null;
        String key = "news_"+news_id;
        if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
            try{
                Log.i("info","++++++++++++++++++++++++++++");
                //获取  资讯  详情
                news = ApiClient.getNewsDetail(this, news_id);
                Log.i("info","----"+news.toString());
                if(news != null){
                    Notice notice = news.getNotice();
                    news.setNotice(null);
                    news.setCacheKey(key);
                    saveObject(news, key);
                    news.setNotice(notice);
                }
            }catch(AppException e){
                news = (News)readObject(key);
                if(news == null)
                    throw e;
            }
        } else {
            news = (News)readObject(key);
            if(news == null)
                news = new News();
        }
        return news;
    }
}
