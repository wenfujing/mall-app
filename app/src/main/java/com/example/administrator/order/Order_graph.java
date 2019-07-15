package com.example.administrator.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.administrator.activity.R;

public class Order_graph extends Activity {
    WebView webView;
    private String url;
    private Button btn_updata;
    private ImageButton igBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_graph);

        btn_updata = findViewById(R.id.btn_update);
        webView = findViewById(R.id.webView_graph);
        igBack = (ImageButton)findViewById(R.id.order_graph_ib_back);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
//        WebView.getSettings().setSupportZoom(true);
//        WebView.getSettings().setBuiltInZoomControls(false);
//        WebView.getSettings().setDefaultFontSize(18);

        webView.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/ajax.jsp");
//        loadLocalHtml(url,ws);

        //点击刷新
        btn_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://zssxl.natapp1.cc/farmmall/jsp/ajax.jsp");
            }
        });

        //点击返回
        igBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadLocalHtml(String url, WebSettings ws ) {

        ws.setJavaScriptEnabled(true);//开启JavaScript支持

        // 允许使用数据库
        ws.setDatabaseEnabled(true);
        ws.setGeolocationEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        ws.setGeolocationDatabasePath(dir);
        ws.setDomStorageEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//
//        });

//        ws.setGeolocationEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                return true;
            }

            ;

            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                return true;
            }

            ;

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        webView.loadUrl(url);
    }
}
