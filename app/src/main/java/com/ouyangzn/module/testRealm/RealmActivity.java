package com.ouyangzn.module.testRealm;

import android.os.Bundle;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmActivity extends BaseActivity {

  private Realm mRealm;

  @Override protected int getContentResId() {
    return R.layout.activity_realm;
  }

  @Override protected void initData() {
    mRealm = Realm.getInstance(new RealmConfiguration.Builder(mContext).name("realmTest.rm")
        .encryptionKey("password".getBytes())
        .build());
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }
}
