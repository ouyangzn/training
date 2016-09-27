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

package com.ouyangzn.module.testCoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.lib.utils.ScreenUtils;
import com.ouyangzn.listener.QuickReturnOnScrollListener;
import com.ouyangzn.module.testRxJava.RxJavaActivity;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;
import java.util.ArrayList;

public class CoordinatorActivity extends BaseActivity implements View.OnClickListener,
    TilesFrameLayoutListener {

  private TilesFrameLayout mTilesFrameLayout;
  private BaseRecyclerViewAdapter<String> mAdapter;

  @Override
  public void onResume() {
    super.onResume();
    //mTilesFrameLayout.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    //mTilesFrameLayout.onPause();
  }

  @Override protected int getContentResId() {
    return R.layout.activity_coordinator;
    //return R.layout.content_coordinator;
  }

  @Override protected void initData() {
    ArrayList<String> list = new ArrayList<>();
    for (int i = 1; i < 31; i++) {
      list.add("测试数据" + i);
    }
    mAdapter = new RecyclerAdapter(R.layout.item_test_recycler, list);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    //setTitle("测试Coordinator");
    //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //setSupportActionBar(toolbar);
    //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    finish();
    //  }
    //});
    mTilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tiles_frame_layout);
    mTilesFrameLayout.setOnAnimationFinishedListener(this);

    findViewById(R.id.fab).setOnClickListener(this);
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    recyclerView.setAdapter(mAdapter);

    View header = findViewById(R.id.header);
    View footer = findViewById(R.id.footer);
    //recyclerView.addItemDecoration(new TopDecoration(header));
    //recyclerView.addOnScrollListener(new TopTrackListener(header));
    //recyclerView.addOnScrollListener(new BottomTrackListener(footer));
    QuickReturnOnScrollListener scrollListener = new QuickReturnOnScrollListener();
    scrollListener.addHeaderItem(new QuickReturnOnScrollListener.Item(header, ScreenUtils.dp2px(getApplicationContext(), 40), 0, 0, 150));
    scrollListener.addFooterItem(new QuickReturnOnScrollListener.Item(footer, 0, 0, ScreenUtils.dp2px(getApplicationContext(), 40), 150));
    recyclerView.addOnScrollListener(scrollListener);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab:
        Snackbar.make(v, "测试snackBar", Snackbar.LENGTH_LONG)
            .setAction("确定", new View.OnClickListener() {
              @Override public void onClick(View v) {
                toast("点击snackBar的确定");
              }
            })
            .show();
        Intent intent = new Intent(getApplicationContext(), RxJavaActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        break;
    }
  }

  @Override public void onAnimationFinished() {
    finish();
    overridePendingTransition(0, R.anim.anim_slide_bottom_out);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      mTilesFrameLayout.startAnimation();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}
