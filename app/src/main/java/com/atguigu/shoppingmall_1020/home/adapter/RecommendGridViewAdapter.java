package com.atguigu.shoppingmall_1020.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.home.bean.HomeBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangmingqiang on 2017/2/25.
 */

public class RecommendGridViewAdapter extends BaseAdapter {
    private final List<HomeBean.ResultEntity.RecommendInfoEntity> dastas;
    private final Context mContext;

    public RecommendGridViewAdapter(Context mContext, List<HomeBean.ResultEntity.RecommendInfoEntity> recommend_info) {
        this.mContext = mContext;
        this.dastas = recommend_info;
    }

    @Override
    public int getCount() {
        return dastas.size();
    }

    @Override
    public Object getItem(int position) {
        return dastas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recommend_grid_view, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //根据位置得到对应的数据
        HomeBean.ResultEntity.RecommendInfoEntity recommendInfoEntity = dastas.get(position);

        //绑定数据
        viewHolder.tvName.setText(recommendInfoEntity.getName());
        viewHolder.tvPrice.setText("￥"+recommendInfoEntity.getCover_price());

        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+recommendInfoEntity.getFigure()).into(viewHolder.ivRecommend);
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_recommend)
        ImageView ivRecommend;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
