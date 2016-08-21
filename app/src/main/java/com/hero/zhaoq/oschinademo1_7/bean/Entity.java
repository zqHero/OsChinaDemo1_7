package com.hero.zhaoq.oschinademo1_7.bean;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:32
 * 实体类 信息
 */
public class Entity extends Base {

    protected int id;

    public int getId() {
        return id;
    }

    protected String cacheKey;  //缓存

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
