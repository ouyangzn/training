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
