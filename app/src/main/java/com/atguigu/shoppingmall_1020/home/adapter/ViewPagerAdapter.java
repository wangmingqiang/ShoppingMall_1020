package com.atguigu.shoppingmall_1020.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atguigu.shoppingmall_1020.home.bean.HomeBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by wangmingqiang on 2017/2/25.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private final List<HomeBean.ResultEntity.ActInfoEntity> datas;
    private final Context mContext;

    public ViewPagerAdapter(Context mContext, List<HomeBean.ResultEntity.ActInfoEntity> act_info) {
        this.mContext=mContext;
        this.datas=act_info;
    }

    @Override
    public int getCount() {
        return datas.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView imageView=new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+datas.get(position).getIcon_url()).into(imageView);

        //添加到容器中
        container.addView(imageView);

        //设置点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener!=null) {
                    listener.OnItemClickListener(v,position);
                }
            }
        });
        return imageView;
    }


    public interface  OnItemClickListener{
        public void OnItemClickListener(View v,int position);
    }

    private OnItemClickListener listener;


    //设置item的点击事件
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
