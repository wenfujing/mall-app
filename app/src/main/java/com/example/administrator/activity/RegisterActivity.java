package com.example.administrator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RegisterActivity
 *      注册
 * @Date 2019-06-01 16:52
 **/


public class RegisterActivity extends Activity {
    private EditText etAccount;
    private EditText etPassword;
    private EditText etComPassword;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAnswer;
    private Button btRegister;
    private ImageButton igBack;
    private RadioGroup radioGroup;
    private Spinner spQuestion;
    private String result;
    private String question;
    private int sex = 1;
    List<HashMap<String,Object>> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    public void initView(){
        etAccount = (EditText)findViewById(R.id.register_et_account);
        etPassword = (EditText)findViewById(R.id.register_et_password);
        etComPassword = (EditText)findViewById(R.id.register_et_confirmPassword);
        etEmail = (EditText)findViewById(R.id.register_et_email);
        etPhone = (EditText)findViewById(R.id.register_et_phone);
        etAnswer = (EditText)findViewById(R.id.register_et_answer);
        btRegister = (Button)findViewById(R.id.register_btn_register);
        igBack = (ImageButton)findViewById(R.id.register_btn_back);
        radioGroup = (RadioGroup)findViewById(R.id.register_radioGroup);
        spQuestion = (Spinner)findViewById(R.id.register_sp_question);

        //返回按钮
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //性别选择
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                if(radioButton.getText().toString().equals("男")){
                    sex = 1;
                }else{
                    sex = 0;
                }
            }
        });
        //密保问题
        spQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(RegisterActivity.this,"你选择的是"+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                question = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //注册按钮
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String account = etAccount.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        String comPassword = etComPassword.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String phone = etPhone.getText().toString().trim();
                        String answer = etAnswer.getText().toString().trim();

                        if((account == null || account == "") || (password == null || password == "") || (comPassword == null || comPassword == "") ||
                                (email == null || email == "") || (phone == null || phone == "")){
                            showToast(RegisterActivity.this,"请完善您的信息");
                        }else if(!password.equals(comPassword)){
                            showToast(RegisterActivity.this,"请确认您的密码");
                        }else if (phone.length() != 11){
                            showToast(RegisterActivity.this,"请确认您的手机号码");
                        }else{
                            HttpRequest httpRequest = new HttpRequest();
                            StringBuilder req = new StringBuilder();
                            req.append("username=").append(account)
                                    .append("&password=").append(password)
                                    .append("&email=").append(email)
                                    .append("&sex=").append(sex)
                                    .append("&phone=").append(phone)
                                    .append("&question=").append(question)
                                    .append( "&answer=").append(answer);

                            list = httpRequest.resultJson(req.toString(),"POST","register");
                            if(list != null){
                                Iterator<HashMap<String, Object>> it = list.iterator();
                                while (it.hasNext()) {
                                    Map<String, Object> ma = it.next();
                                   result = String.valueOf(ma.get("status"));

                                }
                                if(result.equals("0")){
//                                    showToast(RegisterActivity.this,"注册成功");
                                    Looper myLooper = Looper.myLooper();
                                    if (myLooper == null) {
                                        Looper.prepare();
                                        myLooper = Looper.myLooper();
                                    }
                                    toast = Toast.makeText(RegisterActivity.this, "注册成功，返回登录界面", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Global.isRegister = true;
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    intent.putExtra("username",account);
                                    startActivity(intent);

                                    if ( myLooper != null) {
                                        Looper.loop();
                                        myLooper.quit();
                                    }
                                }
                                else{
                                    showToast(RegisterActivity.this,"注册失败");
                                }
                            }else{
                                showToast(RegisterActivity.this,"网络错误");
                            }

                        }
                    }
                }).start();

            }
        });
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
