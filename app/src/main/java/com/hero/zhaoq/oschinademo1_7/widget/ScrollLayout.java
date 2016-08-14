package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.widget
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/13  22:09
 * 自定义  View布局   左右滑动  切换屏幕
 */
public class ScrollLayout extends ViewGroup{

    Scroller mScroller = null;//  Android里Scroller类是为了实现View平滑滚动的一个Helper类。
    private int mCurScreen;
    private int mDefaultScreen = 0 ;
    private int mTouchSlop;//手指滑动  的距离  大于这个距离  到下一页

    /**
     * 设置    是否  可以滑动
     */
    private boolean isScroll = true;
    public void setIsScroll(boolean b){
        this.isScroll = b;
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        //调用   本地后遭方法   用于  xml文件中创建布局
        this(context, attrs,0);
    }

    //初始化滑动变化时需要的   参数
    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mCurScreen = mDefaultScreen;
        //getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();
    }

    //实现  该方法  onLayout方法是ViewGroup中子View的布局方法，用于放置子View的位置。
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //scrollView  应该  左右滑动布局
        int childleft = 0;
        final int childCount = getChildCount();
        for(int i = 0;i<childCount;i++){ //遍历子View
            final View childView = getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                //该方法是View的放置方法，在View类实现。
                // 调用该方法需要传入放置View的矩形空间左上角left、top值
                // 和右下角right、bottom值。
                // 这四个值是相对于父控件而言的。
                // 例如传入的是（10, 10, 100, 100），layout(int l, int t, int r, int b)
                // 则该View在距离父控件的左上角位置(10, 10)处显示，
                // 显示的大小是宽高是90(参数r,b是相对左上角的)，这有点像绝对布局。
                childView.layout(childleft,0,childleft+childWidth,childView.getMeasuredHeight());
                childleft += childWidth;//一次  向右排列布局
            }
        }
    }
    //设置  测量方法   测量  父控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);//获得测量宽度
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获得  测量模式
        if(widthMode != MeasureSpec.EXACTLY){//确切的测量模式
            throw new IllegalStateException(
                    "ScrollLayout only can run at EXACTLY mode!");
        }
        // The children are given the same width and height as the scrollLayout
        final int count = getChildCount(); //获取  子布局的数量  计算宽度
        for(int i=0;i<count;i++){
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
        scrollTo(mCurScreen*width,0);//scrollTo() 方法可把内容滚动到指定的坐标。
    }

    //VelocityTracker顾名思义即速度跟踪,
    // 主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率
    //用于  判断 我们手指事件的  速率  以及 移动位置
    private VelocityTracker mVelocityTracker;

    private float mLastMotionX,mLastMotionY;//记录  手指坐标

    private static final int SNAP_VELOCITY = 600;//滑动  速率

    private int mTouchState = TOUCH_STATE_REST;
    private static final int TOUCH_STATE_REST = 0;  //当前状态记录

    //手指  触摸  滑动时
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //是否 可以滑动：
        if(!isScroll){ //当前  设置不可滑动  返回false
            return false;
        }
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        //用addMovement(MotionEvent)函数将Motion event加入到VelocityTracker类实例中.
        // 你可以使用getXVelocity() 或getXVelocity()获得横向和竖向的速率到速率时，
        // 但是使用它们之前请先调用computeCurrentVelocity(int)来初始化速率的单位 。
        mVelocityTracker.addMovement(event); //添加  事件实例 进行监听

        float x = event.getX();//获得  x方向移动大小
        float y = event.getY();//获得 y方法移动大小
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(!mScroller.isFinished()){ //滑动没有结束
                    mScroller.abortAnimation();;
                }
                mLastMotionX = x;
                mLastMotionY = y;
                break;

            case MotionEvent.ACTION_MOVE:

                int deltaX = (int)(mLastMotionX - x);  //获取增量  xy的变化值
                int deltaY = (int)(mLastMotionY-y);
                //满足  滑动条件
                if(Math.abs(deltaX)<200 && Math.abs(deltaY)>10){
                    break;
                }
                mLastMotionY = y;
                mLastMotionX = x;
                //scrollTo和scrollBy区别： scrollTo就是把View移动到屏幕的X和Y位置，
                // 也就是绝对位置。而scrollBy其实就是调用的scrollTo，
                // 但是参数是当前mScrollX和mScrollY加上X和Y的位置，
                // 所以ScrollBy调用的是相对于mScrollX和mScrollY的位置。
                scrollBy(deltaX,0);
                break;

            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();  //速率x方向的
                //判断  滑动方向   速率 为负数  则是相反方向
                if(velocityX > SNAP_VELOCITY && mCurScreen >0){
                    snapToScreen(mCurScreen - 1);//切换  到新的屏幕
                }else if(velocityX <-SNAP_VELOCITY
                        && mCurScreen < getChildCount()-1){
                    snapToScreen(mCurScreen + 1);//切换  到新的屏幕
                }else{
                    snapToDestination();
                }
                //放开手后  将速率对象  置为  null
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                mTouchState = TOUCH_STATE_REST;//重置 当前  触摸状态
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;//重置 当前  触摸状态
                break;
        }
        return true;
    }

    //切换到   目标  子布局
    private void snapToDestination() {
    }

    //切换   当前显示的子布局
    private void snapToScreen(int i) {
    }
}
