package com.example.administrator.network;

import android.util.JsonReader;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName Analysis
 * @Date 2019-06-17 16:41
 **/

public class Analysis {
    //登录数据的解析
    public static ArrayList<HashMap<String,Object>> AnalysisLogin(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        if(jsonObject != null){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            list.add(map);
        }
        return list;
    }
    //注册
    public static ArrayList<HashMap<String,Object>> AnalysisRegister(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        if(jsonObject != null){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            map.put("msg",jsonObject.getString("msg"));
            list.add(map);
        }
        return list;
    }
    //用户信息
    public static ArrayList<HashMap<String,Object>> AnalysisUserInfo(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        int status = jsonObject.getInt("status");
        if(status == 0){
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            map.put("id",jsonObject1.getInt("id"));
            map.put("username",jsonObject1.getString("username"));
            map.put("email",jsonObject1.getString("email"));
            map.put("sex",jsonObject1.getInt("sex"));
            map.put("phone",jsonObject1.getString("phone"));
            map.put("role",jsonObject1.getInt("role"));
            map.put("shopName",jsonObject1.getString("shopName"));
            list.add(map);
        }else{
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            list.add(map);
        }
        return list;
    }
    //获取分类数据
    public static ArrayList<HashMap<String,Object>> AnalysisGetAllCategory(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray= null;

        jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("id",jsonObject1.getInt("id"));
            map.put("name",jsonObject1.getString("name"));
            list.add(map);
        }
        return list;
    }
    //获取所有的商品
    public static ArrayList<HashMap<String,Object>> AnalysisGetAllProduct(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        JSONArray jsonArray= null;

        jsonArray = jsonObject1.getJSONArray("list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("id",jsonObject2.getInt("id"));
            map.put("userId",jsonObject2.getInt("userId"));
            map.put("categoryId",jsonObject2.getInt("categoryId"));
            map.put("livestock",jsonObject2.getInt("livestock"));
            map.put("name",jsonObject2.getString("name"));
            map.put("subtitle",jsonObject2.getString("subtitle"));
            map.put("mainImage",jsonObject2.getString("mainImage"));
            map.put("detail",jsonObject2.getString("detail"));
            map.put("stock",jsonObject2.getInt("stock"));
            map.put("status",jsonObject2.getInt("status"));
            map.put("price", jsonObject2.getDouble("price"));
//            map.put("price",jsonObject2.getBig)
            list.add(map);
        }
        return list;
    }
    //创建订单
   /* public static ArrayList<HashMap<String,Object>> AnalysisCreateOrder(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        int status = jsonObject.getInt("status");
        if(status == 0){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
//            map.put("data",jsonObject.getString("data"));
            list.add(map);
        }else {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            list.add(map);
        }
        return list;
    }*/
    public static ArrayList<HashMap<String,Object>> AnalysisCreateCartOrder(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("status",jsonObject.getInt("status"));
//        map.put("data",jsonObject.getString("data"));
        list.add(map);

        return list;
    }
    public static ArrayList<HashMap<String,Object>> AnalysisWatch(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        if(jsonStr != null){
            JSONObject jsonObject = new JSONObject(jsonStr);
//        JSONObject data = jsonObject.getJSONObject("data");
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",jsonObject.getInt("status"));
            map.put("msg",jsonObject.getString("msg"));
            list.add(map);
        }
        return list;
    }
    public static ArrayList<HashMap<String,Object>> AnalysisGetPrice(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        JSONObject jsonObject = new JSONObject(jsonStr);
//        JSONObject data = jsonObject.getJSONObject("data");
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("status",jsonObject.getInt("status"));
        map.put("msg",jsonObject.getDouble("msg"));
        list.add(map);

        return list;
    }
    /**
     * @Description //TOOD 查看购物车
    **/
    public static ArrayList<HashMap<String,Object>> AnalysisShowCart(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
//        String status = jsonObject.getJSONObject("status").toString();
        System.out.println("23222222222222222222222222223");
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        JSONArray jsonArray= null;

        jsonArray = jsonObject1.getJSONArray("list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
//            map.put("status",status);
            map.put("id",jsonObject2.getInt("id"));
            map.put("userId",jsonObject2.getInt("userId"));
            map.put("productId",jsonObject2.getInt("productId"));
            map.put("name",jsonObject2.getString("name"));
            map.put("price",jsonObject2.getDouble("price"));
            map.put("quantity",jsonObject2.getInt("quantity"));
            map.put("checked",jsonObject2.getInt("checked"));
            map.put("mainImage",jsonObject2.getString("mainImage"));
            list.add(map);
        }
        return list;
    }

    /**
     * @Description //TOOD 查看收藏
     **/
    public static ArrayList<HashMap<String,Object>> AnalysisShowCollecion(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
//        String status = jsonObject.getJSONObject("status").toString();
        System.out.println("23222222222222222222222222223");
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        JSONArray jsonArray= null;

        jsonArray = jsonObject1.getJSONArray("list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
//            map.put("status",status);
            map.put("id",jsonObject2.getInt("id"));
            map.put("userId",jsonObject2.getInt("userId"));
            map.put("categoryId",jsonObject2.getInt("categoryId"));
            map.put("livestock",jsonObject2.getInt("livestock"));
            map.put("name",jsonObject2.getString("name"));
            map.put("detail",jsonObject2.getString("detail"));
            map.put("price",jsonObject2.getInt("price"));
            map.put("status",jsonObject2.getInt("status"));
            map.put("stock",jsonObject2.getInt("stock"));
            map.put("mainImage",jsonObject2.getString("mainImage"));
            list.add(map);
        }
        return list;
    }

    /**
     * @Description //TOOD 查看地址
     **/
    public static ArrayList<HashMap<String,Object>> AnalysisShowShipping(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        JSONArray jsonArray= null;

        jsonArray = jsonObject1.getJSONArray("list");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            HashMap<String,Object> map = new HashMap<String,Object>();
//            map.put("status",status);
            map.put("id",jsonObject2.getInt("id"));
            map.put("userId",jsonObject2.getInt("userId"));
            map.put("name",jsonObject2.getString("receiverName"));
            map.put("receiverPhone",jsonObject2.getString("receiverPhone"));
            map.put("receiverProvince",jsonObject2.getString("receiverProvince"));
            map.put("receiverCity",jsonObject2.getString("receiverCity"));
            map.put("receiverDistrict",jsonObject2.getString("receiverDistrict"));
            map.put("receiverAddress",jsonObject2.getString("receiverAddress"));
            map.put("receiverZip",jsonObject2.getString("receiverZip"));
            list.add(map);
        }
        return list;
    }

    /**
     * @Description //TOOD 扫码
     **/
    public static ArrayList<HashMap<String,Object>> AnalysisScan(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        int status = jsonObject.getInt("status");
        if(status == 0){
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",status);
            map.put("id",jsonObject1.getInt("id"));
            map.put("label",jsonObject1.getInt("label"));
            map.put("weight",jsonObject1.getInt("weight"));
            map.put("age",jsonObject1.getInt("age"));
            map.put("varieties",jsonObject1.getString("varieties"));
            map.put("faces",jsonObject1.getString("faces"));
            map.put("stapleFood",jsonObject1.getString("stapleFood"));
            map.put("medicalRecord",jsonObject1.getString("medicalRecord"));
            map.put("health",jsonObject1.getString("health"));
            map.put("vaccine",jsonObject1.getString("vaccine"));
            map.put("photo",jsonObject1.getString("photo"));
            map.put("origin",jsonObject1.getString("origin"));
            map.put("createTime",jsonObject1.getString("createTime"));
            map.put("updateTime",jsonObject1.getString("updateTime"));
            list.add(map);
        }else{
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("status",status);
            list.add(map);
        }
        return list;
    }

    /*
     * 订单详情
     *
     * */
    public static ArrayList<HashMap<String,Object>> AnalysisShowOrder(String jsonStr)throws JSONException{
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        JSONArray jsonArray= null;
        jsonArray = jsonObject1.getJSONArray("list");
//        jsonArray = jsonObject1.getJSONArray("total");

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("total",jsonObject2.getInt("total"));
            map.put("orderNo", jsonObject2.getString("orderNo")); //订单号
            map.put("userId", jsonObject2.getInt("userId")); //用户ID
            map.put("shippingId", jsonObject2.getString("shippingId")); //收货地址
            map.put("productId", jsonObject2.getInt("productId")); //产品ID
            map.put("productName", jsonObject2.getString("productName")); //产品名
            map.put("productImage", jsonObject2.getString("productImage")); //产品图片
            map.put("currentUnitPrice", jsonObject2.getInt("currentUnitPrice")); //产品单价
            map.put("quantity", jsonObject2.getInt("quantity")); //购买数
            map.put("totalPrice", jsonObject2.getInt("totalPrice")); //总价
            map.put("paymentType", jsonObject2.getInt("paymentType")); //支付方式
            map.put("status",jsonObject2.getString("status"));
            //订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
            list.add(map);
        }
        return list;
    }

    public static ArrayList<HashMap<String,Object>> AnalysisshowShipping1(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject data = jsonObject.getJSONObject("data");
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("id",data.getInt("id"));
        map.put("userId",data.getInt("userId"));
        map.put("receiverName",data.getString("receiverName"));//收货姓名
        map.put("receiverPhone",data.getString("receiverPhone"));//联系电话
        map.put("receiverProvince",data.getString("receiverProvince"));//省
        map.put("receiverCity",data.getString("receiverCity"));//市
        map.put("receiverDistrict",data.getString("receiverDistrict"));//县
        map.put("receiverAddress",data.getString("receiverAddress"));//详细地址
        map.put("receiverZip",data.getString("receiverZip"));//邮编
        map.put("createTime",data.getString("createTime"));//
        map.put("updateTime",data.getString("updateTime"));//
        list.add(map);

        return list;
    }
    //地图经纬度
    public static ArrayList<HashMap<String,Object>> AnalysisshowLogistic(String jsonStr) throws JSONException {
        //初始化list对象
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject data = jsonObject.getJSONObject("data");
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("longitude",data.getInt("longitude"));
        map.put("latitude",data.getInt("latitude"));
        list.add(map);

        return list;
    }
}
