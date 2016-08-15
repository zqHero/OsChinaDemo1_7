package com.hero.zhaoq.oschinademo1_7.ui;

import android.app.Activity;
import android.app.AppOpsManager;
import android.os.Bundle;

import com.hero.zhaoq.oschinademo1_7.AppManager;

/**
 * 应用程序  activity 的基类
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加到  堆栈中
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //结束  Activity 并从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}
