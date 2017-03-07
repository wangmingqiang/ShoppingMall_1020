package com.atguigu.shoppingmall_1020.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.home.bean.TypeListBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangmingqiang on 2017/3/6.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<TypeListBean.ResultEntity.PageDataEntity> datas;


    public GoodsListAdapter(Context mContext, List<TypeListBean.ResultEntity.PageDataEntity> datas) {

        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_goods_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //根据位置得到相应的数据
        TypeListBean.ResultEntity.PageDataEntity entity = datas.get(position);

        //绑定数据
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE + entity.getFigure()).placeholder(R.drawable.new_img_loading_2).into(holder.ivHot);
        holder.tvName.setText(entity.getName());
        holder.tvPrice.setText("￥"+entity.getCover_price());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_hot)
        ImageView ivHot;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_price)
        TextView tvPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) {
                        listener.OnItemClickListener(datas.get(getLayoutPosition()));
                    }
                }
            });
        }
    }


    public interface  OnItemClickListener{
        public void OnItemClickListener(TypeListBean.ResultEntity.PageDataEntity data);
    }

    private OnItemClickListener listener;


    //设置item的点击事件
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
