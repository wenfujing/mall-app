package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.example.administrator.adapter.CollectionAdapter;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.order.Order_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2019-06-05 19:59
 * 收藏
 **/

public class CollectionActivity extends Activity {
    private int id;
//    private LocalReceiver mReceiver;
    private static List<Map<String,Object>> list_data= new ArrayList<Map<String,Object>>();
    private int[] icon = {R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test,R.mipmap.test};
    private ListView listView;
    private Button btnCancle;
    private int flag = 1;
    private ImageButton igBack;
    private CollectionAdapter collectionAdapter;
    public static int listSize = 0;
    private List<HashMap<String,Object>> showCollectionList = new ArrayList<HashMap<String,Object>>();
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        if(Global.isLogin){

        }else {
            Toast toast = Toast.makeText(CollectionActivity.this,"请先登录",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
    public void initView(){
        listView = (ListView)findViewById(R.id.collection_lv);
        btnCancle = (Button)findViewById(R.id.item_collection_cancle);
        igBack = (ImageButton)findViewById(R.id.collection_btn_back);
        //返回按钮
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest();
                String request = "?pn=1";
                showCollectionList = httpRequest.resultJson(request,"GET","showCollection");
                if(showCollectionList != null){
                    HttpRequest httpRequest1 = new HttpRequest();
                    for(int i = 0; i < showCollectionList.size(); i++){

                        String request1 = showCollectionList.get(i).get("mainImage").toString();
                        httpRequest1.resultJson(request1,"GET","image");
                        showCollectionList.get(i).put("image",HttpRequest.bitmap);
                    }
                    Global.list_show_collection = showCollectionList;
                    collectionAdapter = new CollectionAdapter(CollectionActivity.this,Global.list_show_collection);
                    handler.post(runnableUiCollection);
                    collectionAdapter.setOnCollectionChangedListener(new CollectionAdapter.OnCollectionChangedListener() {
                        @Override
                        public void onCollectionChanged() {
                            handler.post(runnableUiCollection);
                        }
                    });
                }

            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent0 = new Intent(CollectionActivity.this, ProductDetailsActivity.class);
                intent0.putExtra("id",Global.list_show_collection.get(position).get("id").toString());
                intent0.putExtra("name",Global.list_show_collection.get(position).get("name").toString());
                intent0.putExtra("price",Global.list_show_collection.get(position).get("price").toString());
                intent0.putExtra("stock",Global.list_show_collection.get(position).get("stock").toString());
                intent0.putExtra("detail",Global.list_show_collection.get(position).get("detail").toString());
                intent0.putExtra("mainImage",Global.list_show_collection.get(position).get("mainImage").toString());
                startActivity(intent0);
            }
        });

    }
    Runnable runnableUiCollection = new Runnable() {
        @Override
        public void run() {
            //更新界面
            listView.setAdapter(collectionAdapter);
        }
    };

}
