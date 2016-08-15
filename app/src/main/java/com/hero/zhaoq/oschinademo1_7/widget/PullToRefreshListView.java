package com.hero.zhaoq.oschinademo1_7.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.widget
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  23:19
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
