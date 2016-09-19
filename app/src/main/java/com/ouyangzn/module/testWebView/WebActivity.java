package com.ouyangzn.module.testWebView;

import android.os.Bundle;
import android.webkit.WebView;
import butterknife.BindView;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;

public class WebActivity extends BaseActivity {

  @BindView(R.id.web) WebView mWebView;

  @Override protected int getContentResId() {
    return R.layout.activity_web;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle("测试webView");
    mWebView.loadUrl("https://www.baidu.com/");

  }
}
