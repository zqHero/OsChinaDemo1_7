package com.hero.zhaoq.oschinademo1_7.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  22:55
 * 咨询   实体类
 */
public class News extends Entity{

    private String title;
    private String url;
    private String body;
    private String author;
    private int authorId;
    private int commentCount;
    private String pubDate;
    private String softwareLink;
    private String softwareName;
    private int favorite;
    private NewsType newType;
    private List<Relative> relatives;

    //创建对象  创建  news时创建类型对象
    public News(){
        this.newType = new NewsType();
        this.relatives = new ArrayList<Relative>();
    }

    public class NewsType implements Serializable { //请求类型
        public int type;
        public String attachment;
        public int authoruid2;
    }
    public static class Relative implements Serializable{ //
        public String title;
        public String url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Relative> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<Relative> relatives) {
        this.relatives = relatives;
    }

    public NewsType getNewType() {
        return newType;
    }

    public void setNewType(NewsType newType) {
        this.newType = newType;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getSoftwareLink() {
        return softwareLink;
    }

    public void setSoftwareLink(String softwareLink) {
        this.softwareLink = softwareLink;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", body='" + body + '\'' +
                ", author='" + author + '\'' +
                ", authorId=" + authorId +
                ", commentCount=" + commentCount +
                ", pubDate='" + pubDate + '\'' +
                ", softwareLink='" + softwareLink + '\'' +
                ", softwareName='" + softwareName + '\'' +
                ", favorite=" + favorite +
                ", newType=" + newType +
                ", relatives=" + relatives +
                '}';
    }
}
