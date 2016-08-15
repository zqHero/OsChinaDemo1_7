package com.hero.zhaoq.oschinademo1_7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.bean.News;

import java.util.List;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.adapter
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/8/15  22:51
 * 最新咨询的  界面数据   适配
 */
public class ListViewNewsAdapter extends BaseAdapter {

    private Context context;//运行上下文
    private List<News> listItems;//数据集合
    private LayoutInflater listContainer;//试图容器
    private int itemViewResource;//自定义项视图资源

    public ListViewNewsAdapter(Context context, List<News> data, int resource) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(itemViewResource,null);

            holder = new ViewHolder();
            //获取 控件对象
            holder.title = (TextView) convertView.findViewById(R.id.news_listitem_title);
            holder.author = (TextView) convertView.findViewById(R.id.news_listitem_author);
            holder.date = (TextView) convertView.findViewById(R.id.news_listitem_commentCount);
            holder.count = (TextView) convertView.findViewById(R.id.news_listitem_date);
            holder.flag = (ImageView) convertView.findViewById(R.id.news_listitem_flag);

            //设置 控件集 到convertView
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //绑定 数据
        News news = listItems.get(position);

        holder.title.setText(news.getTitle());
        holder.title.setTag(news);//设置隐藏参数(实体类)

        holder.author.setText(news.getAuthor());
        holder.date.setText("获取时间，日期");//StringUtils.friendly_time(news.getPubDate())

        holder.count.setText(news.getCommentCount()+"");

        //
//        if(StringUtils.isToday(news.getPubDate()))
//            holder.flag.setVisibility(View.VISIBLE);
//        else
//            holder.flag.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder{
        public TextView title,author,date,count;
        public ImageView flag;
    }
}
