package com.ouyangzn.module.testHandlerRemove;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2017/4/26.<br/>
 * Description：
 */
public class HandlerRemoveActivity extends BaseActivity {

  private Handler mHandler = new Handler();
  private Runnable mRunnable = new Runnable() {
    @Override public void run() {
      Log.i(TAG, "----------即将被remove的消息-----------");
      mHandler.postDelayed(this, 2000);
    }
  };

  @Override protected int getContentResId() {
    return R.layout.activity_handler_remove;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  public void start(View view) {
    mHandler.postDelayed(mRunnable, 2000);
  }

  public void stop(View view) {
    mHandler.removeCallbacks(mRunnable);
  }
}
