package com.hero.zhaoq.oschinademo1_7.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.hero.zhaoq.oschinademo1_7.AppManager;
import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.ui.Main;
import com.hero.zhaoq.oschinademo1_7.ui.QuestionPub;
import com.hero.zhaoq.oschinademo1_7.ui.Search;
import com.hero.zhaoq.oschinademo1_7.ui.TweetPub;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.common
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  21:04
 * ui帮助类 告诉崩溃信息  显示界面
 */
public class UIHelper {

    //请求 标识和类型：
    public final static int LISTVIEW_ACTION_INIT = 0x01;  //初始化 信息的数据标识
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

    /**
     * 发送App异常崩溃报告
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final Context cont, final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);  //请求信息有误
        builder.setMessage(R.string.app_error_message);//请求信息有误
        builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //发送异常报告
                Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain"); //模拟器
                i.setType("message/rfc822") ; //真机
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jxsmallmouse@163.com"});
                i.putExtra(Intent.EXTRA_SUBJECT,"开源中国Android客户端 - 错误报告");
                i.putExtra(Intent.EXTRA_TEXT,crashReport);
                cont.startActivity(Intent.createChooser(i, "发送错误报告"));
                //退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.show();
    }


    /**
     * 弹出Toast消息
     * @param msg
     */
    public static void ToastMessage(Context cont,String msg)
    {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * 显示    登录页面    Dialog
//     * @param activity
//     */
//    public static void showLoginDialog(Context context)
//    {
//        Intent intent = new Intent(context,LoginDialog.class);
//        if(context instanceof Main)
//            intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
//        else if(context instanceof Setting)
//            intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
//        else
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
}
