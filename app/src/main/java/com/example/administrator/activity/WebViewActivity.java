package com.example.administrator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @ClassName WebViewActivity
 * @Date 2019-07-02 15:39
 **/

public class WebViewActivity extends Activity implements View.OnClickListener {
    private WebView webView;
    private WebView wvMap;
    private ImageButton igBack;
    private Button btnUpdateEn;
    private Button btnUpdateMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }
    public void initView(){
        webView = (WebView)findViewById(R.id.web_web);
        wvMap = (WebView)findViewById(R.id.webView_map);
        igBack = (ImageButton)findViewById(R.id.webview_ib_back);
        btnUpdateEn = (Button)findViewById(R.id.btn_update_en);
        btnUpdateMap = (Button)findViewById(R.id.btn_update_map);

        //返回按钮
        igBack.setOnClickListener(this);
        //更新环境温湿度折线图
        btnUpdateEn.setOnClickListener(this);
        //更新地图
        btnUpdateMap.setOnClickListener(this);

        //加载环境温湿度折线图
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/enviroment.jsp");

        //加载地图
        wvMap.getSettings().setJavaScriptEnabled(true);
        wvMap.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/map.jsp");
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.webview_ib_back:
                finish();
                break;
            case R.id.btn_update_en:
                webView.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/enviroment.jsp");
                break;
            case R.id.btn_update_map:
                wvMap.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/map.jsp");
                break;

        }
    }
}
