package com.hero.zhaoq.oschinademo1_7;

import android.content.Context;
import android.util.Log;

import com.hero.zhaoq.oschinademo1_7.api.ApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Properties;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  22:08
 * 配置信息
 */
public class AppConfig {

    private static AppConfig appConfig;
    private Context mContext;

    public final static String CONF_COOKIE = "cookie";
    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID"; //app唯一标示


    //获取 配置信息类
    public static AppConfig getAppConfig(Context context)
    {
        if(appConfig == null){
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    public static BitSet getAppConfig(ApiClient apiClient) {
        return null;
    }

    /**
     * 获取配置  信息  以键值对存在  文件中的数据
     * @param key
     * @return
     */
    public String get(String key)
    {
        Properties props = get();
        return (props!=null)?props.getProperty(key):null;  //获取 cookie 等键值对的信息
    }

    private final static String APP_CONFIG = "config";  //获取配置文件

    //获取  cookie等属性 信息  返回Properties类
    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            //读取files目录下的config
            //fis = activity.openFileInput(APP_CONFIG);

            //读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG,Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("info", "cookie信息-----"+((props!=null)?props.getProperty("cookie"):null));

        return props;
    }

    /**
     * 设置  配置文件 属性内容
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key,value);
        setProps(props); //设置
    }

    //创建属性文件：
    private void setProps(Properties props) {
        FileOutputStream fos = null;

        try {
            //创建  config文件   在files目录下
            //fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);
            //把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG,Context.MODE_PRIVATE);
            File conf = new File(dirConf,APP_CONFIG);
            fos = new FileOutputStream(conf);

            props.store(fos,null);  //存储  文件内容
            fos.flush(); //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除  对象  键值信息
     * @param key
     */
    public void remove(String...key)
    {
        Properties props = get();
        for(String k : key)
            props.remove(k);
        setProps(props);
    }


}
