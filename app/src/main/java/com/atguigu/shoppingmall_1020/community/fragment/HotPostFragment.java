package com.atguigu.shoppingmall_1020.community.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.atguigu.shoppingmall_1020.community.adapter.HotPostListViewAdapter;
import com.atguigu.shoppingmall_1020.community.bean.HotPostBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by wangmingqiang on 2017/3/4.
 */

public class HotPostFragment extends BaseFragment {

    @InjectView(R.id.lv_hot_post)
    ListView lvHotPost;
    private HotPostListViewAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_hot_post, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.get().url(Constants.HOT_POST_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG","联网失败=="+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG","新帖联网成功==");
                processData(response);
            }
        });
    }

    private void processData(String response) {
        HotPostBean hotPostBean = JSON.parseObject(response, HotPostBean.class);
        List<HotPostBean.ResultEntity> result = hotPostBean.getResult();
        
        if(result!=null&&result.size()>0) {
            adapter = new HotPostListViewAdapter(mContext,result);
            lvHotPost.setAdapter(adapter);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
