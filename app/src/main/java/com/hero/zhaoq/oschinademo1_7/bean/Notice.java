package com.hero.zhaoq.oschinademo1_7.bean;

import java.io.Serializable;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/20  18:42
 * 通知  信息实体类
 */
public class Notice implements Serializable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "oschina";

    //请求类型
    public final static int	TYPE_ATME = 1;
    public final static int	TYPE_MESSAGE = 2; //消息
    public final static int	TYPE_COMMENT = 3;//评论
    public final static int	TYPE_NEWFAN = 4; //

    private int atmeCount;
    private int msgCount;
    private int reviewCount;
    private int newFansCount;


    public int getAtmeCount() {
        return atmeCount;
    }

    public void setAtmeCount(int atmeCount) {
        this.atmeCount = atmeCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getNewFansCount() {
        return newFansCount;
    }

    public void setNewFansCount(int newFansCount) {
        this.newFansCount = newFansCount;
    }
}
