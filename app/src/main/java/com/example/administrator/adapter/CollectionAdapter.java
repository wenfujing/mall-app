package com.example.administrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.administrator.activity.CollectionActivity;
import com.example.administrator.activity.ProductDetailsActivity;
import com.example.administrator.activity.R;
import com.example.administrator.model.Global;
import com.example.administrator.network.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CollectionAdapter extends BaseAdapter {
    private View view;
    private TextView tvNum;
    private Context CollectionContext;
    private ViewHolder viewHolder = null;
    private List<HashMap<String,Object>> CollectionList = new ArrayList<HashMap<String,Object>>();
    private List<Boolean> mChecked;
    private int i = 0;
//    public OnPriceChangedListener onPriceChangedListener;
    private TextView[] tvs = new TextView[100];
    private TextView[] prices = new TextView[100];
    public int price = 0;
    private OnCollectionChangedListener onCollectionChangedListener;

    public CollectionAdapter(Context context, List<HashMap<String,Object>> list){
        CollectionContext = context;
        CollectionList = list;

        /*mChecked = new ArrayList<Boolean>();
        for(int i = 0; i < list.size(); i ++){
            mChecked.add(false);
        }*/
    }
    @Override
    public int getCount() {
        return CollectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return CollectionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CollectionContext).inflate(R.layout.item_collection_lv, null);
            view = convertView;

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_collection_iv);
            viewHolder.tvInfo = (TextView) convertView.findViewById(R.id.item_collection_info);
            viewHolder.tvPrice = (TextView)convertView.findViewById(R.id.item_collection_price);
            viewHolder.btnCancel = (Button) convertView.findViewById(R.id.item_collection_cancle);
            convertView.setTag(viewHolder);
            viewHolder.btnCancel.setTag(position);
            viewHolder.btnCancel.setTag(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPrice.setText(CollectionList.get(position).get("price").toString());
        viewHolder.tvInfo.setText(CollectionList.get(position).get("name").toString());
        viewHolder.imageView.setImageBitmap((Bitmap)CollectionList.get(position).get("image"));
        viewHolder.btnCancel.setOnClickListener(new CancelListener());
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView tvInfo;
        TextView tvPrice;
        Button btnCancel;
    }

    /**
     * @Description //TOOD 监听接口
     **/
    public interface OnCollectionChangedListener {
        void onCollectionChanged();
    }

    public void setOnCollectionChangedListener(OnCollectionChangedListener onCollectionChangedListener) {
        this.onCollectionChangedListener = onCollectionChangedListener;
    }

    /**
     * @Description 取消收藏
    **/
    class CancelListener implements View.OnClickListener{
        @Override
        public void onClick(final View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpRequest httpRequest = new HttpRequest();
                    String id = Global.list_show_collection.get(Integer.parseInt(v.getTag().toString())).get("id").toString();
                    String request = "?productId=" + id;
                    httpRequest.resultJson(request,"POST","deleteCollection");
                    Global.list_show_collection.remove(Integer.parseInt(v.getTag().toString()));
                    if (onCollectionChangedListener != null) {
                        onCollectionChangedListener.onCollectionChanged();
                    }
                }
            }).start();
        }
    }

}