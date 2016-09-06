package com.hero.zhaoq.oschinademo1_7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hero.zhaoq.oschinademo1_7.R;
import com.hero.zhaoq.oschinademo1_7.bean.Blog;
import com.hero.zhaoq.oschinademo1_7.bean.BlogList;
import com.hero.zhaoq.oschinademo1_7.common.StringUtils;

import java.util.List;

/**
 * Package_name:com.hero.zhaoq.oschinademo1_7.adapter
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/9/6  17:05
 * 博客的  适配类   最新博客
 */
public class ListViewBlogAdapter extends BaseAdapter{

    private Context 					context;//运行上下文
    private List<Blog> listItems;//数据集合
    private LayoutInflater listContainer;//视图容器
    private int 						itemViewResource;//自定义项视图源
    private int							blogtype;

    /**
     * 实例化   Adapter
     * @return
     */
    public ListViewBlogAdapter(Context context, int blogtype, List<Blog> data, int resource) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
        this.blogtype = blogtype;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;

        if(convertView == null){
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(this.itemViewResource, null);
            holderView = new HolderView();

            //获取控件对象
            holderView.title = (TextView)convertView.findViewById(R.id.blog_listitem_title);
            holderView.author = (TextView)convertView.findViewById(R.id.blog_listitem_author);
            holderView.count = (TextView)convertView.findViewById(R.id.blog_listitem_commentCount);
            holderView.date = (TextView)convertView.findViewById(R.id.blog_listitem_date);
            holderView.type = (ImageView)convertView.findViewById(R.id.blog_listitem_documentType);

            //设置  控件集到  convertView
            convertView.setTag(holderView);
        }else
        {
            holderView = (HolderView) convertView.getTag();
        }
        //绑定数据
        Blog blog = listItems.get(position);
        holderView.title.setText(blog.getTitle());
        holderView.title.setTag(blog);//设置隐藏参数(实体类)
        holderView.date.setText(StringUtils.friendly_time(blog.getPubDate()));
        holderView.count.setText(blog.getCommentCount()+"");
      //判断  传入的数据  类型   最新博客   我的博客
        if(blog.getDocumentType() == Blog.DOC_TYPE_ORIGINAL)
            holderView.type.setImageResource(R.mipmap.widget_original_icon);
        else
            holderView.type.setImageResource(R.mipmap.widget_repaste_icon);

        if(blogtype == BlogList.CATALOG_USER){
            holderView.author.setVisibility(View.GONE);
        }else{
            holderView.author.setText(blog.getAuthor()+"   发表于");
        }
        return convertView;
    }

    static class HolderView{				//自定义控件集合
        public TextView title;
        public TextView author;
        public TextView date;
        public TextView count;
        public ImageView type;
    }
}
