package com.hjt.phoneshow.ui.activity;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/18.
 * 这是用来测试WebView与Javascript交互
 */
//6.）代码简单解说
//
//（1.）js（HTML）访问Android（Java）端代码是通过jsObj对象实现的，调用jsObj对象中的函数，如： window.jsObj.actionFromJs()，这里的jsObj就是Native中添加接口的别名
//
//（2.）Android（Java）访问js（HTML）端代码是通过loadUrl函数实现的，访问格式如：mWebView.loadUrl("javascript:actionFromNative()");

public class WebviewActivity extends BaseActivity {
    private WebView mWebView;
    private TextView logTextView;
    private CameraManager cameraManager;

    @Override
    protected void initParams() {
        mWebView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 从assets目录下面的加载html
        mWebView.loadUrl("file:///android_asset/wx.html");
        mWebView.addJavascriptInterface(this, "wx");
        logTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 无参数调用js代码
                mWebView.loadUrl("javascript:actionFromNative()");
                // 传递参数调用js代码
                mWebView.loadUrl("javascript:actionFromNativeWithParam(" + "'come from Native'" + ")");
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @android.webkit.JavascriptInterface
    public void actionFromJs() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebviewActivity.this, "js调用了Native函数", Toast.LENGTH_SHORT).show();
                String text = logTextView.getText() + "\njs调用了Native函数";
                logTextView.setText(text);

            }
        });
        //测试打开手闪光灯
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraManager.setTorchMode("0", true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @android.webkit.JavascriptInterface
    public void actionFromJsWithParam(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebviewActivity.this, "js调用了Native函数传递参数：" + str, Toast.LENGTH_SHORT).show();
                String text = logTextView.getText() + "\njs调用了Native函数传递参数：" + str;
                logTextView.setText(text);
            }
        });

    }

}
