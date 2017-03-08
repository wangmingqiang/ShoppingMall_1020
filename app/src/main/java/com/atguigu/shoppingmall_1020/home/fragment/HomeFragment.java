package com.atguigu.shoppingmall_1020.home.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.app.GoodsInfoActivity;
import com.atguigu.shoppingmall_1020.app.SearchActivity;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.atguigu.shoppingmall_1020.home.adapter.HomeAdapter;
import com.atguigu.shoppingmall_1020.home.bean.GoodsBean;
import com.atguigu.shoppingmall_1020.home.bean.HomeBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;
import static com.atguigu.shoppingmall_1020.home.adapter.HomeAdapter.GOODS_BEAN;

/**
 * Created by wangmingqiang on 2017/2/22.
 */

public class  HomeFragment extends BaseFragment {

    @InjectView(R.id.tv_search_home)
    TextView tvSearchHome;

    @InjectView(R.id.tv_message_home)
    TextView tvMessageHome;

    @InjectView(R.id.rv_home)
    RecyclerView rvHome;

    @InjectView(R.id.ib_top)
    ImageButton ibTop;
    @InjectView(R.id.btnSan)
    ImageView btnSan;

    private HomeAdapter adapter;
    private HomeBean homeBean;

    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.fragment_home, null);
        //让当前类跟视图绑定起来
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {

        super.initData();

        getDataFromNet();

    }

    private void getDataFromNet() {
        OkHttpUtils
                .get()
                //联网的地址
                .url(Constants.HOME_URL)
                //100代表http，101代表https安全的
                .id(100)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(mContext, "联网失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(mContext, "联网成功", Toast.LENGTH_SHORT).show();

                        processData(response);

                    }
                });

    }

    //拿到数据
    private void processData(String response) {

        //使用fastjson解析json数据
        homeBean = JSON.parseObject(response, HomeBean.class);

        Log.e("TAG", "解析数据成功==" + homeBean.getResult().getHot_info().get(0).getName());

        //设置RecyclerView的适配器
        adapter = new HomeAdapter(mContext, homeBean.getResult());

        rvHome.setAdapter(adapter);


        //设置布局管理器
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        rvHome.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position <= 3) {
                    //隐藏按钮
                    ibTop.setVisibility(View.GONE);

                } else {
                    ibTop.setVisibility(View.VISIBLE);
                }
                return 1;
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv_search_home, R.id.tv_message_home, R.id.ib_top,R.id.btnSan})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_search_home:
                //Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_message_home:
                Toast.makeText(mContext, "查看", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ib_top:
                // Toast.makeText(mContext, "顶部", Toast.LENGTH_SHORT).show();
                rvHome.scrollToPosition(0);
                break;

            case R.id.btnSan:
                startActivityForResult(new Intent(mContext, CaptureActivity.class), 0);

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }




    //二维码扫描
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result");

            List<HomeBean.ResultEntity.HotInfoEntity> hot_info = homeBean.getResult().getHot_info();
            List<HomeBean.ResultEntity.ActInfoEntity> act_info = homeBean.getResult().getAct_info();
            List<HomeBean.ResultEntity.RecommendInfoEntity> recommend_info = homeBean.getResult().getRecommend_info();
            List<HomeBean.ResultEntity.BannerInfoEntity> banner_info = homeBean.getResult().getBanner_info();
            List<HomeBean.ResultEntity.SeckillInfoEntity.ListEntity> list = homeBean.getResult().getSeckill_info().getList();
            List<HomeBean.ResultEntity.ChannelInfoEntity> channel_info = homeBean.getResult().getChannel_info();

            for (int i = 0; i <hot_info.size(); i++) {
                if(result.equals(Constants.BASE_URL_IMAGE+hot_info.get(i).getFigure())) {
                    GoodsBean goodsBean=new GoodsBean();
                    goodsBean.setName(hot_info.get(i).getName());
                    goodsBean.setCover_price(hot_info.get(i).getCover_price());
                    goodsBean.setFigure(hot_info.get(i).getFigure());
                    goodsBean.setProduct_id(hot_info.get(i).getProduct_id());

                    Intent intent=new Intent(mContext, GoodsInfoActivity.class);

                    intent.putExtra(GOODS_BEAN,goodsBean);

                    mContext.startActivity(intent);
                }

                else if(result.equals(Constants.BASE_URL_IMAGE+recommend_info.get(i).getFigure())) {
                    GoodsBean goodsBean=new GoodsBean();
                    goodsBean.setName(recommend_info.get(i).getName());
                    goodsBean.setCover_price(recommend_info.get(i).getCover_price());
                    goodsBean.setFigure(recommend_info.get(i).getFigure());
                    goodsBean.setProduct_id(recommend_info.get(i).getProduct_id());

                    Intent intent=new Intent(mContext, GoodsInfoActivity.class);

                    intent.putExtra(GOODS_BEAN,goodsBean);

                    mContext.startActivity(intent);
                }

                else if(result.equals(Constants.BASE_URL_IMAGE+list.get(i).getFigure())) {
                    GoodsBean goodsBean=new GoodsBean();
                    goodsBean.setName(list.get(i).getName());
                    goodsBean.setCover_price(list.get(i).getCover_price());
                    goodsBean.setFigure(list.get(i).getFigure());
                    goodsBean.setProduct_id(list.get(i).getProduct_id());

                    Intent intent=new Intent(mContext, GoodsInfoActivity.class);

                    intent.putExtra(GOODS_BEAN,goodsBean);

                    mContext.startActivity(intent);
                }


            }

        }
    }

    @Override
    public  void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA},
                    1);}
    }
}
