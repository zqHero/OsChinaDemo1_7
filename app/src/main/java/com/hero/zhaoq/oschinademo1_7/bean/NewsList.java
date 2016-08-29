package com.hero.zhaoq.oschinademo1_7.bean;

import android.util.Xml;

import com.hero.zhaoq.oschinademo1_7.AppException;
import com.hero.zhaoq.oschinademo1_7.common.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
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
    private int pageSize ;
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


    /**
     * <oschina>
     *     <catalog>0</catalog>
     *     <newsCount>0</newsCount>
     *     <pagesize>20</pagesize>
     *     <newslist>
     *         <news>
     *              <id>76617</id>
     *              <title>hetao 0.2.0 发布，原 htmlserver</title>
     *              <body>一周前我发布了超高性能 Web 服务器软件 htmlserver（登陆时手误用了我的...</body>
     *              <commentCount>24</commentCount>
     *              <author>calvinwilliams</author>
     *              <authorid>988092</authorid>
     *              <pubDate>2016-08-29 15:51:17</pubDate>
     *
     *              <url/>
     *                  <newstype>
     *                      <type>0</type>
     *                      <authoruid2>988092</authoruid2>
     *                      <eventurl/>
     *                  </newstype>
     *         </news>
     */

    public static NewsList parse(InputStream inputStream) throws IOException, AppException {
        NewsList newslist = new NewsList();
        News news = null;
        //获得XmlPullParser解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        try {
            xmlParser.setInput(inputStream, UTF8);
            //获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType = xmlParser.getEventType();
            //一直循环，直到文档结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                String tag = xmlParser.getName();
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("catalog")) {
                            newslist.catalog = StringUtils.toInt(xmlParser.nextText(), 0);
                        } else if (tag.equalsIgnoreCase("pageSize")) {
                            newslist.pageSize = StringUtils.toInt(xmlParser.nextText(), 0);
                        } else if (tag.equalsIgnoreCase("newsCount")) {
                            newslist.newsCount = StringUtils.toInt(xmlParser.nextText(), 0);
                        } else if (tag.equalsIgnoreCase("news")) {
                            news = new News();
                        } else if (news != null) {
                            if (tag.equalsIgnoreCase("id")) {
                                news.id = StringUtils.toInt(xmlParser.nextText(), 0);
                            } else if (tag.equalsIgnoreCase("title")) {
                                news.setTitle(xmlParser.nextText());
                            } else if (tag.equalsIgnoreCase("url")) {
                                news.setUrl(xmlParser.nextText());
                            } else if (tag.equalsIgnoreCase("author")) {
                                news.setAuthor(xmlParser.nextText());
                            } else if (tag.equalsIgnoreCase("authorid")) {
                                news.setAuthorId(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase("commentCount")) {
                                news.setCommentCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase("pubDate")) {
                                news.setPubDate(xmlParser.nextText());
                            } else if (tag.equalsIgnoreCase("type")) {
                                news.getNewType().type = StringUtils.toInt(xmlParser.nextText(), 0);
                            } else if (tag.equalsIgnoreCase("attachment")) {
                                news.getNewType().attachment = xmlParser.nextText();
                            } else if (tag.equalsIgnoreCase("authoruid2")) {
                                news.getNewType().authoruid2 = StringUtils.toInt(xmlParser.nextText(), 0);
                            }
                        }
                        //通知信息
                        else if (tag.equalsIgnoreCase("notice")) {
                            newslist.setNotice(new Notice());
                        } else if (newslist.getNotice() != null) {
                            if (tag.equalsIgnoreCase("atmeCount")) {
                                newslist.getNotice().setAtmeCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase("msgCount")) {
                                newslist.getNotice().setMsgCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase("reviewCount")) {
                                newslist.getNotice().setReviewCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            } else if (tag.equalsIgnoreCase("newFansCount")) {
                                newslist.getNotice().setNewFansCount(StringUtils.toInt(xmlParser.nextText(), 0));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //如果遇到标签结束，则把对象添加进集合中
                        if (tag.equalsIgnoreCase("news") && news != null) {
                            newslist.getNewslist().add(news);
                            news = null;
                        }
                        break;
                }
                //如果xml没有结束，则导航到下一个节点
                int a = xmlParser.next();
                evtType = a;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            throw AppException.xml(e);
        } finally {
            inputStream.close();
        }
        return newslist;
    }

    @Override
    public String toString() {
        return "NewsList{" +
                "catalog=" + catalog +
                ", pageSize=" + pageSize +
                ", newsCount=" + newsCount +
                ", newslist=" + newslist +
                '}';
    }
}
