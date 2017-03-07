package com.atguigu.shoppingmall_1020.type.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.atguigu.shoppingmall_1020.type.adapter.TagGridViewAdapter;
import com.atguigu.shoppingmall_1020.type.bean.TagBean;
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

public class TagFragment extends BaseFragment {

    @InjectView(R.id.gv_tag)
    GridView gvTag;
    private List<TagBean.ResultBean> result;
    private TagGridViewAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_tag, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.get().url(Constants.TAG_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "联网失败了" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", "TagFragment的数据联网成功了==");
                //解析数据
                processData(response);
            }
        });
    }

    private void processData(String response) {

        TagBean tagBean = JSON.parseObject(response, TagBean.class);
        result = tagBean.getResult();

        if (result != null && result.size() > 0) {
            //设置适配器
            adapter = new TagGridViewAdapter(mContext,result);

            gvTag.setAdapter(adapter);

            //设置item的点击事件
            gvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TagBean.ResultBean tagBean = result.get(position);
                    Toast.makeText(mContext, ""+tagBean.toString(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
