package com.ouyangzn.module.testCornerMarkText;

import com.ouyangzn.R;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import com.ouyangzn.view.CornerMarkText;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/19.<br/>
 * Description：
 */
public class RecyclerAdapter extends BaseRecyclerViewAdapter<String> {

  public RecyclerAdapter(int layoutResId, List<String> data) {
    super(layoutResId, data);
  }

  @Override protected void convert(BaseViewHolder holder, String item) {
    CornerMarkText text = (CornerMarkText) holder.getConvertView().findViewById(R.id.language);
    text.setText(item);
  }
}
