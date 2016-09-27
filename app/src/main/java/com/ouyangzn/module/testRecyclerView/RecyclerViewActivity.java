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

package com.ouyangzn.module.testRecyclerView;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import java.util.ArrayList;

public class RecyclerViewActivity extends BaseActivity implements
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemChildClickListener {

  private RecyclerView mRecyclerView;
  private BaseRecyclerViewAdapter<String> mAdapter;

  @Override protected int getContentResId() {
    return R.layout.activity_recycler_view;
  }

  @Override protected void initData() {
    ArrayList<String> list = new ArrayList<>();
    for (int i = 1; i < 31; i++) {
      list.add("测试数据" + i);
    }
    mAdapter = new ChildClickRecyclerViewAdapter(R.layout.item_test_recycler, list);
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnRecyclerViewItemChildClickListener(this);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle("recyclerView测试");

    mRecyclerView = (RecyclerView) findViewById(R.id.list);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onItemClick(View view, int position) {
    toast("点击了第" + (position + 1) + "项");
  }

  @Override public boolean onItemLongClick(View view, int position) {
    toast("长按第" + (position + 1) + "项");
    return true;
  }

  @Override public void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position) {
    switch (view.getId()) {
      case R.id.btn_click_me:
        toast("点击了第" + (position + 1) + "项的按钮");
        break;
      default:
        toast("点击了第" + (position + 1) + "项的其他控件");
        break;
    }
  }
}
