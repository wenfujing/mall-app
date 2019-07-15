package com.example.administrator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

/**
 * @ClassName MyOrderActivity
 * @Date 2019-06-10 16:40
 **/

public class MyOrderActivity extends Activity {
    public ImageButton igBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
    }
    public void initView(){
        igBack = (ImageButton)findViewById(R.id.my_order_btn_back);
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
