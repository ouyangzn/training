package com.ouyangzn.module.testRealm.db;

import com.ouyangzn.module.testRealm.bean.Dog;
import com.ouyangzn.module.testRealm.bean.Person;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：数据库版本更新时会被调用
 */
public class MyRealmMigration implements RealmMigration {

  public static final long DB_VERSION = 2;

  @Override public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
    RealmSchema schema = realm.getSchema();
    if (oldVersion == 1) {
      updateVersionTo2(schema);
      oldVersion++;
    }
    if (oldVersion == 2) {
      updateVersionTo3(schema);
    }
  }

  private void updateVersionTo3(RealmSchema schema) {
    schema.get(Dog.class.getSimpleName()).addField("age", int.class);
  }

  private void updateVersionTo2(RealmSchema schema) {
    schema.create(Person.class.getSimpleName())
        .addField("id", int.class, FieldAttribute.PRIMARY_KEY)
        .addField("name", String.class, FieldAttribute.REQUIRED)
        .addRealmListField("dogs", schema.get(Dog.class.getSimpleName()));
  }
}
