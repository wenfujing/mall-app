package com.example.administrator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.R;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

/**
 * @ClassName CartAdapter
 * @Date 2019-06-17 21:14
 **/

public class CartAdapter extends BaseAdapter {
    private View view;
    private TextView tvNum;
    private Context CartContext;
    private ViewHolder viewHolder = null;
    private List<HashMap<String,Object>> cartList = new ArrayList<HashMap<String,Object>>();;
    private List<Boolean> mChecked;
    private int i = 0;
    public OnPriceChangedListener onPriceChangedListener;
    private TextView[] tvs = new TextView[100];
    public CheckBox[] cbs = new CheckBox[100];
    private TextView[] prices = new TextView[100];
    public int price = 0;
    private Handler handler = null;

    public CartAdapter(Context context,List<HashMap<String,Object>> list){
        CartContext = context;
        cartList = list;
    }
    @Override
    public int getCount() {
        if(cartList != null){
            return cartList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CartContext).inflate(R.layout.item_cart_lv, null);
            view = convertView;
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.item_cart_cb);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_cart_iv);
            viewHolder.tvInfo = (TextView) convertView.findViewById(R.id.item_cart_showInfo);
            viewHolder.btnAdd = (Button)convertView.findViewById(R.id.item_cart_btn_add);
            viewHolder.btnSub = (Button)convertView.findViewById(R.id.item_cart_btn_sub);
            viewHolder.tvNum = (TextView)convertView.findViewById(R.id.item_cart_tv_num);
            viewHolder.tvPrice = (TextView)convertView.findViewById(R.id.item_cart_price);
            convertView.setTag(viewHolder);
            viewHolder.checkBox.setTag(position);
            viewHolder.checkBox.setChecked(false);
            viewHolder.btnAdd.setTag(position);
            viewHolder.btnSub.setTag(position);
            viewHolder.tvPrice.setTag(position);
//            viewHolder.imageView.setTag(position);
            tvs[i] = viewHolder.tvNum;
            prices[i] = viewHolder.tvPrice;
            cbs[i] = viewHolder.checkBox;
            if(cartList.get(position).get("checked").toString().equals("1")){
                cbs[i].setChecked(true);
            }
            if(cbs[i] == null){
                System.out.println("12312312312312312312312312");
            }
            prices[i] = viewHolder.tvPrice;
            i++;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //适配数据
//        if(viewHolder.tvInfo == null){
//            Log.i("dingdanxinxi","111111111111111");
//        }
//        viewHolder.tvPrice.setText(CartList.get(position).get("item_cart_price").toString());
//        Log.i("dingdanliebiao", String.valueOf(Integer.parseInt(String.valueOf(CartList.get(position).get("item_cart_iv")))));
        viewHolder.imageView.setImageBitmap((Bitmap)cartList.get(position).get("image"));
        viewHolder.tvInfo.setText(cartList.get(position).get("name").toString());
        viewHolder.tvPrice.setText(cartList.get(position).get("price").toString());
        viewHolder.tvNum.setText(cartList.get(position).get("quantity").toString());

//        Log.i("cartListSize",String.valueOf(cartList.size()));
        //复选框

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //1 已勾选  0 未勾选
                        if(cartList.get(position).get("checked").toString().equals("0")){
//                            cbs[Integer.parseInt(String.valueOf(buttonView.getTag()))].setChecked(true);
                            List<HashMap<String,Object>> listResult = null;
                            HttpRequest httpRequest = new HttpRequest();
                            String request = "?cartId="+cartList.get(position).get("id")+"&type=1";
                            System.out.println(request);
                            System.out.println(cbs[position].isChecked());
                            listResult = httpRequest.resultJson(request,"POST","selectChecked");
                            if(listResult.get(0).get("status").toString().equals("0")){
                                cartList.get(position).put("checked",1);
                            }

                        }else{
//                            cbs[Integer.parseInt(String.valueOf(buttonView.getTag()))].setChecked(false);
                            List<HashMap<String,Object>> listResult = null;
                            HttpRequest httpRequest = new HttpRequest();
                            String request = "?cartId="+cartList.get(position).get("id")+"&type=0";
                            System.out.println(request);
                            listResult = httpRequest.resultJson(request,"POST","selectChecked");
                            if(listResult.get(0).get("status").toString().equals("0")){
                                cartList.get(position).put("checked",0);
                            }
                        }
                        List<HashMap<String,Object>> listResult1 = null;
                        HttpRequest httpRequest = new HttpRequest();
                        String request = "";
                        System.out.println(request);
                        listResult1 = httpRequest.resultJson(request,"GET","getPrice");
                        Global.totalPrice = listResult1.get(0).get("msg").toString();
                        if (onPriceChangedListener != null) {
                            onPriceChangedListener.onPriceChanged(calculatePrice());
                        }
                    }
                }).start();


            }
        });
        //增加数量
        viewHolder.btnAdd.setOnClickListener(new AddListener());

        //减少数量
        viewHolder.btnSub.setOnClickListener(new SubListener());


        return convertView;

    }


    class ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
        TextView tvInfo;
        TextView tvPrice;
        Button btnAdd;
        Button btnSub;
        TextView tvNum;
    }
    /**
     * @Description 减少数量
    **/
    class SubListener implements View.OnClickListener{
        @Override
        public void onClick(final View v) {
//            System.out.println(v.getTag());
//            System.out.println(Integer.parseInt(String.valueOf(v.getTag())));
//            int num = 0;
            System.out.println(viewHolder.checkBox.isChecked());
            int num = Integer.parseInt(tvs[Integer.parseInt(String.valueOf(v.getTag()))].getText().toString());
            if(num <= 1){
                Toast.makeText(CartContext, "数量不能小于1", Toast.LENGTH_SHORT).show();
            }else{

                num = num - 1;
                tvs[Integer.parseInt(String.valueOf(v.getTag()))].setText(String.valueOf(num));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<HashMap<String,Object>> listResult = null;
                        HttpRequest httpRequest = new HttpRequest();
                        String request = "?cartId="+cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).get("id");
                        System.out.println(request);
                        listResult = httpRequest.resultJson(request,"POST","decQuantity");
                        int quantity = Integer.parseInt(cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).get("quantity").toString()) - 1;
                        cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).put("quantity",quantity);
//                    notifyDataSetChanged();
                        List<HashMap<String,Object>> listResult1 = null;
                        HttpRequest httpRequest1 = new HttpRequest();
                        String request1 = "";
                        System.out.println(request);
                        listResult1 = httpRequest1.resultJson(request1,"GET","getPrice");
                        if(listResult1 != null){
                            Global.totalPrice = listResult1.get(0).get("msg").toString();
                            calculatePrice();
                            if (onPriceChangedListener != null) {
                                onPriceChangedListener.onPriceChanged(calculatePrice());
                            }
                        }

                    }
                }).start();
            }
        }
    }
    /**
     * @Description 增加数量按钮
     **/
    class AddListener implements View.OnClickListener{
        @Override
        public void onClick(final View v) {
            int num = Integer.parseInt(tvs[Integer.parseInt(String.valueOf(v.getTag()))].getText().toString());
            num = num + 1;
            tvs[Integer.parseInt(String.valueOf(v.getTag()))].setText(String.valueOf(num));
            System.out.println(calculatePrice());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String,Object>> listResult = null;
                    HttpRequest httpRequest = new HttpRequest();
                    String request = "?cartId="+cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).get("id");
                    System.out.println(request);
                    listResult = httpRequest.resultJson(request,"POST","addQuantity");
                    int quantity = Integer.parseInt(cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).get("quantity").toString()) + 1;
                    cartList.get(Integer.parseInt(String.valueOf(v.getTag()))).put("quantity",quantity);
//                    notifyDataSetChanged();


                    List<HashMap<String,Object>> listResult1 = null;
                    HttpRequest httpRequest1 = new HttpRequest();
                    String request1 = "";
                    System.out.println(request);
                    listResult1 = httpRequest1.resultJson(request1,"GET","getPrice");
                    Global.totalPrice = listResult1.get(0).get("msg").toString();
                    System.out.println(Global.totalPrice);
                    calculatePrice();
                    if (onPriceChangedListener != null) {
                        onPriceChangedListener.onPriceChanged(calculatePrice());
                    }
                }
            }).start();
        }
    }
    /**
     * @Description //TOOD 监听接口
    **/
    public interface OnPriceChangedListener {
        void onPriceChanged(int price);
    }

    public void setOnPriceChangedListener(OnPriceChangedListener onPriceChangedListener) {
        this.onPriceChangedListener = onPriceChangedListener;
    }
    /**
     * @Description //TOOD 计算价格
    **/
    public int calculatePrice(){
        price = 0;
        for(int i = 0; i < cartList.size(); i++){
            if(cbs[i].isChecked() && cbs[i] != null && tvs[i] != null && prices[i] != null){
//                price = price + Integer.parseInt(tvs[i].getText().toString()) * Integer.parseInt(prices[i].getText().toString());
            }
        }

        return price;
    }
}
