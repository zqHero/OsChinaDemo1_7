package com.hero.zhaoq.oschinademo1_7;

import android.app.Application;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  20:21
 */
public class AppContext extends Application{

    public final static String CONF_SCROLL = "perf_scroll";

    public static final int PAGE_SIZE = 20; //默认分页大小
//    /**
//     * 是否左右滑动
//     * @return
//     */
//    public boolean isScroll()
//    {
//        String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
//        //默认是关闭左右滑动
//        if(StringUtils.isEmpty(perf_scroll))
//            return false;
//        else
//            return StringUtils.toBool(perf_scroll);
//    }

}
