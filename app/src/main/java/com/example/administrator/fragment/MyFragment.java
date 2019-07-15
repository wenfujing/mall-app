package com.example.administrator.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.administrator.activity.*;
import com.example.administrator.model.Global;
import com.example.administrator.order.Order_list;

/**
 * @ClassName MyFragment
 * @Date 2019-05-30 11:22
 **/

public class MyFragment extends Fragment {
//    private static final int REQUEST_CODE = 15342;
    private ImageButton imageButton;
    private ImageView igBackground;
    private RelativeLayout layoutCollection;
    private RelativeLayout layoutOrder;
    private RelativeLayout layoutInfo;
    private int[] icon = {R.mipmap.user1,R.mipmap.user1};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        initView(view);
        return view;
    }
    @SuppressLint("ResourceType")
    @Override
    public void onStart() {         //显示界面
        super.onStart();
        if(Global.isLogin){
            System.out.println("123123123123123123123123");
            //更换用户头像
            imageButton.setImageResource(R.mipmap.user2);
            igBackground.setImageResource(R.mipmap.login_bg);
//            Log.i("dingdanxingxi", String.valueOf(icon[0]));
//            imageButton.setImageResource(2131492895);
//2131492895
        }
    }
    public void initView(View view){
        imageButton = (ImageButton)view.findViewById(R.id.my_ib_login);
        layoutOrder = (RelativeLayout)view.findViewById(R.id.my_order);
        layoutInfo = (RelativeLayout)view.findViewById(R.id.my_info);
        igBackground = (ImageView)view.findViewById(R.id.login_bg_bg);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
//                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        layoutCollection = (RelativeLayout)view.findViewById(R.id.my_collection);
        layoutCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
            }
        });
        layoutOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Order_list.class);
                startActivity(intent);
            }
        });
        layoutInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });


    }
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        String titles = ((LoginActivity) activity).getTitles();
//
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == REQUEST_CODE){
//                // 登录成功，取出登录数据
//            }
//        }
//    }
}
