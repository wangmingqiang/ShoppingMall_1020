package com.atguigu.shoppingmall_1020;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atguigu.shoppingmall_1020.community.fragment.CommunityFragment;
import com.atguigu.shoppingmall_1020.home.fragment.HomeFragment;
import com.atguigu.shoppingmall_1020.shoppingcart.fragment.ShoppingCartFragment;
import com.atguigu.shoppingmall_1020.type.fragment.TypeFragment;
import com.atguigu.shoppingmall_1020.user.fragment.UserFragemnt;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.fl_main)
    FrameLayout flMain;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private List<Fragment> fragments;
    //fragment中对应的位置
    private int position;
    //刚才被显示的页面
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //初始化视图Fragment
        initFragment();

        //初始化监听
        initListener();

    }
    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        position=0;
                        break;
                    case R.id.rb_type:
                        position=1;
                        break;
                    case R.id.rb_community:
                        position=2;
                        break;
                    case R.id.rb_cart:
                        position=3;
                        break;
                    case R.id.rb_user:
                        position=4;
                        break;
                }

                //根据位置切换到对应的fragment
                Fragment currentFragment=fragments.get(position);

                switchFragment(currentFragment);
            }
        });
        //默认选中的button   一定要放在后面
        rgMain.check(R.id.rb_home);
    }

    private void switchFragment(Fragment currentFragment) {

        //切换的不是同一个页面
        if(tempFragment!=currentFragment) {

            //得到FragmentManager
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //如果没有添加就添加
            if(!currentFragment.isAdded()) {
                //缓存的隐藏
                if(tempFragment!=null) {
                    ft.hide(tempFragment);
                }

                //添加
                ft.add(R.id.fl_main,currentFragment);
            }else {
                //缓存的隐藏
                if(tempFragment!=null) {
                    ft.hide(tempFragment);
                }
                //显示
                ft.show(currentFragment);
            }
            //事物提交
            ft.commit();
            //把当前的赋值成缓存的
            tempFragment = currentFragment;

        }
    }

    private void initFragment() {

        fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new ShoppingCartFragment());
        fragments.add(new UserFragemnt());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id=intent.getIntExtra("checked",R.id.rb_home);
        rgMain.check(id);
    }



    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}
