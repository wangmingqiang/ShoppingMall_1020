package com.atguigu.shoppingmall_1020.type.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.atguigu.shoppingmall_1020.type.adapter.TypeLeftAdapter;
import com.atguigu.shoppingmall_1020.type.adapter.TypeRightAdapter;
import com.atguigu.shoppingmall_1020.type.bean.TypeBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by wangmingqiang on 2017/3/3.
 */

public class ListFragment extends BaseFragment {

    @InjectView(R.id.lv_left)
    ListView lvLeft;
    @InjectView(R.id.rv_right)
    RecyclerView rvRight;
    private TypeLeftAdapter leftAdapter;


    //真实情况是网络请求得到数据   这里就用数据模拟一下
    private String[] titles = new String[]{"小裙子", "上衣", "下装", "外套", "配件", "包包", "装扮", "居家宅品",
            "办公文具", "数码周边", "游戏专区"};

    //联网的url的集合
    private String[] urls = new String[]{Constants.SKIRT_URL, Constants.JACKET_URL, Constants.PANTS_URL, Constants.OVERCOAT_URL,
            Constants.ACCESSORY_URL, Constants.BAG_URL, Constants.DRESS_UP_URL, Constants.HOME_PRODUCTS_URL, Constants.STATIONERY_URL,
            Constants.DIGIT_URL, Constants.GAME_URL};
    private TypeRightAdapter rightAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_list, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        leftAdapter = new TypeLeftAdapter(mContext,titles);
        lvLeft.setAdapter(leftAdapter);

        //设置item的点击事件
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.传入被点击的位置
                leftAdapter.changeSelected(position);
                //2.适配器刷新
                leftAdapter.notifyDataSetChanged();

                getDataFromNet(urls[position]);
            }
        });
        //联网请求
        getDataFromNet(urls[0]);


    }

    private void getDataFromNet(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG","联网失败了"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                //Toast.makeText(mContext, "成功"+response, Toast.LENGTH_SHORT).show();
                //解析数据
                processData(response);
            }
        });
    }

    private void processData(String response) {

        TypeBean typeBean = JSON.parseObject(response, TypeBean.class);

        List<TypeBean.ResultBean> result = typeBean.getResult();

        if(result!=null&&result.size()>0) {
            //设置适配器
            rightAdapter = new TypeRightAdapter(mContext,result);
            rvRight.setAdapter(rightAdapter);
        }

        //设置布局管理器
        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==0) {
                    return  3;
                }else{
                    return  1;
                }
            }
        });

        rvRight.setLayoutManager(manager);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
