package com.example.administrator.ui.route;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.activity.R;
import com.example.administrator.network.HttpRequest;
import com.example.administrator.utils.AMapUtil;


import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangqie on 2017/8/12.
 *
 * 两点绘制路线
 *
 */

public class RouteActivity extends AppCompatActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    List<HashMap<String,Object>> list = null;

    private String longitude; //经度
    private String latitude;  //纬度
    private LatLonPoint mStartPoint = new LatLonPoint(25.851299, 114.784667); //起点
    private LatLonPoint mEndPoint = new LatLonPoint(25.838074, 114.936833);//终点
    //114.936833,25.838074
    //https://lbs.amap.com/api/javascript-api/example/map/click-to-get-lnglat/
    //黄金机场:114.784667,25.851299
    private final int ROUTE_TYPE_WALK = 3;


    private ProgressDialog progDialog = null;// 搜索时进度条
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.map_layout);
        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(bundle);



        init();
        setfromandtoMarker();
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        initView();
    }

    /****
     * 起点终点图标设置
     */
    private void setfromandtoMarker() {

        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpRequest httpRequest = new HttpRequest();
                String request = "?boxId="+531651109;
                //                                String request = "";
                list = httpRequest.resultJson(request,"GET","showLogistic");
                if (list != null){
                    longitude = list.get(0).get("longitude").toString();
                    latitude = list.get(0).get("latitude").toString();
                    System.out.println("经度："+longitude+"\n纬度："+latitude);
                }else {
                    System.out.println("list为空");

                }
            }
        });

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(RouteActivity.this);
        aMap.setOnMarkerClickListener(RouteActivity.this);
        aMap.setOnInfoWindowClickListener(RouteActivity.this);
        aMap.setInfoWindowAdapter(RouteActivity.this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }
    /**
     * 开始搜索路线规划方案
     */
    public void searchRouteResult(int routeType, int mode) {

//        mStartPoint = new LatLonPoint(39.942295, 116.335891); //起点

        if (mStartPoint == null) {
            Toast.makeText(RouteActivity.this, "起点未设置", Toast.LENGTH_LONG).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(RouteActivity.this, "终点未设置", Toast.LENGTH_LONG).show();
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
    }
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.getWalkColor();//轨迹颜色修改
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(RouteActivity.this, R.string.no_result, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RouteActivity.this, R.string.no_result, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RouteActivity.this,errorCode, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    private void initView(){

    }
    /**
     * 在线程中正常使用吐司
     **/
    private static Toast toast = null;
    public static void showToast(Context context, String text) {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            Looper.prepare();
            myLooper = Looper.myLooper();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
        if ( myLooper != null) {
            Looper.loop();
            myLooper.quit();
        }

    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //获取位置结果,判断AMapLocation对象不为空，当定位错误码类型为0时定位成功
            if (aMapLocation.getErrorCode() == 0) {
                Log.d("success","定位成功测试");
                //获取纬度
                aMapLocation.getLatitude();
                //获取经度
                aMapLocation.getLongitude();
                //获取地址
                aMapLocation.getAddress();
                //获取准确度信息
                aMapLocation.getAccuracy();
                //获取定位结果来源
                aMapLocation.getLocationType();
                onLocationChanged(aMapLocation);
            }else{
//                Toast.makeText(getContext(),"定位失败",Toast.LENGTH_SHORT).show();
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    };
}
