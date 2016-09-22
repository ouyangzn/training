package com.ouyangzn.module.testRealm.bean;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：
 */
public class Person extends RealmObject {

  @PrimaryKey private long id;
  @Required private String name;
  private RealmList<Dog> dogs; // Declare one-to-many relationships

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RealmList<Dog> getDogs() {
    return dogs;
  }

  public void setDogs(RealmList<Dog> dogs) {
    this.dogs = dogs;
  }

  @Override public String toString() {
    return "Person{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", dogs=" + dogs +
        '}';
  }
}
