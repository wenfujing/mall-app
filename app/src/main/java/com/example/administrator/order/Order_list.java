package com.example.administrator.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.CollectionActivity;
import com.example.administrator.activity.R;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.administrator.utils.ToastUtil.showToast;


public class Order_list extends Activity {

    private static final int COMPLETED = 0;

    List<HashMap<String,Object>> list = null;
    private String result = null;

    private String productName = "productName";//产品名
    private String orderID = "orderNo";

    private String[] orderNO = new String[100];
    private int i = 0;
    private int time;

    private ExpandableListView elMainOrdercenter;
    private ImageButton ibBack;
    private Map<String,List<Order_info>> dataMap;
    private String[] titleArr;
    private int[] iconArr=new int[]{R.mipmap.order_icon,R.mipmap.icon2,R.mipmap.icon1};
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        //初始化数据
        initData();
        //初始化点击监听事件
        initOnclickListener();
    }
    /**
     * 初始化数据
     */
    private void initData(){
        //初始化ExpandlistView的id
        elMainOrdercenter=(ExpandableListView)findViewById(R.id.el_main_ordercenter);
        ibBack = (ImageButton)findViewById(R.id.order_list_back);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化列表数据，正常由服务器返回的Json数据
        if(Global.isLogin){
            initJsonData();
        }else {
            Toast toast = Toast.makeText(Order_list.this,"请先登录",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
    /**
     * 初始点击事件
     */
    private void initOnclickListener(){
        //设置父标题点击不能收缩
        elMainOrdercenter.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        //订单子条目的点击事件
        elMainOrdercenter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent2 = new Intent(Order_list.this, Order_particulars.class);
                intent2.putExtra("list",childPosition);
                time = childPosition;
                Bundle bundleSimple = new Bundle();
                bundleSimple.putInt("pwd",time);
                bundleSimple.putStringArray("yoyoo",orderNO);
                intent2.putExtras(bundleSimple);
                startActivity(intent2);
                Toast.makeText(Order_list.this,"跳转到订单详细页面:"+childPosition,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    /**
     * ExpandableListviewAdapter初始化
     */
    private class MyAdapter extends BaseExpandableListAdapter{
        //  获得父项的数量
        @Override
        public int getGroupCount() {
            return dataMap.size();
        }
        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int groupPosition) {
            return dataMap.get(titleArr[groupPosition]).size();
        }
        //  获得某个父项
        @Override
        public Object getGroup(int groupPosition) {
            return dataMap.get(titleArr[groupPosition]);
        }
        //  获得某个父项的某个子项
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return dataMap.get(titleArr[groupPosition]).get(childPosition);
        }
        //  获得某个父项的id
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }
        //  获得父项显示的view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(Order_list.this,R.layout.parent_view,null);
            }
            ImageView ivParentviewIcon=(ImageView) convertView.findViewById(R.id.iv_parentview_icon);
            TextView tvParentviewTitle=(TextView) convertView.findViewById(R.id.tv_parentview_title);
            ivParentviewIcon.setImageResource(iconArr[groupPosition]);
            tvParentviewTitle.setText(titleArr[groupPosition]);
            return convertView;
        }
        //  获得子项显示的view
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(Order_list.this,R.layout.child_view,null);
            }
            //获取布局控件id
            TextView tvChildviewContent=(TextView) convertView.findViewById(R.id.tv_childview_content);
            tvChildviewContent.setText(dataMap.get(titleArr[groupPosition]).get(childPosition).getName());

            //根据服务器返回的数据来显示和隐藏按钮
            final Order_info orderInfo=dataMap.get(titleArr[groupPosition]).get(childPosition);

            return convertView;
        }
        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    /**
     * 初始化列表数据，正常由服务器返回的Json数据
     */
    public void initJsonData(){
        dataMap=new HashMap<String,List<Order_info>>();
        titleArr=new String[]{"我的订单："};
        final List<Order_info> list1=new ArrayList<Order_info>();

        new Thread(new Runnable() {
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
                    for (Map<String, Object> map : list) {
                        for (Map.Entry<String, Object> m : map.entrySet()) {
                            Order_info orderInfo = new Order_info();
                            if (m.getKey() == productName){
                                orderInfo.setName(titleArr[0]+" "+m.getValue());
                                orderInfo.setEvaluateState(true);
                                orderInfo.setDeleteState(false);
                                list1.add(orderInfo);
                            }
                            if (m.getKey() == orderID){
                                orderNO[i] = m.getValue().toString();
                                i++;
                            }
                        }
                    }
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);
              }else {
                    showToast(Order_list.this, "网络异常");
                }
            }
        }).start();

        dataMap.put(titleArr[0],list1);

        for (int j = 0;j <= 10; j++){

            String m = orderNO[j];
            System.out.println(m);
        }

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED){
                myAdapter=new MyAdapter();
                elMainOrdercenter.setAdapter(myAdapter);
                //设置列表展开
                for(int i=0;i<dataMap.size();i++){
                    elMainOrdercenter.expandGroup(i);
                }
            }
        }
    };

}

