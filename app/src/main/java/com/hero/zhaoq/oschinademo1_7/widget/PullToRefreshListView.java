package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.widget
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  23:19
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private final static String TAG = "PullToRefreshListView";
    //下拉 刷新标志：
    private final static int PULL_To_REFRESH = 0;
    //松开刷新标志：
    private final static int RELEASE_To_REFRESH = 1;
    //正在刷新 标志：
    private final static int REFRESGINH = 2;
    //刷新  完成标志
    private final static int DONE = 3;

    private LayoutInflater inflater;

    private LinearLayout headView;
    private TextView tipsTextView;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    //



    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);//用于在代码中初始化  数据
    }
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);//用于  在xml文件中初始化布局
    }

    //初始化  布局
    private void init(Context context) {
        //设置滑动效果：


    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
