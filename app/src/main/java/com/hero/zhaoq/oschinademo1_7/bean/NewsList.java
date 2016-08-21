package com.hero.zhaoq.oschinademo1_7.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  17:34
 * 信息列表 的显示
 */
public class NewsList extends Entity {

    public final static int CATALOG_ALL = 1;
    public final static int CATALOG_INTEGRATION = 2;
    public final static int CATALOG_SOFTWARE = 3;

    private int catalog;
    private int pageSize;
    private int newsCount;
    private List<News> newslist = new ArrayList<News>();


    public int getCatalog() {
        return catalog;
    }
    public int getPageSize() {
        return pageSize;
    }
    public int getNewsCount() {
        return newsCount;
    }
    public List<News> getNewslist() {
        return newslist;
    }

}
