package com.ouyangzn.module.testMQTT;

import com.ouyangzn.utils.Log;
import java.net.URISyntaxException;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
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
public class MqttClient {

  private volatile static MqttClient sMqttClient;
  private final String TAG = MqttClient.class.getSimpleName();
  private MQTT mMQTT;
  private CallbackConnection mConnection;

  private MqttClient() {
    mMQTT = initMqtt();
  }

  public static MqttClient getInstance() {
    synchronized (MqttClient.class) {
      if (sMqttClient == null) {
        sMqttClient = new MqttClient();
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


  private MQTT initMqtt() {
    MQTT mqtt = new MQTT();
    try {
      mqtt.setHost("tcp://192.168.60.241:1884");
      mqtt.setClientId("ouyangzn-test");
      mqtt.setVersion("3.1.1");
      //mqtt.setCleanSession(false);//若设为false，MQTT服务器将持久化客户端会话的主体订阅和ACK位置，默认为true
      mqtt.setConnectAttemptsMax(3L);//客户端首次连接到服务器时，连接的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
      mqtt.setReconnectAttemptsMax(
          3L);//客户端已经连接到服务器，但因某种原因连接断开时的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
      mqtt.setReconnectDelay(5L);//首次重连接间隔毫秒数，默认为10ms
      mqtt.setReconnectDelayMax(10000L);//重连接间隔毫秒数，默认为30000ms
      mqtt.setTracer(new Tracer() {
        @Override public void debug(String message, Object... args) {
          log(String.format(message, args));
        }
      });
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return mqtt;
  }

  public MQTT getMqtt() {
    return mMQTT;
  }

  public void connect() {
    mConnection = mMQTT.callbackConnection();
    mConnection.listener(new Listener() {

      public void onDisconnected() {
        log("------------Listener.连接断开----------");
      }

      public void onConnected() {
        log("------------Listener.连接上服务器----------");
      }

      public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
        log("------------Listener.收到消息----------");
        log("topic = " + topic.toString() + ", content = " + new String(payload.toByteArray()));
        // You can now process a received message from a topic.
        // Once process execute the ack runnable.
        ack.run();
      }

      public void onFailure(Throwable value) {
        disconnect(); // a connection failure occured.
        log("------------Listener.连接断开：" + value);
      }
    });
    mConnection.connect(new Callback<Void>() {
      public void onFailure(Throwable value) {
        //result.failure(value); // If we could not connect to the server.
        log("------------connect.连接失败：" + value);
      }

      // Once we connect..
      public void onSuccess(Void v) {
        log("------------connect.连接成功----------");
        // Subscribe to a topic
        Topic[] topics = { new Topic("/mobile/broadcast", QoS.AT_LEAST_ONCE) };
        subscribeTopic(topics);
      }
    });
  }

  private void subscribeTopic(Topic[] topics) {
    mConnection.subscribe(topics, new Callback<byte[]>() {
      public void onSuccess(byte[] qoses) {
        // The result of the subcribe request.
        log("------------订阅topic成功");
      }

      public void onFailure(Throwable value) {
        log("------------订阅topic失败：" + value);
      }
    });
  }

  public void sendMsg(String msg) {
    // Send a message to a topic
    mConnection.publish("foo", msg.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
      public void onSuccess(Void v) {
        log("----------消息发送成功----------");
        // the pubish operation completed successfully.
      }

      public void onFailure(Throwable value) {
        log("----------消息发送失败:" + value);
        disconnect(); // publish failed.
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

  public void disconnect() {
    if (mConnection == null) return;
    // To disconnect..
    mConnection.disconnect(new Callback<Void>() {
      public void onSuccess(Void v) {
        log("----------关闭连接成功----------");
        // called once the connection is disconnected.
      }

      public void onFailure(Throwable value) {
        // Disconnects never fail.
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
}
