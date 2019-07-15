package com.example.administrator.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.widget.Switch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * @ClassName HttpRequest
 * @Date 2019-06-14 10:36
 **/

public class HttpRequest {
    private String result = "";
    public static Bitmap bitmap = null;
    private static String session_id = null;
    List<HashMap<String,Object>> allData = null;
    /**
     * @Description //TOOD http连接服务器，获取数据
     * @Param [URL地址, 发送请求的类型]
     * @return java.lang.String
    **/
    public static String send(String u, String request,String type){
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(request.equals("POST")){
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
            }


            //请求类型
            conn.setRequestMethod(request);
            if(request.equals("GET") && !type.equals("showCart") && !type.equals("getPrice") && !type.equals("showCollection") && !type.equals("getUserInfo")
                    && !type.equals("scan")&& !type.equals("showShipping")&& !type.equals("showOrder") && !type.equals("showShipping1")){
                conn.connect();
            }

            if (session_id != null && ((type == "createOrder")||(type == "addCollection")||(type == "addCart")|| (type == "showCart")|| (type == "createCartOrder")|| (type == "showCollection")
                    ||(type == "selectChecked")||(type.equals("getPrice"))||(type.equals("addQuantity"))||(type.equals("decQuantity"))||(type.equals("deleteCart"))
                    ||(type.equals("deleteCollection"))||(type.equals("getUserInfo"))||(type.equals("showShipping"))||(type.equals("showOrder")) ||(type.equals("showShipping1")) ||(type.equals("showLogistic")))) {
                conn.setRequestProperty("Cookie", session_id);//设置sessionid
            }
            InputStream inStream = conn.getInputStream();

            String cookieval = conn.getHeaderField("Set-Cookie");
            if (cookieval != null && type == "login") {
                session_id = null;
                session_id = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
                Log.i("SESSION", "session_id=" + session_id);
            }
            while ((len = inStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(new String(outStream.toByteArray()));
        //通过out.Stream.toByteArray获取到写的数据
        return new String(outStream.toByteArray());
    }
    /**
     * 编码格式问题
    **/
    public static String getResult(String urlStr, String content, String encoding,String type) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            System.out.println(url);
            System.out.println(content);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.setRequestProperty("Accept-Charset", encoding);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.write(content.toString().getBytes("UTF-8"));// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            String cookieval = connection.getHeaderField("Set-Cookie");
            if (cookieval != null && type.equals("login")) {
                session_id = null;
                session_id = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
                Log.i("SESSION", "session_id=" + session_id);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            System.out.println(buffer.toString());
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

        //通过http向网络获取图片
    public static void getImage(String u, String request){
        try{
            URL url = new URL(u);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(request);
            connection.setConnectTimeout(5000);
            int code = connection.getResponseCode();
            if(code == 200){
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @Description //TOOD 适配数据，返回result
     * @return java.lang.String
    **/
    public List<HashMap<String,Object>> resultJson(String s, String r, String type){
        try {
            String url = "http://zssxl.natapp1.cc";
//            String url = "http://10.168.14.100:8888";
            String path = null;

            Analysis analysis = new Analysis();
            String request = r;
            switch(type){
                case "login":
                    url = url + "/farmmall/user/login.do";
                    System.out.println(url);
                    allData = analysis.AnalysisLogin(getResult(url,s,"UTF-8",type));
                    break;
                case "register":
                    url = url + "/farmmall/user/register.do";
                    allData = analysis.AnalysisRegister(getResult(url,s,"UTF-8",type));
                    break;
                case "getAllCategory":
                    url = url + "/farmmall/category/get_all_category.do" + s;
                    allData = analysis.AnalysisGetAllCategory(send(url,request,type));
                    break;
                case "image":
                    url = "" + s;
                    getImage(url,request);
                    break;
                case "getAllProduct":
                    url = url + "/farmmall/product/show_product.do" + s;
                    allData = analysis.AnalysisGetAllProduct(send(url,request,type));
                    break;
                case "createOrder":
                    url = url + "/farmmall/order/create.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "getPrice":
                    url = url + "/farmmall/cart/calcu_total_price.do" + s;
                    allData = analysis.AnalysisWatch(send(url,request,type));
                    break;
                case "addCart":
                    url = url + "/farmmall/cart/add_cart.do" + s;
                    allData = analysis.AnalysisWatch(send(url,request,type));
                    break;
                case "createCartOrder":
                    url = url + "/farmmall/order/create_order_from_cart.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "showCart":
                    url = url + "/farmmall/cart/show_cart.do" + s;
                    allData = analysis.AnalysisShowCart(send(url,request,type));
                    break;
                case "checkAll":
                    url = url + "/farmmall/cart/select_check_all.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "selectChecked":
                    url = url + "/farmmall/cart/select_checked.do" + s;
                    allData = analysis.AnalysisWatch(send(url,request,type));
                    break;
                case "addQuantity":
                    url = url + "/farmmall/cart/add_quantity.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "decQuantity":
                    url = url + "/farmmall/cart/dec_quantity.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "deleteCart":
                    url = url + "/farmmall/cart/delete_cart_by_cartId.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "showCollection":
                    url = url + "/farmmall/product/show_collection.do" + s;
                    allData = analysis.AnalysisShowCollecion(send(url,request,type));
                    break;
                case "deleteCollection":
                    url = url + "/farmmall/product/delete_collection.do" + s;
                    allData = analysis.AnalysisCreateCartOrder(send(url,request,type));
                    break;
                case "addCollection":
                    url = url + "/farmmall/product/add_collection.do" + s;
                    allData = analysis.AnalysisWatch(send(url,request,type));
                    break;
                case "getUserInfo":
                    url = url + "/farmmall/user/get_user_info.do" + s;
                    allData = analysis.AnalysisUserInfo(send(url,request,type));
                    break;
                case "showShipping":
                    url = url + "/farmmall/shipping/show_shipping.do" + s;
                    allData = analysis.AnalysisShowShipping(send(url,request,type));
                    break;
                case "scan":
                    url = url + "/farmmall/livestock/scanning_query.do" + s;
                    System.out.println(s);
                    allData = analysis.AnalysisScan(send(url,request,type));
                    break;
                case "showOrder":
                    url = url + "/farmmall/order/show_order.do" + s;
                    allData = analysis.AnalysisShowOrder(send(url,request,type));
                    break;
                case "showShipping1":
                    url = url + "/farmmall/shipping/show_shipping_by_id.do" + s;
                    allData = analysis.AnalysisshowShipping1(send(url,request,type));
                    break;
                case "showLogistic":
                    url = url + "/farmmall/show_logistic.do" + s;
                    allData = analysis.AnalysisshowLogistic(send(url,request,type));
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return allData;
        }
    }

}
