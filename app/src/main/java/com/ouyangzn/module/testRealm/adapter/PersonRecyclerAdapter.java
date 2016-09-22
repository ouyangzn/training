package com.ouyangzn.module.testRealm.adapter;

import com.ouyangzn.R;
import com.ouyangzn.module.testRealm.bean.Person;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：
 */
public class PersonRecyclerAdapter extends BaseRecyclerViewAdapter<Person> {

  public PersonRecyclerAdapter(List<Person> data) {
    super(R.layout.item_realm_person, data);
  }

  @Override protected void convert(BaseViewHolder holder, Person item) {
    holder.setText(R.id.tv_person_name, item.getName());
  }
}
