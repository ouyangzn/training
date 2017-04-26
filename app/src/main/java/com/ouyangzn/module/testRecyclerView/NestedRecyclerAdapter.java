package com.ouyangzn.module.testRecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.module.testMQTT.MQTTActivity;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by ouyangzn on 2017/2/12.<br/>
 * Description：
 */
public class NestedRecyclerAdapter extends BaseRecyclerViewAdapter<String> {

  private Activity mActivity;

  public NestedRecyclerAdapter(Activity activity, List<String> data) {
    super(R.layout.item_nested_recycler, data);
    mActivity = activity;
  }

  @Override protected void convert(BaseViewHolder holder, String item) {
    holder.setText(R.id.tv_nested, item);
    RecyclerView recyclerView = holder.getView(R.id.recycler_nested);
    NestedAdapter adapter = new NestedAdapter(R.layout.item_test_recycler, mData);
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setAdapter(adapter);
    adapter.setOnRecyclerViewItemChildClickListener(new OnRecyclerViewItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseRecyclerViewAdapter adapter, View view, int position) {
        mActivity.startActivity(new Intent(mActivity.getApplicationContext(), MQTTActivity.class));
      }
    });
  }

  class NestedAdapter extends BaseRecyclerViewAdapter<String> {

    public NestedAdapter(int layoutResId, List<String> data) {
      super(layoutResId, data);
    }

    @Override protected void convert(BaseViewHolder holder, String item) {
      holder.setText(R.id.string, item);
      holder.setOnClickListener(R.id.btn_click_me, new OnItemChildClickListener());
    }
  }
}
