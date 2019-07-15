package com.example.administrator.fragment;

import android.app.Fragment;
import android.content.Intent;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.ProductDetailsActivity;
import com.example.administrator.activity.R;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.utils.ToastUtil.showToast;

/**
 * @ClassName CategoryFragment
 * @Date 2019-05-29 20:39
 **/

public class CategoryFragment extends Fragment {
    private EditText etSearch;
    private ImageButton ibSearch;
    private ListView listView;
    private GridView gridView;
    private List<HashMap<String,Object>> listAllCategory = null;
    private List<HashMap<String,Object>> listAllProduct = null;
    private List<HashMap<String,Object>> listProduct = new ArrayList<HashMap<String,Object>>();
    private Handler handler = null;
    private List<Map<String,Object>> list_data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category,container,false);
        initView(view);
        ibSearch = (ImageButton)view.findViewById(R.id.category_top_ibtn);
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView(view);
            }
        });
        return view;
    }
    public void initView(View view){
        etSearch = (EditText)view.findViewById(R.id.category_top_et);
        ibSearch = (ImageButton)view.findViewById(R.id.category_top_ibtn);
        listView = (ListView)view.findViewById(R.id.category_listView);
        gridView = (GridView)view.findViewById(R.id.category_gridView);
        handler = new Handler();

        //分类栏名称通过http获取，展示所有商品
        new Thread(new Runnable() {
            @Override
            public void run() {
                Global.isDone = false;
                HttpRequest httpRequest = new HttpRequest();
                String request = "";
                listAllCategory = httpRequest.resultJson(request,"GET","getAllCategory");

                request = "?type=1&pn=1&categoryId=0";
                listProduct = httpRequest.resultJson(request,"GET","getAllProduct");
                if(listProduct == null || listProduct == null){
                    showToast(getActivity(), "网络异常，请检查网络");
                }else{
                    handler.post(runnableUiAllCategory);
                    //获取图片
                    Bitmap bitmap = null;
                    HttpRequest httpRequest1 = new HttpRequest();
                        for(int i = 0; i < listProduct.size(); i++){

                        String request1 = listProduct.get(i).get("mainImage").toString();
                        httpRequest1.resultJson(request1,"GET","image");
                        if(HttpRequest.bitmap != null){
                            listProduct.get(i).put("image",HttpRequest.bitmap);
                        }
                    }
                    handler.post(runnableUiProduct);
                }

            }
        }).start();

        //分类栏点击，获取相应的数据
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(Global.isDone == true){
                    new Thread(){
                        public void run(){
                            Global.isDone = false;
                            getData(position);
                        }
                    }.start();
                }

            }
        });


        //传递数据给商品详情页面
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent0 = new Intent(getActivity().getApplicationContext(), ProductDetailsActivity.class);
                intent0.putExtra("id",listProduct.get(position).get("id").toString());
                intent0.putExtra("name",listProduct.get(position).get("name").toString());
                intent0.putExtra("mainImage",listProduct.get(position).get("id").toString());
                intent0.putExtra("price",listProduct.get(position).get("price").toString());
                intent0.putExtra("stock",listProduct.get(position).get("stock").toString());
                intent0.putExtra("detail",listProduct.get(position).get("detail").toString());
                intent0.putExtra("mainImage",listProduct.get(position).get("mainImage").toString());
                intent0.putExtra("status",listProduct.get(position).get("status").toString());
                startActivity(intent0);
            }
        });
    }


    /**
     * @Description //TOOD 从服务器获取数据
    **/
    public void getData(int position){
        //清空listProduct
        if(listProduct != null){
            listProduct.clear();
        }
        String categoryId = listAllCategory.get(position).get("id").toString();
        HttpRequest httpRequest = new HttpRequest();
        String request = "?type=2&pn=1&categoryId=" + categoryId;
        listProduct = httpRequest.resultJson(request,"GET","getAllProduct");

        if(listProduct != null){
            //获取网络图片
            HttpRequest httpRequest1 = new HttpRequest();
            for(int i = 0; i < listProduct.size(); i++){

                String request1 = listProduct.get(i).get("mainImage").toString();
                httpRequest1.resultJson(request1,"GET","image");
                listProduct.get(i).put("image",HttpRequest.bitmap);
            }
            //更新界面
            handler.post(runnableUiProduct);
        }else{
            showToast(getActivity(), "网络异常，请检查网络");
        }

    }


    /**
     * @Description 更新种类界面
     **/
    Runnable runnableUiAllCategory = new Runnable() {
        @Override
        public void run() {
            String [] from ={"name"};
            int [] to = {R.id.item_category_left};
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),listAllCategory,R.layout.item_category_left,from,to);
            listView.setAdapter(simpleAdapter);
        }
    };

   /**
    * @Description 更新视图界面
   **/
    Runnable runnableUiProduct = new Runnable() {
        @Override
        public void run() {
            String [] from ={"image","name"};
            int [] to = {R.id.item_category_right_image,R.id.item_category_right_text};
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),listProduct,R.layout.item_category_right,from,to);
            //适配图片
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                          @Override
                                          public boolean setViewValue(View view, Object data,
                                                                      String textRepresentation) {
                                              if((view instanceof ImageView )&& (data instanceof Bitmap)){
                                                  ImageView i = (ImageView)view;
                                                  i.setImageBitmap((Bitmap) data);
                                                  return true;
                                              }
                                              return false;
                                          }

                                      });
            gridView.setAdapter(simpleAdapter);
            Global.isDone = true;
        }
    };


}
