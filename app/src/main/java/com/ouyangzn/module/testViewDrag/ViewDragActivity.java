package com.ouyangzn.module.testViewDrag;

import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.utils.Log;

public class ViewDragActivity extends BaseActivity {

  @Override protected int getContentResId() {
    return R.layout.activity_view_drag;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {
    View view = findViewById(R.id.layout);
    view.setClickable(true);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
          case MotionEvent.ACTION_DOWN:
            Log.d(TAG, "-----------ACTION_DOWN-------------");
            break;
          case MotionEvent.ACTION_MOVE:
            Log.d(TAG, "-----------ACTION_MOVE-------------");
            break;
          case MotionEvent.ACTION_UP:
            Log.d(TAG, "-----------ACTION_UP-------------");
            break;
        }
        return false;
      }
    });
  }


}
