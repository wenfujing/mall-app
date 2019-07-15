package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import static com.example.administrator.utils.ToastUtil.showToast;

/**
 * @ClassName OrderActivity
 * @Date 2019-06-17 9:51
 **/

public class OrderActivity extends Activity {
    private TextView tvAddress;
    private LinearLayout lyAddress;
    private Button btnCommit;
    private Button btnAdd;
    private Button btnSub;
    private TextView tvNum;
    private TextView tvPrice;
    private TextView tvCost;
    private TextView tvTotalCost;
    private TextView tvName;
    private TextView tvReceiverName;
    private TextView tvPhone;
    private Spinner spinner;
    private ImageButton igBack;
    private ImageView ivProduct;
    private String mainImage = null;
    private Handler handler;
    private String productId;
    private String name;
    private String price;
    private String stock;
    private String detail;
    private int quantity = 1;
    private int addressId = 1;
    List<HashMap<String,Object>> orderList = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
    }
    public void initView(){
        lyAddress = (LinearLayout)findViewById(R.id.order_layout_address);
        btnCommit = (Button)findViewById(R.id.order_commit);
        btnAdd = (Button)findViewById(R.id.order_btn_add);
        btnSub = (Button)findViewById(R.id.order_btn_sub);
        tvNum = (TextView)findViewById(R.id.order_tv_num);
        tvCost = (TextView)findViewById(R.id.order_tv_cost);
        tvPrice = (TextView)findViewById(R.id.order_tv_price);
        tvTotalCost = (TextView)findViewById(R.id.order_total_cost);
        tvName = (TextView)findViewById(R.id.order_tv_name);
        igBack = (ImageButton)findViewById(R.id.order_btn_back);
        ivProduct = (ImageView)findViewById(R.id.order_ig);
        tvReceiverName = (TextView)findViewById(R.id.order_tv_receiverName);
        tvPhone = (TextView)findViewById(R.id.order_tv_phone);
        spinner = (Spinner)findViewById(R.id.order_sp);

        //获取订单数据
        Intent intent = getIntent();
        productId = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        mainImage = intent.getStringExtra("mainImage");

        tvName.setText(name);
        tvPrice.setText(price);
        tvCost.setText(price);
        tvTotalCost.setText(price);
        //获取网络图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpRequest httpRequest = new HttpRequest();
                httpRequest.resultJson(mainImage,"GET","image");
                Message msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }).start();

        //获取地址信息
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
                    Global.shippingId = Integer.parseInt(Global.list_shipping.get(0).get("id").toString());
                    handler.post(runnableUi);
                }
            }
        }).start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tvReceiverName.setText(Global.list_shipping.get(position).get("name").toString());
                tvPhone.setText(Global.list_shipping.get(position).get("receiverPhone").toString());
                Global.shippingId = Integer.parseInt(Global.list_shipping.get(position).get("id").toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * @Description //TOOD 返回按钮
        **/
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * @Description //TOOD 增加商品数量
        **/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = quantity + 1;
                tvNum.setText(String.valueOf(quantity));
//                int cost = Integer.parseInt(tvPrice.getText().toString().trim()) * num;
                double cost = Double.parseDouble(tvPrice.getText().toString().trim()) * quantity;
                tvCost.setText(String.valueOf(cost));
                tvTotalCost.setText(String.valueOf(cost));
            }
        });
        /**
         * @Description //TOOD 减少商品数量
         **/
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantity >= 2){
                    quantity = quantity - 1;
                    tvNum.setText(String.valueOf(quantity));
                    double cost = Double.parseDouble(tvPrice.getText().toString().trim()) * quantity;
                    tvCost.setText(String.valueOf(cost));
                    tvTotalCost.setText(String.valueOf(cost));
                }else{
                    Toast.makeText(OrderActivity.this,"数量不能小于1",Toast.LENGTH_LONG).show();
                }

            }
        });
        /**
         * @Description //TOOD 提交订单
        **/
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest();
                        String request = null;
                        request = "?productId="+productId+"&quantity="+quantity+"&shippingId="+Global.shippingId;
                        Log.i("dingdanxinix", String.valueOf(Global.shippingId));
                        orderList = httpRequest.resultJson(request,"POST","createOrder");
                        if(orderList != null){
                            if(orderList.get(0).get("status").toString().equals("0")){
                                showToast(OrderActivity.this, "生成订单成功");
                            }else{
                                showToast(OrderActivity.this, "生成订单失败");
                            }
                        }else{
                            showToast(OrderActivity.this,"网络异常");
                        }

                    }
                }).start();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(HttpRequest.bitmap != null){
                    ivProduct.setImageBitmap(HttpRequest.bitmap);
                }
                super.handleMessage(msg);
            }
        };

    }

    Runnable   runnableUi = new  Runnable(){
        @Override
        public void run() {
            //更新界面
            String [] from ={"receiverAddress"};
            int [] to = {R.id.item_spin_address};
            SimpleAdapter simpleAdapter = new SimpleAdapter(OrderActivity.this, Global.list_shipping,R.layout.item_spin_address,from,to);
            spinner.setAdapter(simpleAdapter);
        }
    };


}
