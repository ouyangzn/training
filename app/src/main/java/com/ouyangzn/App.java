package com.ouyangzn;

import android.app.Application;
import android.content.Context;
import com.ouyangzn.module.testRealm.db.MyRealmMigration;
import com.ouyangzn.utils.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.ouyangzn.module.testRealm.db.MyRealmMigration.DB_VERSION;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：
 */
public class App extends Application {

  private static App mApp;
  private Context mContext;
  private Realm mGlobalRealm;

  public static App getInstance() {
    return mApp;
  }

  public Realm getGlobalRealm() {
    return mGlobalRealm;
  }

  public Context getContext() {
    return mContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    mApp = this;
    mContext = this;
    initGlobalReaml();
  }

  private void initGlobalReaml() {
    mGlobalRealm = Realm.getInstance(new RealmConfiguration.Builder(mContext).name("globaldb.realm")
        // 密码必须64个字节
        .encryptionKey(
            "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ-0123456789".getBytes())
        .schemaVersion(DB_VERSION)
        .migration(new MyRealmMigration())
        //.deleteRealmIfMigrationNeeded()
        .build());
    Log.d("App", "----------数据库路径" + mGlobalRealm.getPath());
  }
}
