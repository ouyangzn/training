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
