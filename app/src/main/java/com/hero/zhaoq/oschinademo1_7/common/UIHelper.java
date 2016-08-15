package com.hero.zhaoq.oschinademo1_7.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hero.zhaoq.oschinademo1_7.ui.Main;
import com.hero.zhaoq.oschinademo1_7.ui.QuestionPub;
import com.hero.zhaoq.oschinademo1_7.ui.Search;
import com.hero.zhaoq.oschinademo1_7.ui.TweetPub;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.common
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  21:04
 */
public class UIHelper {

    public final static int REQUEST_CODE_FOR_RESULT = 0x01;

    /**
     * 显示  动弹页面
     * @param context
     */
    public static void showTweetPub(Activity context) {
        Intent intent = new Intent(context,TweetPub.class);
        context.startActivityForResult(intent,REQUEST_CODE_FOR_RESULT);
    }

    /**
     * 显示  问题 页   发布问题
     * @param context
     */
    public static void showQuestionPub(Context context) {
        Intent intent = new Intent(context,QuestionPub.class);
        context.startActivity(intent);
    }

    /**
     * 显示搜索界面
     * @param context
     */
    public static void showSearch(Context context)
    {
        Intent intent = new Intent(context, Search.class);
        context.startActivity(intent);
    }
}
