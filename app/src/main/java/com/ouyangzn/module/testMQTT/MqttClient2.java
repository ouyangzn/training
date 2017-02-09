package com.ouyangzn.module.testMQTT;

import com.ouyangzn.utils.Log;
import java.net.URISyntaxException;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.fusesource.mqtt.client.Tracer;

/**
 * Created by ouyangzn on 2017/2/7.<br/>
 * Description：
 */
public class MqttClient2 {
  private volatile static MqttClient2 sMqttClient;
  private final String SERVER = "tcp://192.168.60.241:1884";
  private final String TAG = MqttClient2.class.getSimpleName();
  private MQTT mMQTT;
  private CallbackConnection mConnection;
  private boolean mConnected = false;

  private MqttClient2() {
    initMqtt();
  }

  public static MqttClient2 getInstance() {
    synchronized (MqttClient2.class) {
      if (sMqttClient == null) {
        sMqttClient = new MqttClient2();
      }
    }
    return sMqttClient;
  }

  private void log(String content) {
    Log.d(TAG, content);
  }

  private void log(String content, Throwable throwable) {
    Log.e(TAG, content, throwable);
  }

  public boolean isConnected() {
    return mConnected;
  }

  public MQTT getMqtt() {
    return mMQTT;
  }

  public void openConnect() {
    if (isConnected()) return;

    mConnection = mMQTT.callbackConnection();
    mConnection.listener(new Listener() {

      public void onDisconnected() {
        log("------------与" + SERVER + "的连接断开----------");
        mConnected = false;
      }

      public void onConnected() {
        log("------------连接成功,server = " + SERVER);
        mConnected = true;
      }

      public void onFailure(Throwable value) {
        closeConnect();
        log("------------连接断开", value);
      }

      public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
        log("------------收到消息----------");
        log("topic = " + topic.toString() + ", content = " + new String(payload.toByteArray()));
        ack.run();
      }
    });
    mConnection.connect(new Callback<Void>() {
      public void onFailure(Throwable value) {
        log("------------connect.连接失败：", value);
        mConnected = false;
      }

      public void onSuccess(Void v) {
        mConnected = true;
      }
    });
  }

  public void closeConnect() {
    if (mConnection == null) return;

    mConnection.disconnect(new Callback<Void>() {
      public void onSuccess(Void v) {
        mConnected = false;
        log("----------关闭连接成功----------");
      }

      public void onFailure(Throwable value) {
      }
    });
  }

  public void subscribeTopic(String... topics) {
    if (topics == null) return;

    Topic[] topic = new Topic[topics.length];
    for (int i = 0; i < topics.length; i++) {
      topic[i] = new Topic(topics[i], QoS.AT_LEAST_ONCE);
    }
    mConnection.subscribe(topic, new Callback<byte[]>() {
      public void onSuccess(byte[] qoses) {
        // The result of the subcribe request.
        log("------------订阅topic成功");
      }

      public void onFailure(Throwable value) {
        log("------------订阅topic失败：", value);
      }
    });
  }

  public void unsubscribeTopic(String... topics) {
    if (topics == null || mConnection == null) return;

    UTF8Buffer[] topic = new UTF8Buffer[topics.length];
    for (int i = 0; i < topics.length; i++) {
      topic[i] = new UTF8Buffer(topics[i]);
    }
    mConnection.unsubscribe(topic, new Callback<Void>() {
      @Override public void onSuccess(Void value) {

      }

      @Override public void onFailure(Throwable value) {

      }
    });
  }

  public void sendMsg(String topic, String msg) {
    mConnection.publish(topic, msg.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
      public void onSuccess(Void v) {
        log("----------消息发送成功----------");
      }

      public void onFailure(Throwable value) {
        log("----------消息发送失败:", value);
      }
    });
  }

  public void sendMsg(String topic, byte[] msg) {
    mConnection.publish(topic, msg, QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
      public void onSuccess(Void v) {
        log("----------消息发送成功----------");
      }

      public void onFailure(Throwable value) {
        log("----------消息发送失败:", value);
      }
    });
  }

  private void initMqtt() {
    mMQTT = new MQTT();
    try {
      mMQTT.setHost(SERVER);
      mMQTT.setClientId("ouyangzn-test");
      mMQTT.setVersion(
          "3.1.1");//Set to "3.1.1" to use MQTT version 3.1.1. Otherwise defaults to the 3.1 protocol version.
      //mMQTT.setCleanSession(false);//若设为false，MQTT服务器将持久化客户端会话的主体订阅和ACK位置，默认为true
      mMQTT.setConnectAttemptsMax(3L);//客户端首次连接到服务器时，连接的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
      mMQTT.setReconnectAttemptsMax(
          3L);//客户端已经连接到服务器，但因某种原因连接断开时的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
      mMQTT.setReconnectDelay(5L);//首次重连接间隔毫秒数，默认为10ms
      mMQTT.setReconnectDelayMax(10000L);//重连接间隔毫秒数，默认为30000ms
      //mMQTT.setBlockingExecutor(ThreadUtil.getExecutor());
      mMQTT.setDispatchQueue(Dispatch.createQueue("mMQTT"));

      mMQTT.setTracer(new Tracer() {
        @Override public void debug(String message, Object... args) {
          log(String.format(message, args));
        }
      });
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
