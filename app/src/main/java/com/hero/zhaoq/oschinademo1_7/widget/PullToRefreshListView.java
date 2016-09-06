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
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;

    private TextView tipsTextview;

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
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);

        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.pull_to_refresh_head, null);

        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);

        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

        headContentOriginalTopPadding = headView.getPaddingTop();

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();

        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());
        headView.invalidate();

        //System.out.println("初始高度："+headContentHeight);
        //System.out.println("初始TopPad："+headContentOriginalTopPadding);

        addHeaderView(headView);
        setOnScrollListener(this);
    }


    private boolean isRecored;
    private int startY;
    private int startX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(firstItemIndex == 0 && !isRecored){
                    startY = (int) event.getY();
                    isRecored = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(state != REFRESGINH){
                    if(state == DONE){
                        //当前  抬起什么都不做
                        System.out.println("当前-抬起-ACTION_UP：DONE什么都不做");
                    }else if(state == PULL_To_REFRESH){
                        //下拉刷新
                        state = DONE;
                        changeHeaderViewByState();
                    }else if(state == RELEASE_To_REFRESH){
                        //正在刷新
                        state = REFRESGINH;
                        changeHeaderViewByState();
                        onRefresh();
                    }
                }
                isRecored = false;
                isBack = false;

                break;

            case MotionEvent.ACTION_MOVE:

                int tempY = (int) event.getY();
                if(!isRecored && firstItemIndex ==0){
                    isRecored = true;
                    startY = tempY;
                }
                if(state!=REFRESGINH && isRecored){
                    //可以松开刷新了
                    if(state == RELEASE_To_REFRESH){
                        //往上推  推到屏幕  足够掩盖head的程度   但还没有全部覆盖
                        if((tempY - startY < headContentHeight+20)
                                && (tempY - startY) > 0){
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        }
                        //一下子  推到顶：
                        else if(tempY - startY <=0){
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        //往下拉   或者还没有  上推到  屏幕顶部掩盖head
                        else{
                            //不用   进行特别的操作  只要更新paddingTop的值就可以了
                        }
                    }
                    //还没有到达  显示  松开刷新的时候  DONE或者是PULL_TO_REFRESH状态
                    else if(state == PULL_To_REFRESH){
                        //下拉  到可以进入 RELEASE_TO_REFRESH的状态
                        if(tempY - startY >= headContentHeight+20 &&
                                currentScrollState == SCROLL_STATE_TOUCH_SCROLL){
                            state = RELEASE_To_REFRESH;
                            isBack = true;
                            changeHeaderViewByState();
                            //System.out.println("当前-滑动-PULL_To_REFRESH--》RELEASE_To_REFRESH-由done或者下拉刷新状态转变到松开刷新");
                        }
                        // 上推到顶了
                        else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();
                            //System.out.println("当前-滑动-PULL_To_REFRESH--》DONE-由Done或者下拉刷新状态转变到done状态");
                        }
                    }
                    //done 状态下
                    else if(state == DONE){
                        if (tempY - startY > 0) {
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                            //System.out.println("当前-滑动-DONE--》PULL_To_REFRESH-由done状态转变到下拉刷新状态");
                        }
                    }
                    //更新 headView 的size
                    if(state == PULL_To_REFRESH){
                        int topPadding = (int)((-1 * headContentHeight + (tempY - startY)));
                        headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());
                        headView.invalidate();
                        //System.out.println("当前-下拉刷新PULL_To_REFRESH-TopPad："+topPadding);
                    }
                    //更新headView的paddingTop
                    if(state == RELEASE_To_REFRESH){
                        int topPadding = (int)((tempY - startY - headContentHeight));
                        headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(), headView.getPaddingBottom());
                        headView.invalidate();
                        //System.out.println("当前-释放刷新RELEASE_To_REFRESH-TopPad："+topPadding);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
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
    //有参的
    public void onRefreshComplete(String update) {
        lastUpdatedTextView.setText(update);
        onRefreshComplete();
    }

    public OnRefreshListener refreshListener;
    private void onRefresh(){
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }


    private boolean isBack;
    private int headContentOriginalTopPadding;
    /**
     * 完成  刷新后   改变状态    更新界面
     */
    private void changeHeaderViewByState() {
        switch (state){
            case RELEASE_To_REFRESH:

                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText(R.string.pull_to_refresh_release_label);

                break;
            case PULL_To_REFRESH:

                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);

                if(isBack){
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                }
                tipsTextview.setText(R.string.pull_to_refresh_pull_label); //下拉可以刷新

                break;
            case REFRESGINH:
                headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding,
                        headView.getPaddingRight(), headView.getPaddingBottom());
                headView.invalidate();

                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
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

    //点击刷新
    public void clickRefresh(){
        setSelection(0);
        state = REFRESGINH;
        changeHeaderViewByState();
        onRefresh();
    }

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
