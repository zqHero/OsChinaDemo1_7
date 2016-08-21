package com.hero.zhaoq.oschinademo1_7;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

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

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 推出应用程序
     * @param context
     */
    public void AppExit(Context context){

        finishAllActivity();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(context.getPackageName());
        System.exit(0);
    }

    /**
     * 全部出栈 销毁
     */
    private void finishAllActivity() {
        for(int i=0, size = activityStack.size();i<size;i++){
            if(null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
