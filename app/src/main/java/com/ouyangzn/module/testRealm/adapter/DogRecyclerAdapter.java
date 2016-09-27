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
