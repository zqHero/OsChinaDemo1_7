package com.hero.zhaoq.oschinademo1_7.bean;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.bean
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/9/6  17:01
 * 博客实体类
 */
public class Blog extends Entity{

    public final static int DOC_TYPE_REPASTE = 0;//转帖
    public final static int DOC_TYPE_ORIGINAL = 1;//原创

    private String title;
    private String where;
    private String body;
    private String author;
    private int authorId;
    private int documentType;
    private String pubDate;
    private int favorite;
    private int commentCount;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", where='" + where + '\'' +
                ", body='" + body + '\'' +
                ", author='" + author + '\'' +
                ", authorId=" + authorId +
                ", documentType=" + documentType +
                ", pubDate='" + pubDate + '\'' +
                ", favorite=" + favorite +
                ", commentCount=" + commentCount +
                ", url='" + url + '\'' +
                '}';
    }
}
