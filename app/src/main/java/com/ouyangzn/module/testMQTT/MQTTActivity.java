package com.ouyangzn.module.testMQTT;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;

public class MQTTActivity extends BaseActivity {

  private MqttClient mMqttClient;

  @Override protected int getContentResId() {
    return R.layout.activity_mqtt;
  }

  @Override protected void initData() {
    mMqttClient = MqttClient.getInstance();
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @OnClick({ R.id.btn_connect, R.id.btn_send_msg, R.id.btn_disconnect })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_connect: {
        mMqttClient.connect();
        break;
      }
      case R.id.btn_disconnect: {
        mMqttClient.disconnect();
        break;
      }
      case R.id.btn_send_msg: {
        mMqttClient.sendMsg("hello world");
        break;
      }
    }
  }
}
