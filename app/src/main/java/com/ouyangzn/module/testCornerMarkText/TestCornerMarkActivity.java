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

package com.ouyangzn.module.testCornerMarkText;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.base.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class TestCornerMarkActivity extends BaseActivity {

  @BindView(R.id.recycler) RecyclerView mRecyclerView;
  private RecyclerAdapter mRecyclerAdapter;
  //@BindView(R.id.listview) ListView mListView;
  //private ListViewAdapter mListViewAdapter;

  @Override protected int getContentResId() {
    return R.layout.activity_test_corner_mark;
  }

  @Override protected void initData() {
    mRecyclerAdapter = new RecyclerAdapter(R.layout.item_corner_mark_text, getTestData());
    //mListViewAdapter = new ListViewAdapter(mContext, getTestData());
  }

  @Override protected void initView(Bundle savedInstanceState) {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
    mRecyclerView.setAdapter(mRecyclerAdapter);
    //mListView.setAdapter(mListViewAdapter);

  }

  private List<String> getTestData() {
    ArrayList<String> list = new ArrayList<>();
    list.add("java");
    list.add("c");
    list.add("c++");
    list.add("HTML");
    list.add("javascript");
    list.add("php");
    list.add("ruby");
    list.add("python");
    list.add("swift");
    list.add("object-c");
    list.add("abcdefghijklmnopqrstuvwxyz");
    list.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    list.add("object-c");
    list.add("swift");
    list.add("python");
    list.add("ruby");
    list.add("php");
    list.add("javascript");
    list.add("HTML");
    list.add("c++");
    list.add("c");
    list.add("java");
    return list;
  }
}
