package com.example.administrator.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.example.administrator.fragment.*;
import com.example.administrator.utils.FragmentTag;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;


import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
    private ArrayList<ImageButton> imageButtons;
    private FragmentTag fragmentTag;
    private Fragment fragment;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //默认显示首页
        if (savedInstanceState == null ) {
            fragmentTag = FragmentTag.TAG_HOME;
            fragment = new HomeFragment();
            getFragmentManager().beginTransaction().add(R.id.main_fragment, fragment, fragmentTag.getTag()).commit();
            ((TransitionDrawable) imageButtons.get(0).getDrawable())
                    .startTransition(200);
        }
    }
    //初始化视图
    private void initView(){
        //实例化对象
        imageButtons = new ArrayList<ImageButton>();
        //添加选项卡按钮
        imageButtons.add((ImageButton)findViewById(R.id.main_btn_home));
        imageButtons.add((ImageButton)findViewById(R.id.main_btn_category));
        imageButtons.add((ImageButton)findViewById(R.id.main_btn_scan));
        imageButtons.add((ImageButton)findViewById(R.id.main_btn_cart));
        imageButtons.add((ImageButton)findViewById(R.id.main_btn_my));
        for(int i  = 0; i < imageButtons.size(); i++){
            imageButtons.get(i).setOnClickListener(this);
        }
    }
    //监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_home:
                // 切换首页选项卡
                changeToFragment(FragmentTag.TAG_HOME);
                break;
            case R.id.main_btn_category:
                // 切换商品分类选项卡
                changeToFragment(FragmentTag.TAG_CATEGORY);
                break;
            case R.id.main_btn_scan:
                // 切换扫描选项卡
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 111);
                break;
            case R.id.main_btn_cart:
                // 切换购物车选项卡
                changeToFragment(FragmentTag.TAG_CART);
                break;
            case R.id.main_btn_my:
                // 切换我的信息选项卡
                changeToFragment(FragmentTag.TAG_MY);
                break;
        }
    }
    //跳转到
    private void changeToFragment(FragmentTag to){
        if(to != null) {
        }
        if(!fragmentTag.equals(to)){
            //当前fragment
            Fragment currentF = getFragmentManager().findFragmentByTag(fragmentTag.getTag());
            //目标fragment
            Fragment toF = getFragmentManager().findFragmentByTag(to.getTag());
            if(toF == null){
                try {
                    // 未add过，使用反射新建一个Fragment并add到FragmentManager中
                    toF = (Fragment) Class.forName(to.getTag()).newInstance();
                    getFragmentManager().beginTransaction().hide(currentF)
                            .add(R.id.main_fragment, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
                    // 切换按钮动画
                    changeAnimation(to.ordinal());
                    // 更新当前Fragment
                    fragmentTag = to;
                    fragment = toF;
                } catch (Exception e) {

                    throw new RuntimeException("出错了");
                }
            }else{
                getFragmentManager().beginTransaction().hide(currentF).show(toF).commit();
                changeAnimation(to.ordinal());
                fragmentTag = to;
                fragment = toF;
            }
        }
    }
    //按钮切换动画
    private void changeAnimation(int to){
        ((TransitionDrawable) imageButtons.get(fragmentTag.ordinal()).getDrawable())
                .reverseTransition(200);
        ((TransitionDrawable) imageButtons.get(to).getDrawable())
                .startTransition(200);
    }


    //显示当前的fragment
    public Fragment getCurrentFragment(){
        return fragment;
    }
    //扫码
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data != null) {
                //获取扫码得到的数据
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String species = content.substring(1,2);
                String tag = content.substring(2,7);
                //把数据转发到ProductScanInfoActivity
                Intent intent = new Intent(MainActivity.this, ProductScanInfoActivity.class);
                intent.putExtra("species",species);
                intent.putExtra("tag",tag);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        }

    }
}
