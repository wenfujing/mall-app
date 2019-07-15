package com.example.administrator.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.*;
import com.example.administrator.adapter.CartAdapter;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CartFragment
 *      购物车
 * @Date 2019-05-30 11:22
 **/

public class CartFragment extends Fragment {
    private Button btnEdit;                         //顶部编辑按钮
    private Button btnLogin;                        //提示登录
    private RelativeLayout suggestLayout;           //登录后要隐藏的
    private TextView tvEmpty;
    private ProductDetailsActivity productDetailsActivity;
    private int id;
    private List<Map<String,Object>> list_data = new ArrayList<Map<String,Object>>();
//    private LocalReceiver mReceiver;
    private int flag = 1;
    private CartAdapter cartAdapter;
    public static int listSize = 0;

    private List<HashMap<String,Object>> cartList = null;
    private Handler handler;

    /**
     *  购物车列表
     **/
    private LinearLayout listLayout;
    private ListView listView;
    private Button btnCartAdd;
    private Button btnCartSub;
    private TextView tvNum;
    /**
     *  底部付款
     **/
    private RelativeLayout checkLayout;
    private CheckBox cbCheckAll;
    private TextView tvTotal;
    private Button btnBuy;
    private int[] icon = {R.mipmap.category_zangzhu,R.mipmap.category_zangzhu,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        listView = (ListView)view.findViewById(R.id.cart_lv);
        initView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {         //显示界面
        super.onStart();
        if(Global.isLogin){
            //已登录
            //隐藏登录界面
            suggestLayout.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            checkLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

            handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //获取数据
                    HttpRequest httpRequest = new HttpRequest();
                    String request = "?pn=1";
                    Bitmap bitmap = null;
                    if(Global.isLogin){

                        cartList = httpRequest.resultJson(request,"GET","showCart");

                        if(cartList != null){
                            for(int i = 0; i < cartList.size(); i++){
                                request = cartList.get(i).get("mainImage").toString();
                                httpRequest.resultJson(request,"GET","image");
                                cartList.get(i).put("image",HttpRequest.bitmap);

                            }
                        }


                        //获取总价
                        List<HashMap<String,Object>> listResult1 = null;
                        HttpRequest httpRequest1 = new HttpRequest();
                        String request1 = "";
                        System.out.println(request);
                        listResult1 = httpRequest1.resultJson(request1,"GET","getPrice");
                        if(listResult1 != null){
                            Global.totalPrice = listResult1.get(0).get("msg").toString();

                            cartAdapter = new CartAdapter(getActivity(),cartList);
                            handler.post(runnableUiProduct);
                        }


                        //编辑按钮监听
                        btnEdit.setOnClickListener(new EditListener());


                        //     监听价格 监听复选框
                        cartAdapter.setOnPriceChangedListener(new PricechangeListener());

                        //正常结算
                        btnBuy.setOnClickListener(new NormalBuyListener());

                        //全选
                        cbCheckAll.setOnCheckedChangeListener(new CheckedListener());
                    }

                }
            }).start();


        }else{
            //未登录
            //隐藏编辑按钮
            btnEdit.setVisibility(View.GONE);
            //显示提示
            suggestLayout.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
        }


    }

    public void initView(View view){
        btnEdit = (Button)view.findViewById(R.id.cart_btn_edit);
        cbCheckAll = (CheckBox) view.findViewById(R.id.cart_cb_all);
        tvTotal = (TextView)view.findViewById(R.id.cart_tv_total);
        btnBuy = (Button)view.findViewById(R.id.cart_btn_buy);
        btnCartAdd = (Button)view.findViewById(R.id.item_cart_btn_add);
        btnCartSub = (Button)view.findViewById(R.id.item_cart_btn_sub);
        tvNum = (TextView)view.findViewById(R.id.item_cart_tv_num);
        suggestLayout = (RelativeLayout)view.findViewById(R.id.cart_suggest_layout);
        tvEmpty = (TextView)view.findViewById(R.id.cart_tv_empty);
        listLayout = (LinearLayout)view.findViewById(R.id.cart_list_layout);
        checkLayout = (RelativeLayout)view.findViewById(R.id.cart_check_layout);
        btnLogin = (Button)view.findViewById(R.id.cart_btn_login);


        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                onDestroyView();
                startActivity(intent);
            }
        });

        /**
         * @Description //TOOD 从服务器获取数据
         **/
//        handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpRequest httpRequest = new HttpRequest();
//                String request = "?pn=1";
//                Bitmap bitmap = null;
//                if(Global.isLogin){
//                    System.out.println("111111111111111111111111111111111111");
//                    cartList = httpRequest.resultJson(request,"GET","showCart");
//                    for(int i = 0; i < cartList.size(); i++){
//                        request = cartList.get(i).get("mainImage").toString();
//                        httpRequest.resultJson(request,"GET","image");
//                        cartList.get(i).put("image",HttpRequest.bitmap);
//                    }
//                    cartAdapter = new CartAdapter(getActivity(),cartList);
//                    handler.post(runnableUiProduct);
//
//                    //编辑按钮监听
//                    btnEdit.setOnClickListener(new EditListener());
//                    //     监听价格 监听复选框
//                    cartAdapter.setOnPriceChangedListener(new PricechangeListener());
//
//                    //全选
//                    cbCheckAll.setOnCheckedChangeListener(new CheckedListener());
//                }
//
//            }
//        }).start();




        /*getData();
        cartAdapter = new CartAdapter(getActivity(),list_data);
        listView.setAdapter(cartAdapter);*/


//        btnCartAdd.setOnClickListener(new AddListener());
//        listView.setOnClickListener();



//        String [] from ={"item_cart_iv"};
//        int [] to = {R.id.item_cart_iv};
//        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list_data,R.layout.item_cart_lv,from,to);
//        listView.setAdapter(simpleAdapter);
    }
    //更新界面
    Runnable runnableUiProduct = new Runnable() {
        @Override
        public void run() {
            //更新种类界面

            listView.setAdapter(cartAdapter);
        }
    };
    //编辑按钮
    class EditListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //编辑按钮
            if(btnEdit.getText().toString().equals("编辑")){
                //切换成完成按钮
                btnEdit.setText("完成");
                //取消全选
                cbCheckAll.setChecked(false);
                //隐藏总价格
                tvTotal.setVisibility(View.INVISIBLE);
                //切换为删除按钮
                btnBuy.setText("删除");
                btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int i;
                        for(i = 0; i < cartList.size(); i++){

                            if(cartAdapter.cbs[i] == null){
                            }
                            if(cartAdapter.cbs[i].isChecked()){
//                                cartList.remove(i);
//                                cartAdapter.cbs[i].setChecked(false);
////                                    System.out.println(list_data.get(i).get("item_cart_price"));
////                                    getData();
////                                    cartAdapter = new CartAdapter(getActivity(),list_data);
////                                    listView.setAdapter(cartAdapter);
//                                cartAdapter.notifyDataSetInvalidated();
                                break;
                            }
                        }
                        final int j = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpRequest httpRequest = new HttpRequest();
                                System.out.println(cartList.get(j).get("id").toString());
                                String request = "?cartId=" + cartList.get(j).get("id").toString();
                                httpRequest.resultJson(request,"POST","deleteCart");
//                                cartAdapter.notifyDataSetInvalidated();


                                cartList.remove(j);

                                cartAdapter = new CartAdapter(getActivity(),cartList);
                                handler.post(runnableUiProduct);
                            }
                        }).start();

                    }
                });
            }else{
                btnEdit.setText("编辑");
                // 全部选中
                cbCheckAll.setChecked(true);
                // 显示价格总数文本框
                tvTotal.setVisibility(View.VISIBLE);
                // 将删除按钮改成结算按钮
                btnBuy.setText("结算");
                btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<HashMap<String,Object>> list_check = new ArrayList<HashMap<String,Object>>();
                        int count = 0;
                        for(int i = 0; i < cartList.size(); i++){
                            if (cartAdapter.cbs[i].isChecked()){
                                count = count + 1;
                                list_check.add(cartList.get(i));

                                for(int j = 0; j < list_check.size(); j++){
                                    if(Global.bitmaps[j] == null){
                                        Log.i("dingdanxinxi","11111111111111111111");
                                    }
                                    double cost = Double.parseDouble(list_check.get(j).get("price").toString()) * Double.parseDouble(list_check.get(j).get("quantity").toString());
                                    list_check.get(j).put("cost",cost);
                                    Global.list_data = list_check;
                                }

                            }
                        }
                        if(count > 0){
                            Intent intent = new Intent(getActivity(), CartOrderActivity.class);

//                intent.putExtra("list_check",list_check);
                            System.out.println("1111111111111111111111111111111111111111111111111");
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(),"请先选择商品",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    //正常结算
    class NormalBuyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ArrayList<HashMap<String,Object>> list_check = new ArrayList<HashMap<String,Object>>();
            int count = 0;
            for(int i = 0; i < cartList.size(); i++){
                if (cartAdapter.cbs[i].isChecked()){
                    count = count + 1;
                    list_check.add(cartList.get(i));

                    for(int j = 0; j < list_check.size(); j++){
                        if(Global.bitmaps[j] == null){
                            Log.i("dingdanxinxi","11111111111111111111");
                        }
//                        Global.bitmaps[j] = (Bitmap)list_check.get(j).get("image");
//                        list_check.get(j).put("image","1");
//                        System.out.println(Double.parseDouble(list_check.get(j).get("price").toString()));
//                        System.out.println(Double.parseDouble(list_check.get(j).get("quantity").toString()));
                        double cost = Double.parseDouble(list_check.get(j).get("price").toString()) * Double.parseDouble(list_check.get(j).get("quantity").toString());
                        list_check.get(j).put("cost",cost);
                        Global.list_data = list_check;
                    }

                }
            }
            if(count > 0){
                Intent intent = new Intent(getActivity(), CartOrderActivity.class);

//                intent.putExtra("list_check",list_check);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(),"请先选择商品",Toast.LENGTH_LONG).show();
            }
        }
    }

    //价格监听
    class PricechangeListener implements CartAdapter.OnPriceChangedListener{
        @Override
        public void onPriceChanged(final int price) {
            tvTotal.post(new Runnable() {
                @Override
                public void run() {
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<HashMap<String,Object>> listResult1 = null;
                            HttpRequest httpRequest1 = new HttpRequest();
                            String request1 = "";
                            listResult1 = httpRequest1.resultJson(request1,"GET","getPrice");
                            Global.totalPrice = listResult1.get(0).get("msg").toString();
                        }
                    }).start();*/


                    tvTotal.setText("合计：￥" + Global.totalPrice);
                    int count = 0;
                    for(int i = 0; i < cartList.size(); i++){
                        if(cartAdapter.cbs[i].isChecked()){
                            count = count + 1;
                        }
                    }
                    if(count == cartList.size()){
                        cbCheckAll.setChecked(true);
                    }else{
                        cbCheckAll.setChecked(false);
                    }
                }
            });
        }
    }

    //全选
    class CheckedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            HttpRequest httpRequest = new HttpRequest();
            String request = "?checked=" + cbCheckAll.isChecked();
            httpRequest.resultJson(request,"POST","checkAll");
            if(cbCheckAll.isChecked()){
                for(int i = 0; i < cartList.size(); i++){
                    cartAdapter.cbs[i].setChecked(true);
                }
            }else{
                int i;
                int count = 0;
                for(i = 0; i < cartList.size(); i++){
                    if(cartAdapter.cbs[i].isChecked()){
                        count = count + 1;
                    }
                }
                if (count == cartList.size()){
                    for(i = 0; i < cartList.size(); i++){
                        cartAdapter.cbs[i].setChecked(false);
                    }
                }

            }
        }
    }

}
