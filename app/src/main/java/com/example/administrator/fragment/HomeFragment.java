package com.example.administrator.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.R;
import com.example.administrator.utils.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页fragment
 * @ClassName HomeFragment
 * @Date 2019-05-29 20:36
 **/

public class HomeFragment extends Fragment {
    private int currentItem = 0;
    private ViewPager viewPager;
    private List<String> imageViewList;
    private int[] imageResId;
    private List<Integer> images = new ArrayList<>() ;

    /**
     * 顶部搜索
     */
    private EditText etSearch;
    private ImageButton ibSearch;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化视图
     */
    private void initView(View view) {

        images.add(R.mipmap.livestock1);
        images.add(R.mipmap.livestock2);
        images.add(R.mipmap.livestock3);
        images.add(R.mipmap.livestock4);
        images.add(R.mipmap.livestock5);
        images.add(R.mipmap.livestock6);
        Banner banner = (Banner)view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

}
