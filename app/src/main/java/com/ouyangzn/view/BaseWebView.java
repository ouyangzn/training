package com.ouyangzn.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.AttributeSet;

import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.ouyangzn.utils.Log;

public class BaseWebView extends WebView {

  private static final String TAG = BaseWebView.class.getSimpleName();

  private Listener mListener;
  private WebUrlLoadingInterface mWebUrlLoadingInterface;

  public BaseWebView(Context context) {
    this(context, null);
  }

  public BaseWebView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setListener(Listener listener) {
    this.mListener = listener;
  }

  public void setWebUrlLoadingInterface(WebUrlLoadingInterface webUrlLoadingInterface) {
    mWebUrlLoadingInterface = webUrlLoadingInterface;
  }

  private void init() {
    setWebViewClient(new MyWebViewClient());
    setWebChromeClient(new MyWebChromeClient());

    WebSettings webSettings = getSettings();
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setGeolocationEnabled(true);
    webSettings.setAllowFileAccess(true);
    webSettings.setAppCacheEnabled(true);

    addJavascriptInterface(new Native(), "JsBridge");

    setBackgroundColor(0xFFFFFFFF);
  }

  public interface Listener {
    void onProgressChanged(WebView view, int newProgress);

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    boolean shouldOverrideUrlLoading(WebView view, String url);

    void choosePhoto();

    /**
     * 隐藏分享按钮,
     * 注意只能在这里设置一个标识,用于下一步操作,通常是在 WebViewClient.onPageFinished中进行处理
     */
    void hideAndroidShare();

    void login();

    void contactInfo();

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
  }

  public interface WebUrlLoadingInterface {
    boolean shouldOverrideUrlLoading(WebView view, String url);
  }

  /**
   * 自定义WebChromeClient，做选择图片处理
   */
  private class MyWebChromeClient extends WebChromeClient {
    @Override public void onProgressChanged(WebView view, int newProgress) {
      super.onProgressChanged(view, newProgress);
      if (mListener != null) {
        mListener.onProgressChanged(view, newProgress);
      }
    }

    @Override public void onGeolocationPermissionsShowPrompt(String origin,
        GeolocationPermissions.Callback callback) {
      callback.invoke(origin, true, false);
      super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
    @Override public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
      AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
      builder.setMessage("测试信息:" + message)
          .setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override public void onClick(DialogInterface dialog, int which) {
              result.cancel();
            }
          })
          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              result.confirm();
            }
          })
          .show();
      return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
      AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
      builder.setMessage("测试信息:" + message)
          .setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override public void onClick(DialogInterface dialog, int which) {
              result.cancel();
            }
          })
          .show();
      return true;
    }
  }

  /**
   * 自定义WebViewClient，否则会自动跳转到系统的浏览器的
   */
  private class MyWebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      handler.proceed();
    }

    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
      if (mListener != null) {
        mListener.onPageStarted(view, url, favicon);
      }
    }

    @Override public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      if (mListener != null) {
        mListener.onPageFinished(view, url);
      }
      Log.d(TAG, "------------------onPageFinished------------");
      //view.loadUrl(
      //    "javascript:window.local_obj.showContentSource(document.getElementsByTagName('article')[0].innerText);");
      //view.loadUrl(
      //    "javascript:window.local_obj.showIconSource(document.getElementsByTagName('img')[0].src);");
    }

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (mWebUrlLoadingInterface != null) {
        if (mWebUrlLoadingInterface.shouldOverrideUrlLoading(view, url)) {
          return true;
        }
      }
      if (TextUtils.isEmpty(url)) {
        return false;
      }
      // 电话短信拦截
      if (url.startsWith("sms:") || url.startsWith("tel:")) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(intent);
        return true;
      }
      // 处理其他链接
      view.loadUrl(url);
      return true;
    }

    @Override public void onReceivedError(WebView view, int errorCode, String description,
        String failingUrl) {
      if (mListener != null) {
        mListener.onReceivedError(view, errorCode, description, failingUrl);
      }
    }

  }

  private class Native {

    @JavascriptInterface public void showContentSource(String content) {
      Log.d(TAG, "-----------showContentSource.content = " + content);
    }

    @JavascriptInterface public void showIconSource(Object content) {
      Log.d(TAG, "-----------showIconSource.content = " + content);
    }

  }
}
