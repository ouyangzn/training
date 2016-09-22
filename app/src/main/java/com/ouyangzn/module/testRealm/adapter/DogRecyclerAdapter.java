package com.ouyangzn.module.testRealm.adapter;

import com.ouyangzn.R;
import com.ouyangzn.module.testRealm.bean.Dog;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：
 */
public class DogRecyclerAdapter extends BaseRecyclerViewAdapter<Dog> {

  public DogRecyclerAdapter(List<Dog> data) {
    super(R.layout.item_realm_dog, data);
  }

  @Override protected void convert(BaseViewHolder holder, Dog item) {
    holder.setText(R.id.tv_dog_name, item.name);
  }
}
