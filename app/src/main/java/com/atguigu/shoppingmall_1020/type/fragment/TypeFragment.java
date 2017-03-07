package com.atguigu.shoppingmall_1020.type.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.base.BaseFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wangmingqiang on 2017/2/22.
 */

public class TypeFragment extends BaseFragment {

    @InjectView(R.id.tl_1)
    SegmentTabLayout tl1;
    @InjectView(R.id.iv_type_search)
    ImageView ivTypeSearch;
    @InjectView(R.id.fl_type)
    FrameLayout flType;

    private String[] titles={"分类","标签"};
    private ArrayList<Fragment> fragments;
    private Fragment tempFragment;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_type, null);
        ButterKnife.inject(this, view);
        return view;

    }

    @Override
    public void initData() {
        super.initData();
        initFragment();
        initListener();

        //默认选中listFragment
        switchFragment(0);

    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new ListFragment());
        fragments.add(new TagFragment());
    }

    private void initListener() {
        tl1.setTabData(titles);

        tl1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                //Toast.makeText(mContext, "position=="+position, Toast.LENGTH_SHORT).show();
                switchFragment(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void switchFragment(int position) {
        Fragment fragment=null;
        switch (position) {
            case 0:
                fragment = fragments.get(0);
                break;
            case 1:
                fragment = fragments.get(1);
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.fl_type,fragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_type_search)
    public void onClick() {
    }
}
