package com.ouyangzn.module.testRealm.bean;

import io.realm.RealmObject;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：
 */
public class Dog extends RealmObject {

  public String name;
  public int age;

  @Override public String toString() {
    return "Dog{" +
        ", dogName='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}
