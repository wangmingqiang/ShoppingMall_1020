package com.atguigu.shoppingmall_1020.type.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.atguigu.shoppingmall_1020.type.adapter.TagGridViewAdapter;
import com.atguigu.shoppingmall_1020.type.bean.TagBean;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by wangmingqiang on 2017/3/3.
 */

public class TagFragment extends BaseFragment {


    @InjectView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    private List<TagBean.ResultBean> result;
    private ArrayList<String> list;
    private TagGridViewAdapter adapter;
    private int[] colors = {
            Color.parseColor("#f0a420"), Color.parseColor("#4ba5e2"), Color.parseColor("#f0839a"),
            Color.parseColor("#4ba5e2"), Color.parseColor("#f0839a"), Color.parseColor("#f0a420"),
            Color.parseColor("#f0839a"), Color.parseColor("#f0a420"), Color.parseColor("#4ba5e2")
    };

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
        list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            String name = result.get(i).getName();
            list.add(name);
        }

        if (result != null && result.size() > 0) {

//            adapter = new TagGridViewAdapter(mContext, result);
//            gvTag.setAdapter(adapter);
//
//            //设置item的点击事件
//            gvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    TagBean.ResultBean tagBean = result.get(position);
//                    Toast.makeText(mContext, "" + tagBean.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });


            idFlowlayout.setAdapter(new TagAdapter(result)
            {
                @Override
                public View getView(FlowLayout parent, int position, Object s)
                {
                    TextView tv = (TextView) View.inflate(mContext,R.layout.tv, null);
                    tv.setText(result.get(position).getName());
                    tv.setTextColor(colors[position%colors.length]);
                    return tv;
                }

            });




            idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
            {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent)
                {
                    Toast.makeText(getActivity(), result.get(position).getName(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            idFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
            {
                @Override
                public void onSelected(Set<Integer> selectPosSet)
                {
                    getActivity().setTitle("choose:" + selectPosSet.toString());
                }
            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }
}
