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

package com.ouyangzn.module.testLoadingView;

import android.os.Bundle;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;

public class LoadingActivity extends BaseActivity {

  //@BindView(R.id.group_control) RadioGroup mRadioGroup;
  //@BindView(R.id.bezier) Bezier mBezier;

  @Override protected int getContentResId() {
    return R.layout.activity_test_loading;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {
    //mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    //  @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
    //    if (checkedId == R.id.radio_1) {
    //      mBezier.setMode(true);
    //    } else if (checkedId == R.id.radio_2){
    //      mBezier.setMode(false);
    //    }
    //  }
    //});
  }
}
