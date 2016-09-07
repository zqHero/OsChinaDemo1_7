package com.hero.zhaoq.oschinademo1_7.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hero.zhaoq.oschinademo1_7.R;

/**
 * 用户表情Adapter类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 *  用户表情  适配类
 */
public class GridViewFaceAdapter extends BaseAdapter
{
	// 定义Context
	private Context	mContext;
	// 定义整型数组 即图片源
	private static int[] mImageIds = new int[]{
			R.mipmap.f001,R.mipmap.f002,R.mipmap.f003,R.mipmap.f004,R.mipmap.f005,R.mipmap.f006,
			R.mipmap.f007,R.mipmap.f008,R.mipmap.f009,R.mipmap.f010,R.mipmap.f011,R.mipmap.f012,
			R.mipmap.f013,R.mipmap.f014,R.mipmap.f015,R.mipmap.f016,R.mipmap.f017,R.mipmap.f018,
			R.mipmap.f019,R.mipmap.f020,R.mipmap.f021,R.mipmap.f022,R.mipmap.f023,R.mipmap.f024,
			R.mipmap.f025,R.mipmap.f026,R.mipmap.f027,R.mipmap.f028,R.mipmap.f029,R.mipmap.f030,
			R.mipmap.f031,R.mipmap.f032,R.mipmap.f033,R.mipmap.f034,R.mipmap.f035,R.mipmap.f036,
			R.mipmap.f037,R.mipmap.f038,R.mipmap.f039,R.mipmap.f040,R.mipmap.f041,R.mipmap.f042,
			R.mipmap.f043,R.mipmap.f044,R.mipmap.f045,R.mipmap.f046,R.mipmap.f047,R.mipmap.f048,
			R.mipmap.f049,R.mipmap.f050,R.mipmap.f051,R.mipmap.f052,R.mipmap.f053,R.mipmap.f054,
			R.mipmap.f055,R.mipmap.f056,R.mipmap.f057,R.mipmap.f058,R.mipmap.f059,R.mipmap.f060,
			R.mipmap.f061,R.mipmap.f062,R.mipmap.f063,R.mipmap.f064,R.mipmap.f065,R.mipmap.f067,
			R.mipmap.f068,R.mipmap.f069,R.mipmap.f070,R.mipmap.f071,R.mipmap.f072,R.mipmap.f073,
			R.mipmap.f074,R.mipmap.f075,R.mipmap.f076,R.mipmap.f077,R.mipmap.f078,R.mipmap.f079,
			R.mipmap.f080,R.mipmap.f081,R.mipmap.f082,R.mipmap.f083,R.mipmap.f084,R.mipmap.f085,
			R.mipmap.f086,R.mipmap.f087,R.mipmap.f088,R.mipmap.f089,R.mipmap.f090,R.mipmap.f091,
			R.mipmap.f092,R.mipmap.f093,R.mipmap.f094,R.mipmap.f095,R.mipmap.f096,R.mipmap.f097,
			R.mipmap.f098,R.mipmap.f099,R.mipmap.f100,R.mipmap.f101,R.mipmap.f103,R.mipmap.f104,
			R.mipmap.f105
		};

	public static int[] getImageIds()
	{
		return mImageIds;
	}
	
	public GridViewFaceAdapter(Context c)
	{
		mContext = c;
	}
	
	// 获取图片的个数
	public int getCount()
	{
		return mImageIds.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return mImageIds[position];
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(mContext);
			// 设置图片n×n显示
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(mImageIds[position]);
		if(position < 65)
			imageView.setTag("["+position+"]");
		else if(position < 100)
			imageView.setTag("["+(position+1)+"]");
		else
			imageView.setTag("["+(position+2)+"]");
		
		return imageView;
	}
}