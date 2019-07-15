package com.example.administrator.model;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName Global
 * @Date 2019-06-04 10:49
 * 存储全局变量 ：登录状态
 **/

public class Global {
    public static boolean isLogin = false;
    public static boolean isRegister = false;
    public static boolean isDone = false;
    public static String num = null;
    public static String username = null;
    public static String species = null;
    public static String tag = null;
    public static Bitmap[] bitmaps = new Bitmap[100];
    public static String totalPrice = null;
    public static List<HashMap<String,Object>> list_data = null;
    public static List<HashMap<String,Object>> list_show_collection = null;
    public static List<HashMap<String,Object>> list_colection_product = null;
    public static List<HashMap<String,Object>> list_user_info = null;
    public static List<HashMap<String,Object>> list_shipping = null;
    public static List<HashMap<String,Object>> list_scan = null;
    public static int shippingId;
}
