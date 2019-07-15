package com.example.administrator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.administrator.fragment.MyFragment;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginActivity
 *      登录
 * @Date 2019-06-01 11:04
 **/

public class LoginActivity extends Activity {
    private Button btn_login;
    private Button btn_register;
    private ImageButton ibBack;
    private EditText etAccoutnt;
    private EditText etPassword;
    private String result = null;
    List<HashMap<String,Object>> list = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView(){
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_register = (Button)findViewById(R.id.login_btn_register);
        ibBack = (ImageButton)findViewById(R.id.login_ib_back);
        etAccoutnt = (EditText)findViewById(R.id.login_et_account);         //获取账户
        etPassword = (EditText)findViewById(R.id.login_et_password);        //获取密码
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //返回按钮
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //登录按钮
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        String account = etAccoutnt.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();


                        if("".equals(account) || "".equals(password)){
                            showToast(LoginActivity.this,"用户名或密码不能为空");
                        }else {
                            HttpRequest httpRequest = new HttpRequest();
                            String request = "username="+account+"&password="+password;
                            list = httpRequest.resultJson(request,"POST","login");
                            if (list != null) {
                                //解析数据
                                Iterator<HashMap<String, Object>> it = list.iterator();
                                while (it.hasNext()) {
                                    Map<String, Object> ma = it.next();
                                    result = String.valueOf(ma.get("status"));
                                }
                                //若结果返回0，则表示登录成功
                                if (result.equals("0")) {
                                    Looper myLooper = Looper.myLooper();
                                    if (myLooper == null) {
                                        Looper.prepare();
                                        myLooper = Looper.myLooper();
                                    }
                                    toast = Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Global.isLogin = true;
                                    Global.username = account;
                                    if(Global.isRegister == true){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                    finish();
                                    if (myLooper != null) {
                                        Looper.loop();
                                        myLooper.quit();
                                    }
                                } else {
                                    showToast(LoginActivity.this, "登录失败，用户名或密码错误");
                                }
                            } else {
                                showToast(LoginActivity.this, "网络异常");
                            }
                        }
                    }
                }).start();
            }
        });
        //获取注册成功的用户名
        Intent intent = getIntent();
        String account = intent.getStringExtra("username");
        etAccoutnt.setText(account);
    }

    /**
     * 在线程中正常使用吐司
    **/
    private static Toast toast = null;
    public static void showToast(Context context, String text) {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            Looper.prepare();
            myLooper = Looper.myLooper();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
        if ( myLooper != null) {
            Looper.loop();
            myLooper.quit();
        }

    }

}
