package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hero.zhaoq.oschinademo1_7.R;

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
    //用来设置  箭头图标动画效果
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
    private int headContentWidth;
    private int headContentHeight;
    //初始化  布局
    private void init(Context context) {
        //Interpolator用于动画中的时间插值，其作用就是把0到1的浮点值变化映射到另一个浮点值变化。
        //设置滑动效果：
        animation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setInterpolator(new LinearInterpolator());  //设置  差值器
        animation.setDuration(100);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180,0,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);

        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.pull_to_refresh_head,null);

        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumHeight(50);
        arrowImageView.setMinimumWidth(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextView = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(headView);
        headContentHeight =  headView.getMeasuredHeight();  //获得  测量的高度
        headContentWidth = headView.getMeasuredWidth(); //获得  测量的宽度

        headView.setPadding(headView.getPaddingLeft(),-1 * headContentHeight,
                headView.getPaddingRight(),headView.getPaddingBottom());
        headView.invalidate();

        //添加   进头视图中
        addHeaderView(headView);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
