package com.hero.zhaoq.oschinademo1_7.bean;

import java.io.File;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/21  16:34
 * 动弹实体类
 */
public class Tweet extends Entity{


    public final static String NODE_ID = "id";
    public final static String NODE_FACE = "portrait";
    public final static String NODE_BODY = "body";
    public final static String NODE_AUTHORID = "authorid";
    public final static String NODE_AUTHOR = "author";
    public final static String NODE_PUBDATE = "pubDate";
    public final static String NODE_COMMENTCOUNT = "commentCount";
    public final static String NODE_IMGSMALL = "imgSmall";
    public final static String NODE_IMGBIG = "imgBig";
    public final static String NODE_APPCLIENT = "appclient";
    public final static String NODE_START = "tweet";

    public final static int CLIENT_MOBILE = 2;
    public final static int CLIENT_ANDROID = 3;
    public final static int CLIENT_IPHONE = 4;
    public final static int CLIENT_WINDOWS_PHONE = 5;

    private String face;
    private String body;
    private String author;
    private int authorId;
    private int commentCount;
    private String pubDate;
    private String imgSmall;
    private String imgBig;
    private File imageFile;
    private int appClient;

    public int getAppClient() {
        return appClient;
    }
    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }
    public File getImageFile() {
        return imageFile;
    }
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
    public String getImgSmall() {
        return imgSmall;
    }
    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }
    public String getImgBig() {
        return imgBig;
    }
    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }
    public String getFace() {
        return face;
    }
    public void setFace(String face) {
        this.face = face;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
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
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
