package com.hero.zhaoq.oschinademo1_7.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hero.zhaoq.oschinademo1_7.AppConfig;
import com.hero.zhaoq.oschinademo1_7.AppContext;
import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.adapter.ListViewCommentAdapter;
import com.hero.zhaoq.oschinademo1_7.bean.Comment;
import com.hero.zhaoq.oschinademo1_7.bean.CommentList;
import com.hero.zhaoq.oschinademo1_7.bean.News;
import com.hero.zhaoq.oschinademo1_7.bean.Notice;
import com.hero.zhaoq.oschinademo1_7.common.StringUtils;
import com.hero.zhaoq.oschinademo1_7.common.UIHelper;
import com.hero.zhaoq.oschinademo1_7.widget.BadgeView;
import com.hero.zhaoq.oschinademo1_7.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 显示   资讯列表的  详情信息  传过来  信息 id     重新 请求并获取数据
 */
public class NewsDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        this.initView();//初始化  界面
        this.initData();//网络加载数据
        this.initCommentView();
        this.initCommentData();  //初始化  评论数据
    }

    private View lvComment_footer;

    //初始化界面
    private void initCommentView() {
        lvComment_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvComment_foot_more = (TextView)lvComment_footer.findViewById(R.id.listview_foot_more);
        lvComment_foot_progress = (ProgressBar)lvComment_footer.findViewById(R.id.listview_foot_progress);

        //初始化  adapter
        lvCommentAdapter = new ListViewCommentAdapter(this, lvCommentData, R.layout.comment_list);
        mLvComment = (PullToRefreshListView)findViewById(R.id.comment_list_listview);

        mLvComment.addFooterView(lvComment_footer);//添加底部视图  必须在setAdapter前
        mLvComment.setAdapter(lvCommentAdapter);

        //点击事件
        mLvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        //滚动事件
        mLvComment.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
        //添加  评论的  长按事件
        mLvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        //刷新事件
        mLvComment.setRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
//                loadLvCommentData(curId, curCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });
    }

    private FrameLayout mHeader;
    private LinearLayout mFooter;
    private ImageView mHome;
    private ImageView mFavorite;
    private ImageView mRefresh;
    private TextView mHeadTitle;
    private ProgressBar mProgressbar;
    private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;


    private BadgeView bv_comment;
    private ImageView mDetail,mCommentList,mShare;

    private TextView mTitle,mAuthor,mPubDate,mCommentCount;

    private WebView mWebView;
    private Handler mHandler;
    private News newsDetail;
    private int newsId;

    private ViewSwitcher mFootViewSwitcher;
    private ImageView mFootEditebox;
    private EditText mFootEditer;
    private Button mFootPubcomment;
    private ProgressDialog mProgress;
    private InputMethodManager imm;
    private String tempCommentKey = AppConfig.TEMP_COMMENT;

    private int curId;
    private int curCatalog;
    private int curLvDataState;
    private int curLvPosition;//当前listview选中的item位置

    private int lvSumData;
    private List<Comment> lvCommentData = new ArrayList<Comment>();
    private PullToRefreshListView mLvComment;
    private ListViewCommentAdapter lvCommentAdapter;

    private ProgressBar lvComment_foot_progress;

    private TextView lvComment_foot_more;

    private Handler mCommentHandler;

    //Handler   获取到数据  开始添加
    private void initCommentData()
    {
        curId = newsId;
        curCatalog = CommentList.CATALOG_NEWS;

        mCommentHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                if(msg.what >= 0){
                    CommentList list = (CommentList)msg.obj;
                    Notice notice = list.getNotice();
                    //处理listview数据
                    switch (msg.arg1) {
                        case UIHelper.LISTVIEW_ACTION_INIT:
                        case UIHelper.LISTVIEW_ACTION_REFRESH:
                            lvSumData = msg.what;
                            lvCommentData.clear();//先清除原有数据
                            lvCommentData.addAll(list.getCommentlist());
                            break;
                        case UIHelper.LISTVIEW_ACTION_SCROLL:
                            lvSumData += msg.what;
                            if(lvCommentData.size() > 0){
                                for(Comment com1 : list.getCommentlist()){
                                    boolean b = false;
                                    for(Comment com2 : lvCommentData){
                                        if(com1.getId() == com2.getId() && com1.getAuthorId() == com2.getAuthorId()){
                                            b = true;
                                            break;
                                        }
                                    }
                                    if(!b) lvCommentData.add(com1);
                                }
                            }else{
                                lvCommentData.addAll(list.getCommentlist());
                            }
                            break;
                    }

                    //评论数更新
                    if(newsDetail != null && lvCommentData.size() > newsDetail.getCommentCount()){
                        newsDetail.setCommentCount(lvCommentData.size());
                        bv_comment.setText(lvCommentData.size()+"");
                        bv_comment.show();
                    }

                    if(msg.what < 20){
                        curLvDataState = UIHelper.LISTVIEW_DATA_FULL;
                        lvCommentAdapter.notifyDataSetChanged();
                        lvComment_foot_more.setText("已加载全部");
                    }else if(msg.what == 20){
                        curLvDataState = UIHelper.LISTVIEW_DATA_MORE;
                        lvCommentAdapter.notifyDataSetChanged();
                        lvComment_foot_more.setText("更多");
                    }
                    //发送通知广播
                    if(notice != null){
//                        UIHelper.sendBroadCast(NewsDetail.this, notice);
                    }
                }
                else if(msg.what == -1){
                    //有异常--显示加载出错 & 弹出错误消息
                    curLvDataState = UIHelper.LISTVIEW_DATA_MORE;
                    lvComment_foot_more.setText(R.string.load_error);
                    ((AppException)msg.obj).makeToast(NewsDetail.this);
                }
                if(lvCommentData.size()==0){
                    curLvDataState = UIHelper.LISTVIEW_DATA_EMPTY;
                    lvComment_foot_more.setText(R.string.load_empty);
                }
                lvComment_foot_progress.setVisibility(View.GONE);
                if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
                    mLvComment.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
                    mLvComment.setSelection(0);
                }
            }
        };
        this.loadLvCommentData(curId,curCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_INIT);
    }



    //初始化  视图控件
    private void initView() {
        newsId = getIntent().getIntExtra("news_id",0);

        if(newsId > 0){
            tempCommentKey = AppConfig.TEMP_COMMENT + "_" + CommentList.CATALOG_NEWS + "_" + newsId;
            //查找控件
            mHeader = (FrameLayout)findViewById(R.id.news_detail_header);
            mFooter = (LinearLayout)findViewById(R.id.news_detail_footer);
            mHome = (ImageView)findViewById(R.id.news_detail_home);
            mRefresh = (ImageView)findViewById(R.id.news_detail_refresh);
            mHeadTitle = (TextView)findViewById(R.id.news_detail_head_title);
            mProgressbar = (ProgressBar)findViewById(R.id.news_detail_head_progress);
            mViewSwitcher = (ViewSwitcher)findViewById(R.id.news_detail_viewswitcher);
            mScrollView = (ScrollView)findViewById(R.id.news_detail_scrollview);

            mDetail = (ImageView)findViewById(R.id.news_detail_footbar_detail);
            mCommentList = (ImageView)findViewById(R.id.news_detail_footbar_commentlist);
            mShare = (ImageView)findViewById(R.id.news_detail_footbar_share);
            mFavorite = (ImageView)findViewById(R.id.news_detail_footbar_favorite);

            mTitle = (TextView)findViewById(R.id.news_detail_title);
            mAuthor = (TextView)findViewById(R.id.news_detail_author);
            mPubDate = (TextView)findViewById(R.id.news_detail_date);
            mCommentCount = (TextView)findViewById(R.id.news_detail_commentcount);

            mDetail.setEnabled(false);

            mWebView = (WebView)findViewById(R.id.news_detail_webview);
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDefaultFontSize(15);

            mHome.setOnClickListener(homeClickListener);
//            mFavorite.setOnClickListener(favoriteClickListener);
            mRefresh.setOnClickListener(refreshClickListener);
            mAuthor.setOnClickListener(authorClickListener);
            mShare.setOnClickListener(shareClickListener);
//            mDetail.setOnClickListener(detailClickListener);
//            mCommentList.setOnClickListener(commentlistClickListener);

            bv_comment = new BadgeView(this, mCommentList);
            bv_comment.setBackgroundResource(R.mipmap.widget_count_bg2);
            bv_comment.setIncludeFontPadding(false);
            bv_comment.setGravity(Gravity.CENTER);
            bv_comment.setTextSize(8f);
            bv_comment.setTextColor(Color.WHITE);

            imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

            mFootViewSwitcher = (ViewSwitcher)findViewById(R.id.news_detail_foot_viewswitcher);
            mFootPubcomment = (Button)findViewById(R.id.news_detail_foot_pubcomment);
//            mFootPubcomment.setOnClickListener(commentpubClickListener);  //点击事件
            mFootEditebox = (ImageView)findViewById(R.id.news_detail_footbar_editebox);

            mFootEditebox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFootViewSwitcher.showNext();
                    mFootEditer.setVisibility(View.VISIBLE);
                    mFootEditer.requestFocus();
                    mFootEditer.requestFocusFromTouch();
                }
            });
            mFootEditer = (EditText)findViewById(R.id.news_detail_foot_editer);
            //焦点改变事件：
//            mFootEditer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus){
//                        imm.showSoftInput(v, 0);
//                    }
//                    else{
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    }
//                }
//            });
            //点击事件
            mFootEditer.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(mFootViewSwitcher.getDisplayedChild()==1){
                            mFootViewSwitcher.setDisplayedChild(0);
                            mFootEditer.clearFocus();
                            mFootEditer.setVisibility(View.GONE);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        //编辑器添加文本监听
        mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(this, tempCommentKey));
        //显示临时编辑内容
        UIHelper.showTempEditContent(this, mFootEditer, tempCommentKey);

    }

    //点击事件
    private View.OnClickListener homeClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //回到 home
            UIHelper.showHome(NewsDetail.this);
        }
    };


    private View.OnClickListener refreshClickListener = new View.OnClickListener() {
        public void onClick(View v) {
//            initData(newsId, true);
            //加载评论数据
            loadLvCommentData(curId,curCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
        }
    };



    /**
     * 线程加载评论数据
     * @param id 当前文章id
     * @param catalog 分类
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
    private void loadLvCommentData(final int id,final int catalog,final int pageIndex,final Handler handler,final int action){
        new Thread(){
            public void run() {
                //子线程  发送数据
                Message msg = new Message();
                boolean isRefresh = false;
                if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
                try {
                    //获取   评论数据
                    CommentList commentlist = ((AppContext)getApplication()).getCommentList(catalog, id, pageIndex, isRefresh);
                    msg.what = commentlist.getPageSize();
                    msg.obj = commentlist;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.arg1 = action;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }

    private View.OnClickListener authorClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //展示  用户动态
            UIHelper.showUserCenter(v.getContext(), newsDetail.getAuthorId(), newsDetail.getAuthor());
        }
    };

    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(newsDetail == null){
                UIHelper.ToastMessage(v.getContext(),"读取信息失败");
                return;
            }
            UIHelper.ToastMessage(v.getContext(),"分享暂时 未实现");
            //分享到   显示 分享  界面
//            UIHelper.showShareDialog(NewsDetail.this, newsDetail.getTitle(), newsDetail.getUrl());
        }
    };


    private final static int VIEWSWITCH_TYPE_DETAIL = 0x001; //详情
    private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002; //评论

    private View.OnClickListener commentlistClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(newsId == 0){
                return;
            }
            //切换到评论   切换  底部兰
            viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
        }
    };

    /**
     * 底部栏切换
     * @param type
     */
    private void viewSwitch(int type) {
        switch (type) {
            case VIEWSWITCH_TYPE_DETAIL:
                mDetail.setEnabled(false);
                mCommentList.setEnabled(true);
                mHeadTitle.setText(R.string.news_detail_head_title);
                mViewSwitcher.setDisplayedChild(0);
                break;
            case VIEWSWITCH_TYPE_COMMENTS:
                mDetail.setEnabled(true);
                mCommentList.setEnabled(false);
                mHeadTitle.setText("评论");
                mViewSwitcher.setDisplayedChild(1);
                break;
        }
    }

    private final static int DATA_LOAD_ING = 0x001;  //正在加载
    private final static int DATA_LOAD_COMPLETE = 0x002; //加载完成
    private final static int DATA_LOAD_FAIL = 0x003; // 加载失败

    //初始化控件数据
    private void initData()
    {
        mHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                if(msg.what == 1)
                {
                    headButtonSwitch(DATA_LOAD_COMPLETE); //头部按钮的  显示

                    mTitle.setText(newsDetail.getTitle());
                    mAuthor.setText(newsDetail.getAuthor());
                    mPubDate.setText(StringUtils.friendly_time(newsDetail.getPubDate()));
                    mCommentCount.setText(String.valueOf(newsDetail.getCommentCount()));

                    //是否收藏
                    if(newsDetail.getFavorite() == 1)
                        mFavorite.setImageResource(R.drawable.widget_bar_favorite2);
                    else
                        mFavorite.setImageResource(R.drawable.widget_bar_favorite);

                    //显示评论数
                    if(newsDetail.getCommentCount() > 0){
                        bv_comment.setText(newsDetail.getCommentCount()+"");
                        bv_comment.show();
                    }else{
                        bv_comment.setText("");
                        bv_comment.hide();
                    }

                    //网页样式
                    String body = UIHelper.WEB_STYLE + newsDetail.getBody();
                    //读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
                    boolean isLoadImage;
                    AppContext ac = (AppContext)getApplication();
                    if(AppContext.NETTYPE_WIFI == ac.getNetworkType()){
                        isLoadImage = true;
                    }else{
                        isLoadImage = ac.isLoadImage();
                    }
                    if(isLoadImage){
                        //过滤掉 img标签的width,height属性
                        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
                        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
                    }else{
                        //过滤掉 img标签
                        body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
                    }

                    //更多关于***软件的信息
                    String softwareName = newsDetail.getSoftwareName();
                    String softwareLink = newsDetail.getSoftwareLink();
                    if(!StringUtils.isEmpty(softwareName) && !StringUtils.isEmpty(softwareLink))
                        body += String.format("<div id='oschina_software' style='margin-top:8px;color:#FF0000;font-weight:bold'>更多关于:&nbsp;<a href='%s'>%s</a>&nbsp;的详细信息</div>", softwareLink, softwareName);

                    //相关新闻
                    if(newsDetail.getRelatives().size() > 0)
                    {
                        String strRelative = "";
                        for(News.Relative relative : newsDetail.getRelatives()){
                            strRelative += String.format("<a href='%s' style='text-decoration:none'>%s</a><p/>", relative.url, relative.title);
                        }
                        body += String.format("<p/><hr/><b>相关资讯</b><div><p/>%s</div>", strRelative);
                    }

                    body += "<div style='margin-bottom: 80px'/>";

                    mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
                    mWebView.setWebViewClient(new WebViewClient());//UIHelper.getWebViewClient()

                    //发送通知广播
                    if(msg.obj != null){
//                        UIHelper.sendBroadCast(NewsDetail.this, (Notice)msg.obj);
                    }
                }
                else if(msg.what == 0)
                {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    UIHelper.ToastMessage(NewsDetail.this, "读取失败，可能已被删除");
                }
                else if(msg.what == -1 && msg.obj != null)
                {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    ((AppException)msg.obj).makeToast(NewsDetail.this);
                }
            }
        };
        initData(newsId, false); //加载  数据
    }

    private void initData(final int news_id, final boolean isRefresh)
    {
        headButtonSwitch(DATA_LOAD_ING);

        new Thread(){
            public void run() {
                Message msg = new Message();
                try {
                    Log.i("info","----");
                    newsDetail = ((AppContext)getApplication()).getNews(news_id, isRefresh);
                    msg.what = (newsDetail!=null && newsDetail.getId()>0) ? 1 : 0;
                    msg.obj = (newsDetail!=null) ? newsDetail.getNotice() : null;//通知信息
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) {
        switch (type) {
            case DATA_LOAD_ING:
                mScrollView.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.VISIBLE);
                mRefresh.setVisibility(View.GONE);
                break;
            case DATA_LOAD_COMPLETE:
                mScrollView.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_FAIL:
                mScrollView.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                break;
        }
    }

}
