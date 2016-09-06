package com.hero.zhaoq.oschinademo1_7.ui;

import android.app.Activity;
import android.os.Bundle;

import com.hero.zhaoq.oschinademo1_7.R;


/**
 * 显示   资讯列表的  详情信息  传过来  信息 id     重新 请求并获取数据
 */
public class NewsDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
    }
}
