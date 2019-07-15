package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.order.Order_graph;
import com.example.administrator.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ProductScanInfoActivity
 * @Date 2019-06-23 21:04
 **/

public class ProductScanInfoActivity extends Activity implements View.OnClickListener {
    private TextView tvInfo;
    private ImageButton ibBack;
    private TextView tvAge;
    private TextView tvHealth;
    private TextView tvLabel;
    private TextView tvWeight;
    private TextView tvOrigin;
    private TextView tvTime;
    private TextView tvVaccine;
    private TextView tvMedical;
    private TextView tvFood;
    private Button btnWeb;
    private Button btnWeb2;

    private String species;
    private String tag;
    private String content;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_scan_info);

        initView();
    }
    public void initView(){
        tvInfo = (TextView)findViewById(R.id.product_scan_tv_info);
        ibBack = (ImageButton)findViewById(R.id.product_scan_ib_back);
        tvAge = (TextView)findViewById(R.id.product_scan_tv_age);
        tvHealth = (TextView)findViewById(R.id.product_scan_tv_health);
        tvLabel = (TextView)findViewById(R.id.product_scan_tv_label);
        tvOrigin = (TextView)findViewById(R.id.product_scan_tv_origin);
        tvWeight = (TextView)findViewById(R.id.product_scan_tv_weight);
        tvTime = (TextView)findViewById(R.id.product_scan_tv_time);
        tvVaccine = (TextView)findViewById(R.id.product_scan_tv_vaccine);
        tvMedical = (TextView)findViewById(R.id.product_scan_tv_medical);
        tvFood = (TextView)findViewById(R.id.product_scan_tv_food);
        btnWeb = (Button)findViewById(R.id.product_scan_view);
//        btnWeb2 = (Button)findViewById(R.id.product_scan_view2);

        ibBack.setOnClickListener(this);
        btnWeb.setOnClickListener(this);
//        btnWeb2.setOnClickListener(this);

        species = getIntent().getStringExtra("species");
        tag = getIntent().getStringExtra("tag");
        content = getIntent().getStringExtra("content");
        System.out.println(content);
        //通过服务器获取数据
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest();
                String request = "?label=" + tag + "&va=" + species;
                System.out.println(content);
                Global.list_scan = httpRequest.resultJson(request,"GET","scan");
                if(Global.list_scan != null){
                    if(Global.list_scan.get(0).get("status").toString().equals("0")){
                        handler.post(runnableUi);
                    }else if(Global.list_scan.get(0).get("status").toString().equals("1")){
                        ToastUtil.showToast(ProductScanInfoActivity.this,"传了错误的参数，换个码");
                    }
                }else {
                    ToastUtil.showToast(ProductScanInfoActivity.this,"匹配不了....");
                }


            }
        }).start();

        tvInfo.setText(content);

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.product_scan_ib_back:
                finish();
                break;
            case R.id.product_scan_view:
                Intent intent = new Intent(ProductScanInfoActivity.this,WebViewActivity.class);
                startActivity(intent);
                break;
//            case R.id.product_scan_view2:
//                Intent intent1 = new Intent(ProductScanInfoActivity.this, Order_graph.class);
//                startActivity(intent1);
//                break;

        }
    }

    Runnable   runnableUi = new  Runnable(){
        @Override
        public void run() {
            //更新界面
            tvInfo.setText(Global.list_scan.get(0).get("varieties").toString());
            tvLabel.setText(Global.list_scan.get(0).get("label").toString());
            tvWeight.setText(Global.list_scan.get(0).get("weight").toString() + " kg");
            tvHealth.setText(Global.list_scan.get(0).get("health").toString());
            tvOrigin.setText(Global.list_scan.get(0).get("origin").toString());
            tvAge.setText(Global.list_scan.get(0).get("age").toString() + " 天");
            tvFood.setText(Global.list_scan.get(0).get("stapleFood").toString());
            tvVaccine.setText(Global.list_scan.get(0).get("vaccine").toString());
            tvMedical.setText(Global.list_scan.get(0).get("medicalRecord").toString());
            String time = Global.list_scan.get(0).get("createTime").toString();
            tvTime.setText(stampToDate(time));
        }
    };
    //转换时间格式
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
