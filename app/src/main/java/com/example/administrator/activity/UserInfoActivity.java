package com.example.administrator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.administrator.utils.ToastUtil.showToast;

/**
 * @ClassName UserInfoActivity
 * @Date 2019-06-12 11:24
 **/

public class UserInfoActivity extends Activity {
    private ImageButton igBack;
    private TextView tvAccount;
    private TextView tvSex;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvRole;
    List<HashMap<String,Object>> list = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }
    public void initView(){
        igBack = (ImageButton)findViewById(R.id.user_info_ib_back);
        tvAccount = (TextView)findViewById(R.id.user_info_tv_account);
        tvSex = (TextView)findViewById(R.id.user_info_tv_sex);
        tvEmail = (TextView)findViewById(R.id.user_info_tv_email);
        tvPhone = (TextView)findViewById(R.id.user_info_tv_phone);
        tvRole = (TextView)findViewById(R.id.user_info_tv_role);
        //返回按钮
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String username = Global.username;
                HttpRequest httpRequest = new HttpRequest();
                String request = "";
                list = httpRequest.resultJson(request,"GET","getUserInfo");
                if(list != null){
                    if(list.get(0).get("status").toString().equals("0")){
                        tvAccount.setText(list.get(0).get("username").toString());
                        String sex = "";
                        sex = list.get(0).get("sex").toString();
                        if(sex.equals("1")){
                            tvSex.setText("男");
                        }else{
                            tvSex.setText("女");
                        }
                        if(list.get(0).get("role").toString().equals("0")){
                            tvRole.setText("管理员");
                        }else if(list.get(0).get("role").toString().equals("1")){
                            tvRole.setText("商家");
                        }else if(list.get(0).get("role").toString().equals("2")){
                            tvRole.setText("普通用户");
                        }

                        tvEmail.setText(list.get(0).get("email").toString());
                        tvPhone.setText(list.get(0).get("phone").toString());
                    }else if(list.get(0).get("status").toString().equals("10")){
                        showToast(UserInfoActivity.this,"请先登录");
                    }
                }else{
                    showToast(UserInfoActivity.this,"网络错误");
                }
            }
        }).start();
    }
}
