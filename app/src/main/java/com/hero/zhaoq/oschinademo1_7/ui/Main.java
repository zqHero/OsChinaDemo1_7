package com.hero.zhaoq.oschinademo1_7.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.adapter.ListViewBlogAdapter;
import com.hero.zhaoq.oschinademo1_7.adapter.ListViewNewsAdapter;
import com.hero.zhaoq.oschinademo1_7.bean.Blog;
import com.hero.zhaoq.oschinademo1_7.bean.BlogList;
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

    //初始化  各个界面导航栏的  单击事件：
    private Button  framebtn_News_lastest,framebtn_News_blog,framebtn_News_recommend,framebtn_Question_ask,
            framebtn_Question_share,framebtn_Question_other,framebtn_Question_job,framebtn_Question_site,
            framebtn_Tweet_lastest,framebtn_Tweet_hot,framebtn_Tweet_my,framebtn_Active_lastest,framebtn_Active_atme,
            framebtn_Active_comment,framebtn_Active_myself,framebtn_Active_message;

    private Handler lvNewsHandler; //处理    新闻  消息
    private Handler lvBlogHandler; //处理  博客消息

    //PullToRefresh控件
    private PullToRefreshListView lvNews,lvBlog,lvQuestion,lvTweet,lvActive,lvMsg; //控件
    private List<News> lvNewsData = new ArrayList<News>();  //请求新闻信息
    private List<Blog> lvBlogData = new ArrayList<Blog>();

    private AppContext appContext;//

    private ListViewNewsAdapter lvNewsAdapter;//适配器
    private ListViewBlogAdapter lvBlogAdapter;

    private int curNewsCatalog = NewsList.CATALOG_ALL; //获取 所有资讯信息

    private View lvNews_footer,lvBlog_footer,lvQuestion_footer,lvTweet_footer,lvActive_footer,lvMsg_footer;//下拉刷新的布局

    private TextView lvNews_foot_more,lvBlog_foot_more,lvQuestion_foot_more,lvTweet_foot_more,lvActive_foot_more,lvMsg_foot_more;  // 下拉时布局
    private ProgressBar lvNews_foot_progress,lvBlog_foot_progress,lvQuestion_foot_progress,lvTweet_foot_progress,lvActive_foot_progress,lvMsg_foot_progress; //进度条布局

    private int lvNewsSumData,lvBlogSumData,lvQuestionSumData,lvTweetSumData
            ,lvActiveSumData,lvMsgSumData; //  获取到的  数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        appContext = (AppContext) getApplication();

        initFootBar();//初始化底部  RadioButton
        initHeadView();//初始化   头部视图
        initFrameButton();//初始化  各个界面按钮的  单机事件
        initFrameListView();//初始化  listView  显示数据  最新新闻  最新博客

        initPageScroll(); //初始化 底部点击事件   切换界面  获取  数据等

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
                //最后一个  模块  TODO  最后一个模块的点击事件

            }
        });
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

    /**
     * 初始化   各个按钮的  单击事件
     */
    private void initFrameButton() {
        //初始化按钮控件
        framebtn_News_lastest = (Button)findViewById(R.id.frame_btn_news_lastest); //最新  资讯
        framebtn_News_blog = (Button)findViewById(R.id.frame_btn_news_blog);//最新博客
        framebtn_News_recommend = (Button)findViewById(R.id.frame_btn_news_recommend);//  推荐阅读

        framebtn_Question_ask = (Button)findViewById(R.id.frame_btn_question_ask);//问答
        framebtn_Question_share = (Button)findViewById(R.id.frame_btn_question_share);//分享
        framebtn_Question_other = (Button)findViewById(R.id.frame_btn_question_other);//综合
        framebtn_Question_job = (Button)findViewById(R.id.frame_btn_question_job); //职业
        framebtn_Question_site = (Button)findViewById(R.id.frame_btn_question_site); //站务

        framebtn_Tweet_lastest = (Button)findViewById(R.id.frame_btn_tweet_lastest);//最新  动弹
        framebtn_Tweet_hot = (Button)findViewById(R.id.frame_btn_tweet_hot);//热门动弹
        framebtn_Tweet_my = (Button)findViewById(R.id.frame_btn_tweet_my); ///我的  动弹
        //
        framebtn_Active_lastest = (Button)findViewById(R.id.frame_btn_active_lastest);//最新  动态
        framebtn_Active_atme = (Button)findViewById(R.id.frame_btn_active_atme);//我
        framebtn_Active_comment = (Button)findViewById(R.id.frame_btn_active_comment);//评论
        framebtn_Active_myself = (Button)findViewById(R.id.frame_btn_active_myself);//我自己
        framebtn_Active_message = (Button)findViewById(R.id.frame_btn_active_message);//留言

        //设置首选择项
        framebtn_News_lastest.setEnabled(false);
        framebtn_Question_ask.setEnabled(false);
        framebtn_Tweet_lastest.setEnabled(false);
        framebtn_Active_lastest.setEnabled(false);

        //资讯+博客  点击  事件  并请求数据
        framebtn_News_lastest.setOnClickListener(frameNewsBtnClick(framebtn_News_lastest,NewsList.CATALOG_ALL));
        framebtn_News_blog.setOnClickListener(frameNewsBtnClick(framebtn_News_blog,BlogList.CATALOG_LATEST));
        framebtn_News_recommend.setOnClickListener(frameNewsBtnClick(framebtn_News_recommend,BlogList.CATALOG_RECOMMEND));

//        //问答
//        framebtn_Question_ask.setOnClickListener(frameQuestionBtnClick(framebtn_Question_ask,PostList.CATALOG_ASK));
//        framebtn_Question_share.setOnClickListener(frameQuestionBtnClick(framebtn_Question_share,PostList.CATALOG_SHARE));
//        framebtn_Question_other.setOnClickListener(frameQuestionBtnClick(framebtn_Question_other,PostList.CATALOG_OTHER));
//        framebtn_Question_job.setOnClickListener(frameQuestionBtnClick(framebtn_Question_job,PostList.CATALOG_JOB));
//        framebtn_Question_site.setOnClickListener(frameQuestionBtnClick(framebtn_Question_site,PostList.CATALOG_SITE));
//
//        //动弹
//        framebtn_Tweet_lastest.setOnClickListener(frameTweetBtnClick(framebtn_Tweet_lastest,TweetList.CATALOG_LASTEST));
//        framebtn_Tweet_hot.setOnClickListener(frameTweetBtnClick(framebtn_Tweet_hot,TweetList.CATALOG_HOT));


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
            mButtons[i].setTag(i); //设置当前标签
            mButtons[i].setChecked(false); //设置为 不可以再点击

            //跳转界面
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();//获取  标签  当前点击的
                    //点击刷新界面：
                    if(mCurSel == pos){ //当前选中的  当前位置
                        switch(pos){
                            case 0: //咨讯+博客
                                if(lvNews.getVisibility()==View.VISIBLE){
                                    lvNews.clickRefresh();
                                }else{
                                    lvBlog.clickRefresh();
                                }
                                break;
                            case 1: // 问答
                                lvQuestion.clickRefresh();
                                break;
                            case 2: //动弹
                                lvTweet.clickRefresh();
                                break;
                            case 3: //动态+留言
                                if(lvActive.getVisibility() == View.VISIBLE)
                                    lvActive.clickRefresh();
                                else
                                    lvMsg.clickRefresh();
                                break;
                        }
                    }
                    mScrollLayout.snapToScreen(pos);
                }
            });
        }
        //设置  处理  跳转后效果
        mCurSel = 0;
        mButtons[mCurSel].setChecked(true);  //当前  第一个被选中
        //TODO  加载  数据
        mScrollLayout.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener(){
            @Override
            public void OnViewChange(int viewIndex) {
                //切换  列表视图-如果列表数据为空：加载数据
                switch(viewIndex){
                    case 0://咨讯
                        if(lvNews.getVisibility() == View.VISIBLE) {
                            if(lvNewsData.isEmpty()) {
                                //请求  最新资讯
                                loadLvNewsData(curNewsCatalog, 0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_INIT);
                            }
                        } else {
                            //最新博客   数据
                            if(lvBlogData.isEmpty()) {
                                loadLvBlogData(curNewsCatalog, 0, lvBlogHandler, UIHelper.LISTVIEW_ACTION_INIT);
                            }
                        }
                        break;
                    case 1://问答
                        break;
                    case 2://动弹
                        break;
                    case 3://动态
                        //判断登录
                        break;
                }
                setCurPoint(viewIndex);//改变底部 栏的焦点状态
            }
        });
    }


    /**
     * 改变   底部状态栏的  焦点效果
     * @param index
     */
    private void setCurPoint(int index) {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index)
            return;
        mButtons[mCurSel].setChecked(false);  //当前选中
        mButtons[index].setChecked(true);
        mHeadTitle.setText(mHeadTitles[index]);
        mCurSel = index;

        mHead_search.setVisibility(View.GONE);
        mHeadPub_post.setVisibility(View.GONE);
        mHeadPub_tweet.setVisibility(View.GONE);

        switch(index){
            case 0://新闻
                mHeadLogo.setImageResource(R.drawable.frame_logo_news);
                mHead_search.setVisibility(View.VISIBLE);
                break;
            case 1://问答
                mHeadLogo.setImageResource(R.drawable.frame_logo_post);
                mHeadPub_post.setVisibility(View.VISIBLE);
                break;
            case 2://动弹
                mHeadLogo.setImageResource(R.drawable.frame_logo_tweet);
                mHeadPub_tweet.setVisibility(View.VISIBLE);
                break;
            case 3://我的空间
                mHeadLogo.setImageResource(R.drawable.frame_logo_active);
                mHeadPub_tweet.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 初始化 listView
     */
    private void initFrameListView(){
        //初始化  最新咨询   最新博客
        this.initNewsListView();
        //初始化   最新博客
        this.initBlogListView();
//        this.initQuestionListView();
//        this.initTeeetListView();
//        this.initActivityListView();
//        this.initMsgListView();

        //加载数据   加载所有    请求数据信息
        //设置
        lvNewsHandler = getLvHandler(lvNews, lvNewsAdapter, lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);
        lvBlogHandler = getLvHandler(lvBlog, lvBlogAdapter, lvBlog_foot_more, lvBlog_foot_progress, AppContext.PAGE_SIZE);

        //加载 咨询数据
        if(lvNewsData.isEmpty()){
            loadLvNewsData(curNewsCatalog,0,lvNewsHandler,UIHelper.LISTVIEW_ACTION_INIT);
        }
    }

    /**
     * 初始化  最新咨询界面
     */
    private void initNewsListView() {
        lvNewsAdapter = new ListViewNewsAdapter(this,lvNewsData,R.layout.news_listitem);
        lvNews_footer = getLayoutInflater().inflate(R.layout.listview_footer,null); //下拉  加载中布局
        lvNews_foot_more = (TextView) lvNews_footer.findViewById(R.id.listview_foot_more);
        lvNews_foot_progress = (ProgressBar) lvNews_footer.findViewById(R.id.listview_foot_progress);
        lvNews = (PullToRefreshListView) findViewById(R.id.frame_listview_news);
        lvNews.addFooterView(lvNews_footer);//添加底部视图  必须在setAdapter前
        lvNews.setAdapter(lvNewsAdapter);
        //添加  item 事件
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击头部、底部栏无效
                if(position == 0 || view == lvNews_footer) return;

                News news = null;
                //判断是否是TextView
                if(view instanceof TextView){
                    news = (News)view.getTag();
                }else{
                    TextView tv = (TextView)view.findViewById(R.id.news_listitem_title);
                    news = (News)tv.getTag();
                }
                if(news == null) return;

                //跳转到新闻详情  页   显示新闻详情
                UIHelper.showNewsRedirect(view.getContext(), news);
            }
        });
    }

    /**
     * 初始化  最新博客界面
     */
    private void initBlogListView() {
        //初始化  适配器   最新博客
        lvBlogAdapter = new ListViewBlogAdapter(this, BlogList.CATALOG_LATEST, lvBlogData, R.layout.blog_listitem);
        lvBlog_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);  //获取  布局
        lvBlog_foot_more = (TextView)lvBlog_footer.findViewById(R.id.listview_foot_more); //获取  加载中...
        lvBlog_foot_progress =(ProgressBar)lvBlog_footer.findViewById(R.id.listview_foot_progress);
        lvBlog = (PullToRefreshListView) findViewById(R.id.frame_listview_blog);
        lvBlog.addFooterView(lvBlog_footer);//添加底部视图  必须在setAdapter前
        lvBlog.setAdapter(lvBlogAdapter);

        //为每一项  添加点击事件
        lvBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        //添加  滚动时间
        lvBlog.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
        //下拉刷新  事件
        lvBlog.setRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    /**
     * 加载  最新  资讯  数据信息
     * @param catalog  类别标识
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action  动作标识
     */
    private void loadLvNewsData(final int catalog,final int pageIndex,final Handler handler,
                                final int action) {
        mHeadProgress.setVisibility(ProgressBar.VISIBLE);
        new Thread(){
            public void run(){
                Message msg = new Message();
                boolean isRefresh = false;
                //加载数据   初始化  和滚动都要刷新数据：
                if(action == UIHelper.LISTVIEW_ACTION_REFRESH
                        || action == UIHelper.LISTVIEW_ACTION_SCROLL){
                    isRefresh = true;
                }
                try {
                    //加载  数据信息
                    //catalog: 1    pageIndex: 0     isRefresh:true
                    NewsList list = appContext.getNewsList(catalog, pageIndex, isRefresh);
                    Log.i("list----------",list.toString());
                    msg.what = list.getPageSize();
                    msg.obj = list;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.arg1 = action;
                msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
                if(curNewsCatalog == catalog)
                    handler.sendMessage(msg);
            }
        }.start();
    }
    /**
     * 加载 最新博客界面的数据
     */
    private void loadLvBlogData(final int catalog,final int pageIndex,final Handler handler,final int action) {
        mHeadProgress.setVisibility(ProgressBar.VISIBLE);  //进度条可见
        new Thread(){
            public void run() {
                Message msg = new Message();
                boolean isRefresh = false;
                if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
                String type = "";
                switch (catalog) {
                    case BlogList.CATALOG_LATEST:
                        type = BlogList.TYPE_LATEST;  //最新 博客
                        break;
                    case BlogList.CATALOG_RECOMMEND:
                        type = BlogList.TYPE_RECOMMEND; //推荐阅读
                        break;
                }
                try {
                    //请求    信息
                    BlogList list = appContext.getBlogList(type, pageIndex, isRefresh);
                    msg.what = list.getPageSize();
                    msg.obj = list;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.arg1 = action;
                msg.arg2 = UIHelper.LISTVIEW_DATATYPE_BLOG;
                if(curNewsCatalog == catalog)
                    handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 获取  listView 的初始化   handler 用于初始化数据
     */
    private Handler getLvHandler(final PullToRefreshListView lv, final BaseAdapter adapter,
                                 final TextView more,final ProgressBar progress,final int pageSize) {
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what>=0){
                    //listView 数据处理：   最新博客  和最新资讯
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
                    case UIHelper.LISTVIEW_DATATYPE_BLOG://处理博客信息
                        BlogList blist = (BlogList) obj;
                        notice = blist.getNotice();
                        lvBlogSumData = what;
                        if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
                            if(lvBlogData.size() > 0){
                                for(Blog blog1 : blist.getBloglist()){
                                    boolean b = false;
                                    for(Blog blog2 : lvBlogData){
                                        if(blog1.getId() == blog2.getId()){
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
                        lvBlogData.clear();//先清除原有数据
                        lvBlogData.addAll(blist.getBloglist());
                        Log.i("info","----------"+lvBlogData.toString()+"--------");
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

    //初始化  最新资讯页面的  单击事件
    private View.OnClickListener frameNewsBtnClick(final Button btn,final int catalog){
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(btn == framebtn_News_lastest){
                    framebtn_News_lastest.setEnabled(false);
                }else{
                    framebtn_News_lastest.setEnabled(true);
                }
                if(btn == framebtn_News_blog){
                    framebtn_News_blog.setEnabled(false);
                }else{
                    framebtn_News_blog.setEnabled(true);
                }
                if(btn == framebtn_News_recommend){
                    framebtn_News_recommend.setEnabled(false);
                }else{
                    framebtn_News_recommend.setEnabled(true);
                }

                curNewsCatalog = catalog;

                //非新闻列表
                if(btn == framebtn_News_lastest)  //最新  资讯
                {
                    lvNews.setVisibility(View.VISIBLE);
                    lvBlog.setVisibility(View.GONE);

                    loadLvNewsData(curNewsCatalog, 0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
                }
                else
                {
                    lvNews.setVisibility(View.GONE);
                    lvBlog.setVisibility(View.VISIBLE);

                    loadLvBlogData(curNewsCatalog, 0, lvBlogHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
                }
            }
        };
    }

    //初始化  问题界面的    点击事件  初始化
    public View.OnClickListener frameQuestionBtnClick(final Button btn, final int catalog){
        return new View.OnClickListener() {
            public void onClick(View v) {
                if (btn == framebtn_Question_ask)
                    framebtn_Question_ask.setEnabled(false);
                else
                    framebtn_Question_ask.setEnabled(true);
                if (btn == framebtn_Question_share)
                    framebtn_Question_share.setEnabled(false);
                else
                    framebtn_Question_share.setEnabled(true);
                if (btn == framebtn_Question_other)
                    framebtn_Question_other.setEnabled(false);
                else
                    framebtn_Question_other.setEnabled(true);
                if (btn == framebtn_Question_job)
                    framebtn_Question_job.setEnabled(false);
                else
                    framebtn_Question_job.setEnabled(true);
                if (btn == framebtn_Question_site)
                    framebtn_Question_site.setEnabled(false);
                else
                    framebtn_Question_site.setEnabled(true);

//                curQuestionCatalog = catalog;
//                loadLvQuestionData(curQuestionCatalog, 0, lvQuestionHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
            }
        };
    }

    //初始化  动弹界面的    点击事件  初始化
    private View.OnClickListener frameTweetBtnClick(final Button btn,final int catalog){
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(btn == framebtn_Tweet_lastest)
                    framebtn_Tweet_lastest.setEnabled(false);
                else
                    framebtn_Tweet_lastest.setEnabled(true);
                if(btn == framebtn_Tweet_hot)
                    framebtn_Tweet_hot.setEnabled(false);
                else
                    framebtn_Tweet_hot.setEnabled(true);
                if(btn == framebtn_Tweet_my)
                    framebtn_Tweet_my.setEnabled(false);
                else
                    framebtn_Tweet_my.setEnabled(true);

//                curTweetCatalog = catalog;
//                loadLvTweetData(curTweetCatalog, 0, lvTweetHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
            }
        };
    }

    //初始化数据
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
        mScrollLayout.setIsScroll(false);//appContext.isScroll  设置  可以滑动
    }
}
