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
