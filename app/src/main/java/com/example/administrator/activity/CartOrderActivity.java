package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.utils.ToastUtil.showToast;

/**
 * @ClassName CartOrderActivity
 * @Date 2019-06-19 15:42
 **/

public class CartOrderActivity extends Activity implements View.OnClickListener {
    private LinearLayout lyAddress;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvName;
    private ImageButton igBack;
    private ListView listView;
    private ListView lvAddress;
    private GridView gridView;
    private Button btnCommit;
    private TextView tvTotalCost;
    private Button btnEdit;
    private Spinner spinner;
    private List<HashMap<String,Object>> list_check = new ArrayList<HashMap<String,Object>>();
    private double totalCost;
    private Handler handler;
    private boolean select = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        initview();
    }
    public void initview(){
//        lyAddress = (LinearLayout)findViewById(R.id.cart_order_layout_address);
//        tvAddress = (TextView)findViewById(R.id.cart_order_address);
        igBack = (ImageButton)findViewById(R.id.cart_order_btn_back);
        listView = (ListView)findViewById(R.id.cart_order_listview);
        btnCommit = (Button)findViewById(R.id.cart_order_commit);
        tvTotalCost = (TextView)findViewById(R.id.cart_order_total_cost);
//        btnEdit = (Button)findViewById(R.id.address_btn_edit);
        spinner = (Spinner)findViewById(R.id.cart_order_sp);
        tvName = (TextView)findViewById(R.id.cart_order_tv_name);
        tvPhone = (TextView)findViewById(R.id.cart_order_tv_phone);


//        lvAddress = (ListView)findViewById(R.id.cart_order_lv_address);
//        gridView = (GridView)findViewById(R.id.cart_order_gridview);
        igBack.setOnClickListener(this);
        //更改订单地址
//        btnEdit.setOnClickListener(this);

        String [] from ={"image","name","price","cost","quantity"};
        int [] to = {R.id.cart_order_ig,R.id.cart_order_tv_name,R.id.cart_order_tv_price,R.id.cart_order_tv_cost,R.id.cart_order_tv_num};
        SimpleAdapter simpleAdapter = new SimpleAdapter(CartOrderActivity.this,Global.list_data,R.layout.item_cart_order_lv,from,to);
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if((view instanceof ImageView )&& (data instanceof Bitmap)){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }

        });
        listView.setAdapter(simpleAdapter);

        for(int i = 0; i < Global.list_data.size(); i++){
            totalCost = totalCost + Double.parseDouble(Global.list_data.get(i).get("cost").toString());
        }
        tvTotalCost.setText(String.valueOf(totalCost));

        //提交按钮
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<HashMap<String,Object>> list_result = null;
                        HttpRequest httpRequest = new HttpRequest();
                        String request = "?shippingId=" + Global.shippingId;

                        list_result = httpRequest.resultJson(request,"POST","createCartOrder");
                        if(list_result != null){
                            if(list_result.get(0).get("status").toString().equals("0")){
                                showToast(CartOrderActivity.this,"下单成功");
                            }else{
                                showToast(CartOrderActivity.this,"下单失败");
                            }
                        }else{
                            showToast(CartOrderActivity.this,"网络异常");
                        }

                    }
                }).start();
            }
        });
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Global.list_shipping != null){
                    Global.list_shipping.clear();
                }
                HttpRequest httpRequest = new HttpRequest();
                String request = "?pn=1";
                Global.list_shipping = httpRequest.resultJson(request,"GET","showShipping");
                if(Global.list_shipping != null){
                    handler.post(runnableUi);
                }else{
                    showToast(CartOrderActivity.this,"网络异常");
                }

            }
        }).start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                    tvAddress.setText(Global.list_shipping.get(position).get("receiverAddress").toString());
                tvName.setText(Global.list_shipping.get(position).get("name").toString());
                tvPhone.setText(Global.list_shipping.get(position).get("receiverPhone").toString());
                Global.shippingId = Integer.parseInt(Global.list_shipping.get(position).get("id").toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cart_order_btn_back:
                finish();
                break;
        }
    }

    Runnable   runnableUi = new  Runnable(){
        @Override
        public void run() {
            //更新界面
            String [] from ={"receiverAddress"};
            int [] to = {R.id.item_spin_address};
            SimpleAdapter simpleAdapter = new SimpleAdapter(CartOrderActivity.this,Global.list_shipping,R.layout.item_spin_address,from,to);
            spinner.setAdapter(simpleAdapter);
        }
    };

}
