package com.hero.zhaoq.oschinademo1_7.bean;

import java.io.Serializable;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:31
 * 实体类基本类
 */
public class Base implements Serializable {

    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "oschina";

    protected Notice notice; //通知类   包含 类型信息

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

}
