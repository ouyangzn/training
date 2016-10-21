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

package com.ouyangzn.module.testViewDrag;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.base.DividerItemDecoration;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import java.util.ArrayList;

public class DragActivity extends BaseActivity {

  @BindView(R.id.list_vertical) RecyclerView mRecyclerVertical;
  @BindView(R.id.list_horizontal) RecyclerView mRecyclerHorizontal;
  private BaseRecyclerViewAdapter<String> mAdapter;
  private BaseRecyclerViewAdapter<String> mAdapterHorizontal;

  @Override protected int getContentResId() {
    return R.layout.activity_view_drag;
  }

  @Override protected void initData() {
    ArrayList<String> list = new ArrayList<>();
    for (int i = 1; i < 11; i++) {
      list.add("测试数据" + i);
    }
    mAdapter = new RecyclerViewAdapter(list);
    mAdapterHorizontal = new RecyclerViewAdapter(list);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    mRecyclerVertical.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerVertical.addItemDecoration(new DividerItemDecoration(mContext));
    mRecyclerVertical.setAdapter(mAdapter);

    mRecyclerHorizontal.setLayoutManager(
        new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    mRecyclerHorizontal.addItemDecoration(new DividerItemDecoration(mContext));
    mRecyclerHorizontal.setAdapter(mAdapterHorizontal);
  }
}
