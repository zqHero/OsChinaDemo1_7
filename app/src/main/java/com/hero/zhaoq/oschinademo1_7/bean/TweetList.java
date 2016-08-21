package com.hero.zhaoq.oschinademo1_7.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:30
 * 动弹列表  实体类   包含很多动弹信息
 */
public class TweetList extends Entity{

    public final static int CATALOG_LASTEST = 0;
    public final static int CATALOG_HOT = -1;

    private int pageSize;
    private int tweetCount;
    private List<Tweet> tweetlist = new ArrayList<Tweet>();

    public int getPageSize() {
        return pageSize;
    }
    public int getTweetCount() {
        return tweetCount;
    }
    public List<Tweet> getTweetlist() {
        return tweetlist;
    }

}
