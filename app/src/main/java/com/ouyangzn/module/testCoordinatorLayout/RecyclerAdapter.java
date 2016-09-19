package com.ouyangzn.module.testCoordinatorLayout;

import com.ouyangzn.R;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Description：
 */
public class RecyclerAdapter extends BaseRecyclerViewAdapter<String> {

  public RecyclerAdapter(int layoutResId, List<String> data) {
    super(layoutResId, data);
  }

  @Override protected void convert(BaseViewHolder helper, String item) {
    helper.setText(R.id.string, item);
    helper.setOnClickListener(R.id.btn_click_me, new OnItemChildClickListener());
  }
}
