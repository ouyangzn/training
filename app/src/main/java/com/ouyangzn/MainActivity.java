package com.ouyangzn;

import android.os.Bundle;
import android.view.View;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.module.testCoordinatorLayout.CoordinatorActivity;
import com.ouyangzn.module.testCornerMarkText.TestCornerMarkActivity;
import com.ouyangzn.module.testFlexboxLayout.FlexboxLayoutActivity;
import com.ouyangzn.module.testRecyclerView.RecyclerViewActivity;
import com.ouyangzn.module.testRxJava.RxJavaActivity;
import com.ouyangzn.module.testViewDrag.ViewDragActivity;
import com.ouyangzn.module.testViewTouchDispatcher.DispatcherActivity;
import com.ouyangzn.module.testWebView.WebActivity;
import com.ouyangzn.utils.Log;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "------------------getName = " + MainActivity.class.getName());
    Log.d(TAG, "------------------getSimpleName = " + MainActivity.class.getSimpleName());
    Log.d(TAG, "------------------getCanonicalName = " + MainActivity.class.getCanonicalName());
  }

  @Override protected int getContentResId() {
    return R.layout.activity_main;
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override protected void initData() {
  }

  public void testRxJava(View view) {
    openActivity(RxJavaActivity.class);
  }

  public void testRecyclerView(View view) {
    openActivity(RecyclerViewActivity.class);
  }

  public void testWebView(View view) {
    openActivity(WebActivity.class);
  }

  public void testFlexboxLayout(View view) {
    openActivity(FlexboxLayoutActivity.class);
  }

  public void testCoordinatorLayout(View view) {
    openActivity(CoordinatorActivity.class);
  }

  public void testDispatcher(View view) {
    openActivity(DispatcherActivity.class);
  }

  public void testTopDragLayout(View view) {
    openActivity(ViewDragActivity.class);
  }

  public void testCornerMarkText(View view) {
    openActivity(TestCornerMarkActivity.class);
  }
}
