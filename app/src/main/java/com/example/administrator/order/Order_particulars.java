package com.example.administrator.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.activity.R;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.ui.route.RouteActivity;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.administrator.utils.ToastUtil.showToast;

public class Order_particulars extends Activity {

//    Map.Entry<String, Object> m;
    private ExpandableListView elMainOrdercenter;
    private static final int COMPLETED = 0;

    List<HashMap<String,Object>> list = null;
    List<HashMap<String,Object>> list1 = null;
    private String result = null;
    private String orderID = "orderNo";
    private String[] orderNO = new String[100];
    private int i = 0;
    private int pwd;

    private String url;
    private String url2;
    private ImageButton ibBack;
    private Button btn_graph;
    private Button btn_graph1;


    private TextView textView_phone;
    private TextView textView_quantity; //购买数
    private TextView textView_totalPrice; //总价
    private TextView textView_paymentType; //支付方式
    private TextView textView_status; //订单状态
    private TextView textView_productName; //产品名
    private TextView textView_userId; //用户id
    private TextView textView_orderNo; //订单号
    private TextView textView_address; //订单地址
    private ImageView Img_productImage;
    private String ImgUrl;
    private Bitmap bitmap;
    private String quantity;
    private String totalPrice;
    private String paymentType;
    private String status;
    private String productName;
    private String orderNo;
    private String userId;
    private String shippingId;
    private String userNmae; //用户名A
    private String detailedAddress; //详细地址
    private String phone; //电话号码
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        init();
//        btn_graph1 = findViewById(R.id.btn_graph1);
//        btn_graph = findViewById(R.id.btn_graph);
        ibBack = findViewById(R.id.login_ib_back2);

        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        pwd = bundle.getInt("pwd");
        System.out.println("再来一次"+pwd);


        //返回按钮
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //btn_graph 点击跳转温湿度页面
      /*  btn_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开另一个activity，通过意图，意图作用是激活其他组件
                Intent intent = new Intent();
                intent.setClass(Order_particulars.this, Order_graph.class);
                //发送意图.将意图发送给android系统，系统根据意图来激活组件
                startActivity(intent);
            }
        });*/
        //btn_graph1 点击跳转温湿度页面
        /*btn_graph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开另一个activity，通过意图，意图作用是激活其他组件
                Intent intent = new Intent();
                intent.setClass(Order_particulars.this, RouteActivity.class);
                //发送意图.将意图发送给android系统，系统根据意图来激活组件
                startActivity(intent);
            }
        });*/
    }

    public void init()  {

//        dataMap=new HashMap<String, List<Order_info>>();
//        titleArr=new String[]{"商品名："};
//        final List<HashMap<String, Object>>[] list1 = new List[]{new ArrayList<Order_info>()};

        textView_phone = findViewById(R.id.textView_phone);
        textView_quantity = findViewById(R.id.textView_quantity);
        textView_totalPrice = findViewById(R.id.textView_totalPrice);
        textView_paymentType = findViewById(R.id.textView_paymentType);
        textView_status = findViewById(R.id.textView_status);
        textView_productName = findViewById(R.id.textView_productName);
        textView_userId = findViewById(R.id.textView_userId);
        textView_orderNo = findViewById(R.id.textView_orderNo);
        textView_address = findViewById(R.id.textView_address);
        Img_productImage = findViewById(R.id.Img_productImage);


        final Thread  thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpRequest httpRequest = new HttpRequest();
                String request = "?pn="+1+"&status="+0;
                //                                String request = "";
                list = httpRequest.resultJson(request,"GET","showOrder");
                if (list != null){
                    //发送http请求
                    Iterator<HashMap<String, Object>> it = list.iterator();
                    while (it.hasNext()) {
                        Map<String, Object> ma = it.next();
                        result = String.valueOf(ma.get("status"));
                    }
                    int u = 0;
                    for (Map<String, Object> map : list) {
                        for (Map.Entry<String, Object> m : map.entrySet()) {
                            Order_info orderInfo = new Order_info();
                            System.out.print(m.getKey() + "    " + m.getValue());

                            if (u == pwd){
                                if (m.getKey() == "quantity"){
                                    quantity = m.getValue().toString();
                                }else if (m.getKey() == "totalPrice"){
                                    totalPrice = m.getValue().toString();
                                }else if (m.getKey() == "currentUnitPrice"){

                                }else if (m.getKey() == "paymentType"){
                                    paymentType = m.getValue().toString();
                                        paymentType = "线上支付";

                                }else if (m.getKey() == "status"){
                                    status = m.getValue().toString();
                                    //订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
                                    /*if (status.equals("40")){
                                        status = "已发货";
                                    }else if (status.equals("10") ){
                                        status = "未付款";
                                    }else if (status.equals("20")){
                                        status = "已付款";
                                    }else if (status.equals("0")){
                                        status = "已取消";
                                    }else if (status.equals("50")){
                                        status = "交易成功";
                                    }else if (status.equals("60")){
                                        status = "交易关闭";
                                    }*/
                                    status = "已付款";
                                }else if (m.getKey() == "productName"){
                                    productName = m.getValue().toString();
                                }else if (m.getKey() == "userId"){
                                    userId = m.getValue().toString();
                                }else if (m.getKey() == "productId"){

                                }else if (m.getKey() == "productImage"){
                                    ImgUrl = m.getValue().toString();
                                    HttpRequest httpRequest1 = new HttpRequest();
                                    String request1 = ImgUrl;
                                    httpRequest1.resultJson(request1,"GET","image");
                                    bitmap = HttpRequest.bitmap;
                                }else if (m.getKey() == orderID){
                                     orderNo = m.getValue().toString();
                                }else if (m.getKey().equals("shippingId") ){
                                    shippingId = m.getValue().toString();
                                    Log.i("dingdan",shippingId);
                                }

                            }else {
//                                System.out.println("!!!!!!!!");
                            }
                        }
                        u++;
                    }



                }else {
                    showToast(Order_particulars.this, "网络异常");
                }
            }
        });
        Thread  thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 引入t1线程，等待t1线程执行完
                    thread1.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("dingdanxinxi",shippingId);
                if (shippingId != null) {
                    HttpRequest httpRequest2 = new HttpRequest();
                    String request2 = "?shippingId=" + shippingId;
                    System.out.println("request2:"+request2);
                    list1 = httpRequest2.resultJson(request2, "GET", "showShipping1");
                    if (list1 != null) {
                        detailedAddress = list1.get(0).get("receiverProvince").toString()+" "+list1.get(0).get("receiverCity").toString()
                                +" "+list1.get(0).get("receiverDistrict").toString()+" "+list1.get(0).get("receiverAddress").toString();
                        System.out.println(list1.get(0).get("receiverName").toString());
                        userNmae = list1.get(0).get("receiverName").toString();
                        phone = list1.get(0).get("receiverPhone").toString();
                    } else {
                        System.out.println("list1为空");
                    }
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);

                }else {
                    System.out.println("shipping为空");
                }
            }
        });

        thread1.start();
        thread2.start();

    }



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            textView_paymentType.setText(paymentType);
            textView_orderNo.setText(orderNo);
            textView_productName.setText(productName);
            textView_quantity.setText(quantity);
            textView_userId.setText(userNmae);
            textView_totalPrice.setText(totalPrice);
            textView_status.setText(status);
            textView_address.setText(detailedAddress);
            Img_productImage.setImageBitmap(bitmap);
            textView_phone.setText(phone);
            bitmap = null;
        }
    };
}
