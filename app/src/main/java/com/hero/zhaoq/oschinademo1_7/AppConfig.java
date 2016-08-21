package com.hero.zhaoq.oschinademo1_7;

import android.content.Context;
import android.util.Log;

import com.hero.zhaoq.oschinademo1_7.api.ApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
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
        return props;
    }
}
