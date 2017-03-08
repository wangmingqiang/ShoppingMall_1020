package com.atguigu.shoppingmall_1020.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.app.GoodsInfoActivity;
import com.atguigu.shoppingmall_1020.home.adapter.ExpandableListViewAdapter;
import com.atguigu.shoppingmall_1020.home.adapter.GoodsListAdapter;
import com.atguigu.shoppingmall_1020.home.adapter.HomeAdapter;
import com.atguigu.shoppingmall_1020.home.bean.GoodsBean;
import com.atguigu.shoppingmall_1020.home.bean.TypeListBean;
import com.atguigu.shoppingmall_1020.home.view.SpaceItemDecoration;
import com.atguigu.shoppingmall_1020.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class GoodsListActivity extends AppCompatActivity {

    @InjectView(R.id.ib_goods_list_back)
    ImageButton ibGoodsListBack;
    @InjectView(R.id.tv_goods_list_search)
    TextView tvGoodsListSearch;
    @InjectView(R.id.ib_goods_list_home)
    ImageButton ibGoodsListHome;
    @InjectView(R.id.tv_goods_list_sort)
    TextView tvGoodsListSort;
    @InjectView(R.id.tv_goods_list_price)
    TextView tvGoodsListPrice;
    @InjectView(R.id.iv_goods_list_arrow)
    ImageView ivGoodsListArrow;
    @InjectView(R.id.ll_goods_list_price)
    LinearLayout llGoodsListPrice;
    @InjectView(R.id.tv_goods_list_select)
    TextView tvGoodsListSelect;
    @InjectView(R.id.ll_goods_list_head)
    LinearLayout llGoodsListHead;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.dl_left)
    DrawerLayout dlLeft;
    @InjectView(R.id.ib_drawer_layout_back)
    ImageButton ibDrawerLayoutBack;
    @InjectView(R.id.tv_ib_drawer_layout_title)
    TextView tvIbDrawerLayoutTitle;
    @InjectView(R.id.ib_drawer_layout_confirm)
    TextView ibDrawerLayoutConfirm;
    @InjectView(R.id.rb_select_hot)
    RadioButton rbSelectHot;
    @InjectView(R.id.rb_select_new)
    RadioButton rbSelectNew;
    @InjectView(R.id.rg_select)
    RadioGroup rgSelect;
    @InjectView(R.id.tv_drawer_price)
    TextView tvDrawerPrice;
    @InjectView(R.id.tv_drawer_recommend)
    TextView tvDrawerRecommend;
    @InjectView(R.id.rl_select_recommend_theme)
    RelativeLayout rlSelectRecommendTheme;
    @InjectView(R.id.tv_drawer_type)
    TextView tvDrawerType;
    @InjectView(R.id.rl_select_type)
    RelativeLayout rlSelectType;
    @InjectView(R.id.btn_select_all)
    Button btnSelectAll;
    @InjectView(R.id.ll_select_root)
    LinearLayout llSelectRoot;
    @InjectView(R.id.btn_drawer_layout_cancel)
    Button btnDrawerLayoutCancel;
    @InjectView(R.id.btn_drawer_layout_confirm)
    Button btnDrawerLayoutConfirm;
    @InjectView(R.id.rl_select_price)
    RelativeLayout rlSelectPrice;
    @InjectView(R.id.ll_price_root)
    LinearLayout llPriceRoot;
    @InjectView(R.id.btn_drawer_theme_cancel)
    Button btnDrawerThemeCancel;
    @InjectView(R.id.btn_drawer_theme_confirm)
    Button btnDrawerThemeConfirm;
    @InjectView(R.id.ll_theme_root)
    LinearLayout llThemeRoot;
    @InjectView(R.id.btn_drawer_type_cancel)
    Button btnDrawerTypeCancel;
    @InjectView(R.id.btn_drawer_type_confirm)
    Button btnDrawerTypeConfirm;
    @InjectView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @InjectView(R.id.ll_type_root)
    LinearLayout llTypeRoot;

    @InjectView(R.id.rb_theme_selector)
    RadioGroup rbThemeSelector;

    @InjectView(R.id.rb_price_seletor)
    RadioGroup rbPriceSeletor;


    private int position;
    /**
     * 请求网络
     */
    private String[] urls = new String[]{
            Constants.CLOSE_STORE,
            Constants.GAME_STORE,
            Constants.COMIC_STORE,
            Constants.COSPLAY_STORE,
            Constants.GUFENG_STORE,
            Constants.STICK_STORE,
            Constants.WENJU_STORE,
            Constants.FOOD_STORE,
            Constants.SHOUSHI_STORE,
    };
    private GoodsListAdapter adapter;
    private int click_count;

    /**
     * 父层的数据
     */
    private ArrayList<String> group;
    /**
     * 孩子的数据
     */
    private ArrayList<List<String>> child;

    private ExpandableListViewAdapter expandableListViewAdapter;
    private String tempTheme;
    private String tempPrice;
    private String tempType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.inject(this);
        getData();
    }

    private void getData() {
        position = getIntent().getIntExtra("position", 0);
//        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        getDataFromNet(urls[position]);

        initView();

        setListenter();
    }


    private void setListenter() {

        //设置筛选价格监听
        rbPriceSeletor.setOnCheckedChangeListener(new MyPriceOnCheckedChangeListener());

        //设置主题监听
        rbThemeSelector.setOnCheckedChangeListener(new MyThemeOnCheckedChangeListener());


    }

    class MyThemeOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_theme_all:
                    tempTheme = "全部";
                    break;
                case R.id.rb_theme_funko:
                    tempTheme = "FUNKO";
                    break;
                case R.id.rb_theme_gress:
                    tempTheme = "长草颜文字";
                    break;
                case R.id.rb_theme_gsc:
                    tempTheme = "GSC";
                    break;
                case R.id.rb_theme_moon:
                    tempTheme = "秦时明月";
                    break;
                case R.id.rb_theme_note:
                    tempTheme = "盗墓笔记";
                    break;
                case R.id.rb_theme_quanzhi:
                    tempTheme = "全职高手";
                    break;
                case R.id.rb_theme_sword:
                    tempTheme = "剑侠情愿叁";
                    break;

            }

            Toast.makeText(GoodsListActivity.this, "" + tempTheme, Toast.LENGTH_SHORT).show();
        }
    }

    class MyPriceOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_price_nolimit:
                    tempPrice = "nolimit";
                    break;
                case R.id.rb_price_0_15:
                    tempPrice = "0-15";
                    break;
                case R.id.rb_price_15_30:
                    tempPrice = "15-30";
                    break;
                case R.id.rb_price_30_50:
                    tempPrice = "30-50";
                    break;
                case R.id.rb_price_50_70:
                    tempPrice = "50-70";
                    break;
                case R.id.rb_price_70_100:
                    tempPrice = "70-100";
                    break;
                case R.id.rb_price_100:
                    tempPrice = "100";
                    break;

            }

            Toast.makeText(GoodsListActivity.this, "" + tempPrice, Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        //设置综合排序高亮显示
        tvGoodsListSort.setTextColor(Color.parseColor("#ed4141"));

        //价格设置默认
        tvGoodsListPrice.setTextColor(Color.parseColor("#333538"));

        //筛选设置默认
        tvGoodsListSelect.setTextColor(Color.parseColor("#333538"));


        showSelectorLayout();//默认显示筛选页面
    }

    @OnClick({R.id.ib_goods_list_back, R.id.tv_goods_list_search, R.id.ib_goods_list_home,
            R.id.tv_goods_list_sort, R.id.tv_goods_list_price, R.id.tv_goods_list_select,
            R.id.ib_drawer_layout_back, R.id.ib_drawer_layout_confirm, R.id.rl_select_price,
            R.id.rl_select_recommend_theme, R.id.rl_select_type, R.id.btn_drawer_layout_cancel,
            R.id.btn_drawer_layout_confirm, R.id.btn_drawer_theme_cancel, R.id.btn_drawer_theme_confirm,
            R.id.btn_drawer_type_cancel, R.id.btn_drawer_type_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_goods_list_back:
                finish();
                break;
            case R.id.tv_goods_list_search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_goods_list_home:
                Toast.makeText(this, "主页面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_goods_list_sort:
                click_count = 0;
                ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_normal);
//                Toast.makeText(this, "综合排序", Toast.LENGTH_SHORT).show();
                //设置综合排序高亮显示
                tvGoodsListSort.setTextColor(Color.parseColor("#ed4141"));

                //价格设置默认
                tvGoodsListPrice.setTextColor(Color.parseColor("#333538"));

                //筛选设置默认
                tvGoodsListSelect.setTextColor(Color.parseColor("#333538"));

                break;
            case R.id.tv_goods_list_price:
//                Toast.makeText(this, "价格", Toast.LENGTH_SHORT).show();
                //设置价格高亮
                tvGoodsListPrice.setTextColor(Color.parseColor("#ed4141"));
                //综合设置默认
                tvGoodsListSort.setTextColor(Color.parseColor("#333538"));
                //筛选设置默认
                tvGoodsListSelect.setTextColor(Color.parseColor("#333538"));


                click_count++;

                if (click_count % 2 == 1) {
                    // 箭头向下红
                    ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_desc);
                } else {
                    // 箭头向上红
                    ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_asc);
                }

                break;
            case R.id.tv_goods_list_select:
//                Toast.makeText(this, "筛选", Toast.LENGTH_SHORT).show();
                click_count = 0;
                ivGoodsListArrow.setBackgroundResource(R.drawable.new_price_sort_normal);

                //筛选设置高亮
                tvGoodsListSelect.setTextColor(Color.parseColor("#ed4141"));
                //综合设置默认
                tvGoodsListSort.setTextColor(Color.parseColor("#333538"));
                //价格设置默认
                tvGoodsListPrice.setTextColor(Color.parseColor("#333538"));

                //打开DrawLayout
                dlLeft.openDrawer(Gravity.RIGHT);
                break;

            case R.id.ib_drawer_layout_back:
                //关闭DrawLayout
                dlLeft.closeDrawers();
                break;
            case R.id.ib_drawer_layout_confirm:
                Toast.makeText(this, "筛选-确定", Toast.LENGTH_SHORT).show();
                dlLeft.closeDrawers();
                getDataFromNet(urls[3]);

                break;
            case R.id.rl_select_price://显示-价格
                llPriceRoot.setVisibility(View.VISIBLE);
                showPriceLayout();

                break;
            case R.id.rl_select_recommend_theme://主题
                llThemeRoot.setVisibility(View.VISIBLE);
                showThemeLayout();
                break;
            case R.id.rl_select_type://类别
                llTypeRoot.setVisibility(View.VISIBLE);
                showTypeLayout();
                break;
            case R.id.btn_drawer_layout_cancel:
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;

            case R.id.btn_drawer_layout_confirm:
                //Toast.makeText(this, "价格-确定", Toast.LENGTH_SHORT).show();
                tvDrawerPrice.setText(tempPrice);
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;

            case R.id.btn_drawer_theme_cancel:
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;
            case R.id.btn_drawer_theme_confirm:
                //Toast.makeText(this, "主题-确定", Toast.LENGTH_SHORT).show();
                tvDrawerRecommend.setText(tempTheme);
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;

            case R.id.btn_drawer_type_cancel:
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;
            case R.id.btn_drawer_type_confirm:
                //Toast.makeText(this, "类别-确定", Toast.LENGTH_SHORT).show();
                tvDrawerType.setText(tempType);
                llSelectRoot.setVisibility(View.VISIBLE);
                showSelectorLayout();
                break;
        }
    }


    //筛选页面
    private void showSelectorLayout() {
        llPriceRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }


    //价格页面
    private void showPriceLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }

    //主题页面
    private void showThemeLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llPriceRoot.setVisibility(View.GONE);
        llTypeRoot.setVisibility(View.GONE);
    }

    //类别页面
    private void showTypeLayout() {
        llSelectRoot.setVisibility(View.GONE);
        llPriceRoot.setVisibility(View.GONE);
        llThemeRoot.setVisibility(View.GONE);

        //初始化ExpandableListView
        initExpandableListView();
    }

    private void initExpandableListView() {

        //创建集合
        group = new ArrayList<>();
        child = new ArrayList<>();

        //添加数据
        addInfo("全部", new String[]{});
        addInfo("上衣", new String[]{"古风", "和风", "lolita", "日常"});
        addInfo("下装", new String[]{"日常", "泳衣", "汉风", "lolita", "创意T恤"});
        addInfo("外套", new String[]{"汉风", "古风", "lolita", "胖次", "南瓜裤", "日常"});

        //设置适配器
        expandableListViewAdapter = new ExpandableListViewAdapter(this, group, child);
        expandableListView.setAdapter(expandableListViewAdapter);


        //设置孩子的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(ExpandableListViewActivity.this, "_" + group.get(groupPosition) + "_" + child.get(groupPosition).get(childPosition) + "被点击了", Toast.LENGTH_SHORT).show();
                //把位置传入适配器中
                expandableListViewAdapter.isChildSelectable(groupPosition, childPosition);
                //刷新
                expandableListViewAdapter.notifyDataSetChanged();
                String s = child.get(groupPosition).get(childPosition);
                tempType=s;
                //返回true
                return true;
            }
        });


//        // 这里是控制如果列表没有孩子菜单不展开的效果
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent,
                                        View v, int groupPosition, long id) {

                if (child.get(groupPosition).isEmpty()) {// isEmpty没有
                    return true;
                } else {
                    return false;
                }
            }
        });


    }

    private void addInfo(String father, String[] datas) {

        group.add(father);//下装
        //下装--孩子的数据{日常", "泳衣", "汉风", "lolita", "创意T恤}
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < datas.length; i++) {
            list.add(datas[i]);
        }
        child.add(list);
    }


    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "联网失败了" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "GoodsListActivity的数据联网成功了==");
                        processData(response);

                    }
                });
    }

    private void processData(String json) {
        TypeListBean bean = JSON.parseObject(json, TypeListBean.class);
//        Log.e("TAG",bean.getResult().getPage_data().get(0).getName());
        List<TypeListBean.ResultEntity.PageDataEntity> datas = bean.getResult().getPage_data();
        if (datas != null && datas.size() > 0) {
            //有数据-设置适配器

            adapter = new GoodsListAdapter(this, datas);
            recyclerview.setAdapter(adapter);

            //设置布局管理器
            recyclerview.setLayoutManager(new GridLayoutManager(this, 2));

            recyclerview.addItemDecoration(new SpaceItemDecoration(10));


            adapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
                @Override
                public void OnItemClickListener(TypeListBean.ResultEntity.PageDataEntity data) {
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setProduct_id(data.getProduct_id());
                    goodsBean.setName(data.getName());
                    goodsBean.setCover_price(data.getCover_price());
                    goodsBean.setFigure(data.getFigure());

                    Intent intent = new Intent(GoodsListActivity.this, GoodsInfoActivity.class);
                    intent.putExtra(HomeAdapter.GOODS_BEAN, goodsBean);
                    startActivity(intent);
                }
            });

        }


    }


}