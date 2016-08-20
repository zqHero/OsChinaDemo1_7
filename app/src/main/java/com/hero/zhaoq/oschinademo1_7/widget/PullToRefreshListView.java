package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    //计算   headView 的宽和高
    private void measureView(LinearLayout child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if(p==null){
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0+0,p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if(lpHeight > 0){ //设置测量模式
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        }else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec,childHeightSpec);
    }

    private int state; //刷新完成
    public void onRefreshComplete(){
        state = DONE;
        changeHeaderViewByState();
    }

    public OnRefreshListener refreshListener;
    private void onRefresh(){
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    private TextView tipsTextview;
    private boolean isBack;
    private int headContentOriginalTopPadding;
    /**
     * 完成  刷新后  改变状态   更新界面
     */
    private void changeHeaderViewByState() {
        switch (state){
            case RELEASE_To_REFRESH:

                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText(R.string.pull_to_refresh_release_label);

                break;
            case PULL_To_REFRESH:

                progressBar.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);

                if(isBack){
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                }
                tipsTextView.setText(R.string.pull_to_refresh_pull_label); //下拉可以刷新

                break;
            case REFRESGINH:
                headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding,
                        headView.getPaddingRight(), headView.getPaddingBottom());
                headView.invalidate();

                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextView.setText(R.string.pull_to_refresh_refreshing_label);
                lastUpdatedTextView.setVisibility(View.GONE);

                break;
            case DONE:

                headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight,
                        headView.getPaddingRight(), headView.getPaddingBottom());
                headView.invalidate();

                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                //此处更换图标
                arrowImageView.setImageResource(R.mipmap.ic_pulltorefresh_arrow);

                tipsTextview.setText(R.string.pull_to_refresh_pull_label);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    //记录  当前状态 和索引值
    private int currentScrollState;
    private int firstItemIndex;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        currentScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        firstItemIndex = firstVisiableItem;
    }

    public OnRefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }
}
