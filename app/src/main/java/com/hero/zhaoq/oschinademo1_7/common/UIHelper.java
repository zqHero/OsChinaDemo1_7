package com.hero.zhaoq.oschinademo1_7.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.hero.zhaoq.oschinademo1_7.AppConfig;
import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppManager;
import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.adapter.GridViewFaceAdapter;
import com.hero.zhaoq.oschinademo1_7.bean.News;
import com.hero.zhaoq.oschinademo1_7.bean.URLs;
import com.hero.zhaoq.oschinademo1_7.bean.UserCenter;
import com.hero.zhaoq.oschinademo1_7.ui.Main;
import com.hero.zhaoq.oschinademo1_7.ui.NewsDetail;
import com.hero.zhaoq.oschinademo1_7.ui.QuestionPub;
import com.hero.zhaoq.oschinademo1_7.ui.Search;
import com.hero.zhaoq.oschinademo1_7.ui.TweetPub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /** 全局web样式 */
    public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
            "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
            "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
            "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
    /**
     * 获取webviewClient对象
     * @return
     */
    public static WebViewClient getWebViewClient(){
        return new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }

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

    /**
     * 条转到   资讯详情页
     * @param context
     * @param news
     */
    public static void showNewsRedirect(Context context, News news) {
        String url = news.getUrl();
            if(StringUtils.isEmpty(url)){
                int newsId = news.getId();
                int newsType = news.getNewType().type;
                String objId = news.getNewType().attachment;
                switch (newsType) {
                    case News.NEWSTYPE_NEWS:
                        showNewsDetail(context, newsId);
                        break;
                    case News.NEWSTYPE_SOFTWARE:
                        break;
                    case News.NEWSTYPE_POST:
                        break;
                    case News.NEWSTYPE_BLOG:
                        break;
                }
            } else {
                showUrlRedirect(context, url);
            }
    }


    /**
     * url跳转
     * @param context
     * @param url
     */
    public static void showUrlRedirect(Context context, String url){
        URLs urls = URLs.parseURL(url);
        if(urls != null){
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey());
        }else{
            openBrowser(context, url);
        }
    }

    /**
     * 打开浏览器
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url){
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"无法显示网页",Toast.LENGTH_LONG).show();
        }
    }

    public static void showLinkRedirect(Context context, int objType, int objId, String objKey){
        switch (objType) {
            case URLs.URL_OBJ_TYPE_NEWS:
                showNewsDetail(context, objId);
                break;
            case URLs.URL_OBJ_TYPE_QUESTION:
                break;
            case URLs.URL_OBJ_TYPE_QUESTION_TAG:
                break;
            case URLs.URL_OBJ_TYPE_SOFTWARE:
                break;
            case URLs.URL_OBJ_TYPE_ZONE:
                break;
            case URLs.URL_OBJ_TYPE_TWEET:
                break;
            case URLs.URL_OBJ_TYPE_BLOG:
                break;
            case URLs.URL_OBJ_TYPE_OTHER:
                break;
        }
    }

    /**
     * 获取TextWatcher对象
     * @param context
     * @return
     */
    public static TextWatcher getTextWatcher(final Activity context, final String temlKey) {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //保存当前EditText正在编辑的内容
                ((AppContext)context.getApplication()).setProperty(temlKey, s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }
    /**
     * 编辑器显示保存的草稿
     * @param context
     * @param editer
     * @param temlKey
     */
    public static void showTempEditContent(Activity context, EditText editer, String temlKey) {
        String tempContent = ((AppContext)context.getApplication()).getProperty(temlKey);
        if(!StringUtils.isEmpty(tempContent)) {
            SpannableStringBuilder builder = parseFaceByText(context, tempContent);
            editer.setText(builder);
            editer.setSelection(tempContent.length());//设置光标位置
        }
    }
    /** 表情图片匹配 */
    private static Pattern facePattern = Pattern.compile("\\[{1}([0-9]\\d*)\\]{1}");

    /**
     * 将[12]之类的字符串替换为表情
     * @param context
     * @param content
     */
    public static SpannableStringBuilder parseFaceByText(Context context, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Matcher matcher = facePattern.matcher(content);
        while (matcher.find()) {
            //使用正则表达式找出其中的数字
            int position = StringUtils.toInt(matcher.group(1));
            int resId = 0;
            try {
                if(position > 65 && position < 102)
                    position = position-1;
                else if(position > 102)
                    position = position-2;
                resId = GridViewFaceAdapter.getImageIds()[position];  //获取   图片的资源id
                Drawable d = context.getResources().getDrawable(resId); //获取   图片
                d.setBounds(0, 0, 35, 35);//设置表情图片的显示大小
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
            }
        }
        return builder;
    }

    /**
     * 显示首页
     * @param activity
     */
    public static void showHome(Activity activity)
    {
        Intent intent = new Intent(activity,Main.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 显示用户动态
     * @param context
     * @param hisuid
     * @param hisname
     */
    public static void showUserCenter(Context context, int hisuid, String hisname)
    {
        Intent intent = new Intent(context, UserCenter.class);
        intent.putExtra("his_id", hisuid);
        intent.putExtra("his_name", hisname);
        context.startActivity(intent);
    }


    /**
     * 显示新闻详情
     * @param context
     * @param newsId
     */
    public static void showNewsDetail(Context context, int newsId)
    {
        Intent intent = new Intent(context, NewsDetail.class);
        intent.putExtra("news_id", newsId);
        context.startActivity(intent);
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


//    /**
//     * 分享到'新浪微博'或'腾讯微博'的对话框
//     * @param context 当前Activity
//     * @param title	分享的标题
//     * @param url 分享的链接
//     */
//    public static void showShareDialog(final Activity context,final String title,final String url)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(android.R.drawable.btn_star);
//        builder.setTitle(context.getString(R.string.share));
//        builder.setItems(R.array.app_share_items,new DialogInterface.OnClickListener(){
//            AppConfig cfgHelper = AppConfig.getAppConfig(context);
//            AccessInfo access = cfgHelper.getAccessInfo();
//            public void onClick(DialogInterface arg0, int arg1) {
//                switch (arg1) {
//                    case 0://新浪微博
//                        //分享的内容
//                        final String shareMessage = title + " " +url;
//                        //初始化微博
//                        if(SinaWeiboHelper.isWeiboNull())
//                        {
//                            SinaWeiboHelper.initWeibo();
//                        }
//                        //判断之前是否登陆过
//                        if(access != null)
//                        {
//                            SinaWeiboHelper.progressDialog = new ProgressDialog(context);
//                            SinaWeiboHelper.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            SinaWeiboHelper.progressDialog.setMessage(context.getString(R.string.sharing));
//                            SinaWeiboHelper.progressDialog.setCancelable(true);
//                            SinaWeiboHelper.progressDialog.show();
//                            new Thread()
//                            {
//                                public void run()
//                                {
//                                    SinaWeiboHelper.setAccessToken(access.getAccessToken(), access.getAccessSecret(), access.getExpiresIn());
//                                    SinaWeiboHelper.shareMessage(context, shareMessage);
//                                }
//                            }.start();
//                        }
//                        else
//                        {
//                            SinaWeiboHelper.authorize(context, shareMessage);
//                        }
//                        break;
//                    case 1://腾讯微博
//                        QQWeiboHelper.shareToQQ(context, title, url);
//                        break;
//                    case 2://更多
//                        showShareMore(context, title, url);
//                        break;
//                }
//            }
//        });
//        builder.create().show();
//    }


}
