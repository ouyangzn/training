/*
 * Copyright (c) 2016.  ouyangzn   <email : ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ouyangzn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.module.testCoordinatorLayout.CoordinatorActivity;
import com.ouyangzn.module.testCornerMarkText.TestCornerMarkActivity;
import com.ouyangzn.module.testEMUINotification.NotificationActivity;
import com.ouyangzn.module.testFlexboxLayout.FlexboxLayoutActivity;
import com.ouyangzn.module.testLoadingView.LoadingActivity;
import com.ouyangzn.module.testMQTT.MQTTActivity;
import com.ouyangzn.module.testRealm.RealmActivity;
import com.ouyangzn.module.testRecyclerView.RecyclerViewActivity;
import com.ouyangzn.module.testRefreshLayout.RefreshLayoutActivity;
import com.ouyangzn.module.testRxJava.RxJavaActivity;
import com.ouyangzn.module.testViewDrag.DragActivity;
import com.ouyangzn.module.testViewDrag.TopDragActivity;
import com.ouyangzn.module.testViewTouchDispatcher.DispatcherActivity;
import com.ouyangzn.module.testWebView.WebActivity;
import com.ouyangzn.utils.Log;

public class MainActivity extends BaseActivity {

  @Override protected int getContentResId() {
    return R.layout.activity_main;
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override protected void initData() {
    Log.d(TAG, "------------------getName = " + MainActivity.class.getName());
    Log.d(TAG, "------------------getSimpleName = " + MainActivity.class.getSimpleName());
    Log.d(TAG, "------------------getCanonicalName = " + MainActivity.class.getCanonicalName());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
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
    openActivity(TopDragActivity.class);
  }

  public void testDragLayout(View view) {
    openActivity(DragActivity.class);
  }

  public void testCornerMarkText(View view) {
    openActivity(TestCornerMarkActivity.class);
  }

  public void testRealm(View view) {
    openActivity(RealmActivity.class);
  }

  public void testLoadingView(View view) {
    openActivity(LoadingActivity.class);
  }

  public void testRefreshLayout(View view) {
    openActivity(RefreshLayoutActivity.class);
  }

  public void testMQTT(View view) {
    openActivity(MQTTActivity.class);
  }

  public void testEMUINotify(View view) {
    openActivity(NotificationActivity.class);
  }
}
