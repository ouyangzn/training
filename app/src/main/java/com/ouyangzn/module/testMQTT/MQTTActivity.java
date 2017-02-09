package com.ouyangzn.module.testMQTT;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;

public class MQTTActivity extends BaseActivity {

  private MqttClient mMqttClient;
  private MqttClient2 mMqttClient2;

  @Override protected int getContentResId() {
    return R.layout.activity_mqtt;
  }

  @Override protected void initData() {
    mMqttClient = MqttClient.getInstance();
    mMqttClient2 = MqttClient2.getInstance();
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override protected void onDestroy() {
    mMqttClient.unsubscribeTopic("/mobile/broadcast");
    mMqttClient.disconnect();
    mMqttClient = null;
    mMqttClient2.unsubscribeTopic("/mobile/broadcast");
    mMqttClient2.closeConnect();
    mMqttClient2 = null;
    super.onDestroy();
  }

  @OnClick({
      R.id.btn_connect, R.id.btn_connect_2, R.id.btn_send_msg, R.id.btn_send_msg_2,
      R.id.btn_disconnect, R.id.btn_disconnect_2
  })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_connect: {
        mMqttClient.connect();
        break;
      }
      case R.id.btn_connect_2: {
        mMqttClient2.openConnect();
        mMqttClient2.subscribeTopic("/mobile/broadcast");
        break;
      }
      case R.id.btn_disconnect: {
        mMqttClient.disconnect();
        break;
      }
      case R.id.btn_disconnect_2: {
        mMqttClient2.closeConnect();
        break;
      }
      case R.id.btn_send_msg: {
        mMqttClient.sendMsg("/mobile/broadcast", "mMqttClient ---> hello world");
        break;
      }
      case R.id.btn_send_msg_2: {
        mMqttClient2.sendMsg("/mobile/broadcast", "mMqttClient2 say ---> hello world");
        break;
      }
    }
  }
}
