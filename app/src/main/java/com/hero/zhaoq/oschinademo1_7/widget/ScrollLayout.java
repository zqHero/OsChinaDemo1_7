package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.widget
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/13  22:09
 * 自定义  View布局   左右滑动  切换屏幕
 */
public class ScrollLayout extends ViewGroup{

    public ScrollLayout(Context context) {
        super(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
