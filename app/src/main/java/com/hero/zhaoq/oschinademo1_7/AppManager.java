package com.hero.zhaoq.oschinademo1_7;

import android.app.Activity;

import java.util.Stack;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  21:14
 * app的管理类
 */
public class AppManager {

    private static AppManager instance;

    /**
     * 单一实例
     * @return
     */
    public static AppManager getAppManager() {
        if(instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    //管理  activity的栈
    private Stack<Activity> activityStack;

    /**
     * 添加  Activity的堆栈中
     * @param activity
     */
    public void addActivity(Activity activity) {
        if(activityStack == null){
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void finishActivity(Activity activity) {
        //将  当前的activity 取出  并销毁
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
}
