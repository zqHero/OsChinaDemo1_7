package com.hero.zhaoq.oschinademo1_7.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.adapter.ListViewNewsAdapter;
import com.hero.zhaoq.oschinademo1_7.bean.News;
import com.hero.zhaoq.oschinademo1_7.bean.NewsList;
import com.hero.zhaoq.oschinademo1_7.bean.Notice;
import com.hero.zhaoq.oschinademo1_7.bean.TweetList;
import com.hero.zhaoq.oschinademo1_7.common.UIHelper;
import com.hero.zhaoq.oschinademo1_7.widget.PullToRefreshListView;
import com.hero.zhaoq.oschinademo1_7.widget.ScrollLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 应用程序首页
 */
public class Main extends Activity {

    //头部组件
    private ImageView mHeadLogo;
    private TextView mHeadTitle;
    private ProgressBar mHeadProgress;
    private ImageButton mHead_search,mHeadPub_post,mHeadPub_tweet;

    //底部组件
    private ScrollLayout mScrollLayout; //中间的  布局
    private RadioButton[] mButtons; //下面的  RadioButton
    private String[] mHeadTitles;//表头显示的内容
    private int mViewCount;//
    private int mCurSel;//当前选中的  radioButton

    //初始化  底部按钮 RadioButton
    private RadioButton fbNews,fbQuestion,fbTweet,fbactive,fbSetting;

    private AppContext appContext;//

    private ListViewNewsAdapter lvNewsAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        appContext = (AppContext) getApplication();

        initFootBar();//初始化底部  RadioButton
        initHeadView();//初始化   头部视图
        initPageScroll(); //初始化 底部点击事件   切换界面
        initFrameListView();//初始化  listView  显示数据  最新新闻  最新博客
    }

    /**
     * 初始化  头部  视图  以及右边部分 按钮点击事件
     */
    private void initHeadView() {
        mHeadLogo = (ImageView) findViewById(R.id.main_head_logo);
        mHeadTitle = (TextView) findViewById(R.id.main_head_title);
        mHeadProgress = (ProgressBar)findViewById(R.id.main_head_progress);

        mHead_search = (ImageButton)findViewById(R.id.main_head_search);
        mHeadPub_post = (ImageButton)findViewById(R.id.main_head_pub_post);
        mHeadPub_tweet = (ImageButton)findViewById(R.id.main_head_pub_tweet);

        mHead_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UIHelper.showSearch(v.getContext());
            }
        });

        mHeadPub_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                UIHelper.showQuestionPub(view.getContext());
            }
        });

        //发布动弹
        mHeadPub_tweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UIHelper.showTweetPub(Main.this);
            }
        });

    }

    //初始化  底部栏
    private void initFootBar() {
        fbNews = (RadioButton) findViewById(R.id.main_footbar_news);
        fbQuestion = (RadioButton) findViewById(R.id.main_footbar_question);
        fbTweet = (RadioButton) findViewById(R.id.main_footbar_tweet);
        fbactive = (RadioButton) findViewById(R.id.main_footbar_active);
        fbSetting = (RadioButton) findViewById(R.id.main_footbar_setting);


        fbSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //最后一个  模块  TODO

            }
        });
    }

    /**
     * 初始化 水平滚动翻页  底部的  点击事件
     */
    private void initPageScroll() {
        mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
        //数组  咨讯   问答  动弹   我的空间  添加进去
        mHeadTitles = getResources().getStringArray(R.array.head_titles);
        mViewCount = mScrollLayout.getChildCount();
        mButtons = new RadioButton[mViewCount]; //创建 底部 按钮的数组

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linearlayout_footer);
        //添加事件  给底部按钮
        for (int i=0;i<mViewCount;i++){
            mButtons[i] = (RadioButton) linearLayout.getChildAt(i*2);  //中间有  竖线组件 需要乘以二
            mButtons[i].setTag(i);
            mButtons[i].setChecked(false); //设置为 不可以再点击

            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();//获取  标签  当前点击的
                    //TODO  未完成 点击事件
                    //点击刷新界面：
                    if(mCurSel == pos){ //当前选中的是  当前位置
                        switch(pos){
                            case 0: //咨讯+博客
                                if(lvNews.getVisibility()==View.VISIBLE){
                                    //TODO  未完成 点击事件
                                }else{
                                    //TODO  未完成 点击事件
                                }
                                break;
                            case 1: // 问答
                                break;
                            case 2: //动弹
                                break;
                            case 3: //动态+留言
                                break;
                        }
                    }
                    mScrollLayout.snapToScreen(pos);
                }
            });
        }

    }


    /**
     * 初始化 listView
     */
    private void initFrameListView(){
        //初始化  最新咨询   最新博客
        this.initNewsListView();

//        this.initBlogListView();
//        this.initQuestionListView();
//        this.initTeeetListView();
//        this.initActivityListView();
//        this.initMsgListView();

        //加载数据   加载所有    请求数据信息
        this.initFrameListViewData();
    }

    private View lvNews_footer;//下拉刷新的布局

    private TextView lvNews_foot_more;
    private ProgressBar lvNews_foot_progress;
    private void initNewsListView() {
        lvNewsAdapter = new ListViewNewsAdapter(this,lvNewsData,R.layout.news_listitem);
        lvNews_footer = getLayoutInflater().inflate(R.layout.listview_footer,null); //下拉  加载中布局
        lvNews_foot_more = (TextView) lvNews_footer.findViewById(R.id.listview_foot_more);
        lvNews_foot_progress = (ProgressBar) lvNews_footer.findViewById(R.id.listview_foot_progress);
        lvNews = (PullToRefreshListView) findViewById(R.id.frame_listview_news);
        lvNews.addFooterView(lvNews_footer);//添加底部视图  必须在setAdapter前
        lvNews.setAdapter(lvNewsAdapter);
        //添加 事件

    }

    private Handler lvNewsHandler;
    /**
     * 加载所有listView  的数据
     */
    private void initFrameListViewData() {
        //
        lvNewsHandler = getLvHandler(lvNews, lvNewsAdapter, lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);
    }

    private PullToRefreshListView lvNews; //控件
    private List<News> lvNewsData = new ArrayList<News>();  //请求新闻信息
    private int lvNewsSumData; //

    /**
     * 获取  listView 的初始化   handler 用于初始化数据
     */
    private Handler getLvHandler(final PullToRefreshListView lv, final ListViewNewsAdapter adapter,
                                 final TextView more,final ProgressBar progress,final int pageSize) {
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what>=0){
                    //listView 数据处理：
                    Notice notice = handleLvData(msg.what,msg.obj,msg.arg2,msg.arg1);
                    if (msg.what < pageSize){
                        lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_full);
                    }else if(msg.what == pageSize){
                        lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_more);

//                        //特殊处理-热门动弹不能翻页
//                        if(lv == lvTweet) {
//                            TweetList tlist = (TweetList)msg.obj;
//                            if(lvTweetData.size() == tlist.getTweetCount()){
//                                lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
//                                more.setText(R.string.load_full);
//                            }
//                        }
                    }
//                    if(notice !=null){//通知 不为null
//                        UIHelper.sendBroadCast(lv.getContext(), notice);
//                    }
//                    //是否清除通知信息
//                    if(isClearNotice){
//                        ClearNotice(CurClearNoticeType);
//                        isCLearNotice = false;//重置
//                        curClearNoticeType = 0;
//                    }
                }
                else if(msg.what == -1){
                    //有异常  -- 显示加载出错  & 弹出错误消息
                    lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                    more.setText(R.string.load_error);
                    ((AppException)msg.obj).makeToast(Main.this);
                }
                if(adapter.getCount() ==0){
                    lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progress.setVisibility(ProgressBar.GONE);
                mHeadProgress.setVisibility(ProgressBar.GONE);
                if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
                    lv.setSelection(0);
                }else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG){
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }
            }
        };
    }

    /**
     * list View 数据处理
     * @param what 数量
     * @param obj 数据
     * @param objtype 操作类型
     * @param actiontype  通知信息
     * @return
     */
    private Notice handleLvData(int what, Object obj, int objtype, int actiontype) {
        Notice notice = null; //返回信息 通知类
        switch(actiontype){
            case UIHelper.LISTVIEW_ACTION_INIT:
            case UIHelper.LISTVIEW_ACTION_REFRESH:
            case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
                int newdata = 0;//新加载数据  只有刷新动作才会使用的到
                switch(objtype){
                    case UIHelper.LISTVIEW_DATATYPE_NEWS://请求 新闻信息
                        NewsList nlist = (NewsList)obj;
                        notice = nlist.getNotice();
                        lvNewsSumData = what;  //
                        if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
                            if(lvNewsData.size() > 0){
                                for(News news1 : nlist.getNewslist()){
                                    boolean b = false;
                                    for(News news2 : lvNewsData){
                                        if(news1.getId() == news2.getId()){
                                            b = true;
                                            break;
                                        }
                                    }
                                    if(!b) newdata++;
                                }
                            }else{
                                newdata = what;
                            }
                        }
                        lvNewsData.clear();//先清除原有数据
                        lvNewsData.addAll(nlist.getNewslist());
                        Log.i("info","----------"+lvNewsData.toString()+"--------");
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_BLOG://请求博客信息
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_POST:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_TWEET:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_ACTIVE:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_MESSAGE:
                        break;
                }
                break;
            case UIHelper.LISTVIEW_ACTION_SCROLL:
                switch(objtype){
                    case UIHelper.LISTVIEW_DATATYPE_NEWS:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_BLOG:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_POST:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_TWEET:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_ACTIVE:
                        break;
                    case UIHelper.LISTVIEW_DATATYPE_MESSAGE:
                        break;
                }
                break;
        }
        return notice;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化  资讯  模块：
        if(mViewCount == 0){
            mViewCount = 4;//记录  五个模块的脚标值
        }
        if(mCurSel ==0 && !fbNews.isChecked()){
            fbNews.setChecked(true);
            fbactive.setChecked(false);
            fbQuestion.setChecked(false);
            fbTweet.setChecked(false);
            fbSetting.setChecked(false);
        }
//        mScrollLayout.setIsScroll(false);//appContext.isScroll  设置  可以滑动
    }


}
