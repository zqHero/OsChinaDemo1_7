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

    //请求 标识和类型：
    public final static int LISTVIEW_ACTION_INIT = 0x01;
    public final static int LISTVIEW_ACTION_REFRESH = 0x02;
    public final static int LISTVIEW_ACTION_SCROLL = 0x03;
    public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

    public final static int LISTVIEW_DATA_MORE = 0x01;
    public final static int LISTVIEW_DATA_LOADING = 0x02;
    public final static int LISTVIEW_DATA_FULL = 0x03;
    public final static int LISTVIEW_DATA_EMPTY = 0x04;

    public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
    public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
    public final static int LISTVIEW_DATATYPE_POST = 0x03;
    public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
    public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
    public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
    public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;


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
