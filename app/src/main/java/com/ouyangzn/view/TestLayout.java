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

package com.ouyangzn.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/9/8.<br/>
 * Description：
 */
public class TestLayout extends LinearLayout {

  private static final String TAG = TestLayout.class.getSimpleName();

  public TestLayout(Context context) {
    super(context);
  }

  public TestLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TestLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    Log.d(TAG, "--------------onTouchEvent------------------");
    return super.onTouchEvent(event);
    //return true;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    Log.d(TAG, "--------------onInterceptTouchEvent------------------");
    //return true;
    return super.onInterceptTouchEvent(ev);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    Log.d(TAG, "--------------dispatchTouchEvent------------------");
    return super.dispatchTouchEvent(ev);
  }
}
