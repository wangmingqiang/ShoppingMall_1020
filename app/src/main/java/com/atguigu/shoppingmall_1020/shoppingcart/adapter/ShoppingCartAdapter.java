package com.atguigu.shoppingmall_1020.shoppingcart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.home.bean.GoodsBean;
import com.atguigu.shoppingmall_1020.shoppingcart.utils.CartStorage;
import com.atguigu.shoppingmall_1020.shoppingcart.view.AddSubView;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangmingqiang on 2017/2/28.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHoler> {


    private final Context mContext;
    private final List<GoodsBean> datas;
    private final TextView tvShopcartTotal;
    private final CheckBox checkboxAll;
    private final CheckBox checkboxDeleteAll;


    public ShoppingCartAdapter(Context mContext, List<GoodsBean> list, TextView tvShopcartTotal, CheckBox checkboxAll, CheckBox checkboxDeleteAll) {
        this.mContext = mContext;
        this.datas = list;
        this.tvShopcartTotal=tvShopcartTotal;
        this.checkboxAll=checkboxAll;
        this.checkboxDeleteAll=checkboxDeleteAll;

        showTotalPrice();
    }

    public void showTotalPrice() {
        //显示总价格
        tvShopcartTotal.setText("合计"+getTotalPrice());
    }

    //返回总价格
    public double getTotalPrice() {
        double totalPrice=0;
        if(datas!=null&&datas.size()>0) {
            for (int i=0;i<datas.size();i++){
                GoodsBean goodsBean=datas.get(i);
                if(goodsBean.isChecked()) {
                    totalPrice+=Double.parseDouble(goodsBean.getCover_price())*goodsBean.getNumber();
                }
            }
        }
        return totalPrice;
    }

    /**
     * 校验是否要全选
     */
    public void checkAll(){
        if(datas!=null&&datas.size()>0) {
            int number=0;
            for (int i=0;i<datas.size();i++){
                GoodsBean goodsBean=datas.get(i);
                if(!goodsBean.isChecked()) {
                    //只要有一个没有勾选
                    checkboxAll.setChecked(false);
                    checkboxDeleteAll.setChecked(false);
                }else {
                    //勾选
                    number++;
                }
            }
            if(datas.size()==number) {
                checkboxDeleteAll.setChecked(true);
                checkboxAll.setChecked(true);
            }
            
        }else {
            //没有数据
            checkboxAll.setChecked(false);
            checkboxDeleteAll.setChecked(false);
        }
    }
    public void cheAll_none(boolean isChecked){
        if(datas!=null&&datas.size()>0) {
            for (int i=0;i<datas.size();i++){
                GoodsBean goodsBean=datas.get(i);
                //设置是否勾选
                goodsBean.setChecked(isChecked);
                checkboxAll.setChecked(isChecked);
                checkboxDeleteAll.setChecked(isChecked);

                //更新视图
                notifyItemChanged(i);
            }
        }
    }

    /**
     * 删除数据
     */
    public void deleteData(){
        if(datas!=null&&datas.size()>0) {
            for (int i=0;i<datas.size();i++){
                GoodsBean goodsBean = datas.get(i);
                if(goodsBean.isChecked()) {
                    //1内存中删除
                    datas.remove(goodsBean);
                    //2本地也好保存
                    CartStorage.getInstance(mContext).deleteDate(goodsBean);
                    //刷新数据
                    notifyItemRemoved(i);
                    i--;
                }
            }
        }
    }


    /**
     * 创建viewholder---初始化视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_shop_cart, null);
        MyViewHoler myViewHoler = new MyViewHoler(view);
        return myViewHoler;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //1.先得到数据
        final GoodsBean goodsBean=datas.get(position);
        //2.绑定数据
        holder.cbGov.setChecked(goodsBean.isChecked());
        //图片
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+goodsBean.getFigure()).into(holder.ivGov);
        //设置名称
        holder.tvDescGov.setText(goodsBean.getName());
        //设置价格
        holder.tvPriceGov.setText("￥"+goodsBean.getCover_price());
        //设置数量
        holder.addSubView.setValue(goodsBean.getNumber());
        holder.addSubView.setMinValue(1);
        //设置库存-来自服务器-
        holder.addSubView.setMaxValue(10);

        holder.addSubView.setOnNumberChangerListener(new AddSubView.OnNumberChangerListener() {
            @Override
            public void onNumberChanger(int value) {
                //回调数量
                goodsBean.setNumber(value);
                CartStorage.getInstance(mContext).updataData(goodsBean);
                showTotalPrice();
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHoler extends RecyclerView.ViewHolder {
        @InjectView(R.id.cb_gov)
        CheckBox cbGov;
        @InjectView(R.id.iv_gov)
        ImageView ivGov;
        @InjectView(R.id.tv_desc_gov)
        TextView tvDescGov;
        @InjectView(R.id.tv_price_gov)
        TextView tvPriceGov;
        @InjectView(R.id.addSubView)
        AddSubView addSubView;

        MyViewHoler(View view) {
            super(view);
            ButterKnife.inject(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClickListener(v,getLayoutPosition());
                    }
                }
            });
        }
    }




    //回调点击事件的监听
    private OnItemClickListener itemClickListener;
    //点击item的监听
    public interface OnItemClickListener{
         void onItemClickListener(View view ,int position);
    }
    //设置item的监听


    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
