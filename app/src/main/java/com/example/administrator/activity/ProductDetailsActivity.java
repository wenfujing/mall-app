package com.example.administrator.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.utils.ToastUtil;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName ProductDetailsActivity
 * @Date 2019-06-04 19:49
 **/

public class ProductDetailsActivity extends Activity {
    private TextView tvDescribe;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvStock;
    private ImageButton ibBack;
    private ImageButton ibCollection;
    private Button btnCart;
    private Button btnBuy;
    private ImageView igProduct;
    private String mainImage = null;
    private Handler handler;
    private String id;
    private String name;
    private String price;
    private String stock;
    private String detail;

    private List<HashMap<String,Object>> addCollectionList;
    private List<HashMap<String,Object>> addCartList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initView();
    }
    @SuppressLint("HandlerLeak")
    public void initView(){
//        goodsDao = new GoodsDao(ProductDetailsActivity.this);
        tvDescribe = (TextView)findViewById(R.id.product_tv_describe);
        tvName = (TextView)findViewById(R.id.product_tv_relname);
        tvPrice = (TextView)findViewById(R.id.product_tv_price1);
        tvStock = (TextView)findViewById(R.id.product_tv_stock);
        ibCollection = (ImageButton)findViewById(R.id.product_btn_collection);
        btnCart = (Button)findViewById(R.id.product_btn_cart);
        ibBack = (ImageButton)findViewById(R.id.product_btn_back);
        ibCollection = (ImageButton)findViewById(R.id.product_btn_collection);
        btnBuy = (Button)findViewById(R.id.product_btn_buy);
        igProduct = (ImageView)findViewById(R.id.product_iv);


        /**
         * @Description //TOOD 获取商品数据
        **/
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
//        BigDecimal price = new BigDecimal(intent.getDoubleExtra("price", 0));
        price = intent.getStringExtra("price");
        stock = intent.getStringExtra("stock");
        detail = intent.getStringExtra("detail");
        mainImage = intent.getStringExtra("mainImage");

        Log.i("dingdanxinxi",mainImage);

        tvDescribe.setText(detail);
        tvPrice.setText(price.toString());
        tvStock.setText("剩余： " + stock + " 份");
        tvName.setText(name);
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

        //购买
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isLogin){
                    Intent intent = new Intent(ProductDetailsActivity.this,OrderActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("price",price);
                    intent.putExtra("mainImage",mainImage);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProductDetailsActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(ProductDetailsActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }

            }
        });

        //返回
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加收藏
        ibCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isLogin){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(addCollectionList != null){
                                addCollectionList.clear();
                            }
                            HttpRequest httpRequest = new HttpRequest();
                            String request = "?productId="+id;
                            addCollectionList = httpRequest.resultJson(request,"POST","addCollection");
                            if(addCollectionList != null){
                                if(addCollectionList.get(0).get("status").toString().equals("0")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"添加收藏成功");
                                }else if(addCollectionList.get(0).get("status").toString().equals("10")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"请先登录，再收藏商品");
                                }else if(addCollectionList.get(0).get("status").toString().equals("1")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"您已收藏该商品");
                                }else {
                                    ToastUtil.showToast(ProductDetailsActivity.this,"添加收藏失败");
                                }
                            }else{
                                ToastUtil.showToast(ProductDetailsActivity.this,"网络连接失败，请检查网络");
                            }

                        }
                    }).start();
                }else{
                    Toast.makeText(ProductDetailsActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(ProductDetailsActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }

            }
        });

        //加入购物车
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isLogin){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(addCartList != null){
                                addCartList.clear();
                            }
                            HttpRequest httpRequest = new HttpRequest();
                            String request = "?quantity=1&productId="+id;
                            addCartList = httpRequest.resultJson(request,"POST","addCart");
                            if(addCartList != null){
                                if(addCartList.get(0).get("status").toString().equals("0")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"加入购物车成功");
                                }else if(addCartList.get(0).get("status").toString().equals("10")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"请先登录，再加入购物车");
                                }else if(addCartList.get(0).get("status").toString().equals("1")){
                                    ToastUtil.showToast(ProductDetailsActivity.this,"该商品已加入购物车");
                                }else {
                                    ToastUtil.showToast(ProductDetailsActivity.this,"加入购物车失败");
                                }
                            }else{
                                ToastUtil.showToast(ProductDetailsActivity.this,"网络连接失败，请检查网络");
                            }

                        }
                    }).start();
                }else {
                    Toast.makeText(ProductDetailsActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(ProductDetailsActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }

            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(HttpRequest.bitmap != null){
                    igProduct.setImageBitmap(HttpRequest.bitmap);
                }
                super.handleMessage(msg);
            }
        };
    }

}





