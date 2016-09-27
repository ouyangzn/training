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

import com.ouyangzn.R;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Description：
 */
public class ChildClickRecyclerViewAdapter extends BaseRecyclerViewAdapter<String> {

  public ChildClickRecyclerViewAdapter(int layoutResId, List<String> data) {
    super(layoutResId, data);
  }

  @Override protected void convert(BaseViewHolder helper, String item) {
    helper.setText(R.id.string, item);
    helper.setOnClickListener(R.id.btn_click_me, new OnItemChildClickListener());
  }
}
