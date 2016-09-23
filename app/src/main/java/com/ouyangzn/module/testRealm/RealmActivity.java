package com.ouyangzn.module.testRealm;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.OnClick;
import com.ouyangzn.App;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.base.DividerItemDecoration;
import com.ouyangzn.module.testRealm.adapter.DogRecyclerAdapter;
import com.ouyangzn.module.testRealm.adapter.PersonRecyclerAdapter;
import com.ouyangzn.module.testRealm.bean.Dog;
import com.ouyangzn.module.testRealm.bean.Person;
import com.ouyangzn.utils.Log;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RealmActivity extends BaseActivity {

  @BindView(R.id.recycler_dog) RecyclerView mDogRecycler;
  @BindView(R.id.recycler_person) RecyclerView mPersonRecycler;
  @BindView(R.id.btn_insert_dog) Button mAddBtn;
  @BindView(R.id.btn_delete_dog) Button mDeleteBtn;
  @BindView(R.id.btn_query_dog) Button mQueryBtn;
  @BindView(R.id.btn_update_dog) Button mUpdateBtn;
  long beginTime;
  private Realm mRealm;
  private DogRecyclerAdapter mDogAdapter;
  private PersonRecyclerAdapter mPersonAdapter;

  @Override protected int getContentResId() {
    return R.layout.activity_realm;
  }

  @Override protected void initData() {
    mRealm = App.getInstance().getGlobalRealm();
    mDogAdapter = new DogRecyclerAdapter(new ArrayList<Dog>(0));
    mPersonAdapter = new PersonRecyclerAdapter(new ArrayList<Person>(0));
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle("RealmActivity");
    mDogRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    mDogRecycler.addItemDecoration(new DividerItemDecoration(mContext));
    mDogRecycler.setAdapter(mDogAdapter);

    mPersonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    mPersonRecycler.addItemDecoration(new DividerItemDecoration(mContext));
    mPersonRecycler.setAdapter(mPersonAdapter);
  }

  @OnClick({
      R.id.btn_insert_dog, R.id.btn_delete_dog, R.id.btn_update_dog, R.id.btn_query_dog,
      R.id.btn_insert_person, R.id.btn_delete_person, R.id.btn_update_person, R.id.btn_query_person
  }) void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_insert_dog:
        insertDog();
        break;
      case R.id.btn_delete_dog:
        deleteDog();
        break;
      case R.id.btn_update_dog:
        updateDog();
        break;
      case R.id.btn_query_dog:
        queryDog();
        break;
      case R.id.btn_insert_person:
        insertPerson();
        break;
      case R.id.btn_delete_person:
        deletePerson();
        break;
      case R.id.btn_update_person:
        updatePerson();
        break;
      case R.id.btn_query_person:
        queryPerson();
        break;
    }
  }

  private void updatePerson() {

  }

  private void deletePerson() {
    //mRealm.delete(Person.class);
    mRealm.executeTransactionAsync(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        boolean result =
            realm.where(Person.class).contains("name", "1").findAll().deleteAllFromRealm();
        Log.d(TAG, result ? "----------删除成功--------" : "-------删除失败---------");
      }
    });
  }

  private void insertPerson() {
    mRealm.executeTransactionAsync(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        //Person person = new Person();
        //person.setName("person");
        //person.setId(UUID.randomUUID().getLeastSignificantBits());
        //realm.copyToRealm(person);
        long time = System.currentTimeMillis();
        ArrayList<Person> list = new ArrayList<>();
        Person person;
        for (int i = 1; i < 100000; i++) {
          person = new Person();
          person.setName("person_" + i);
          person.setId(UUID.randomUUID().getLeastSignificantBits());
          list.add(person);
        }
        Log.d(TAG, "----------制造数据耗时：" + (System.currentTimeMillis() - time));
        beginTime = System.nanoTime();
        realm.copyToRealm(list);
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override public void onSuccess() {
        Log.d(TAG, "----------插入数据完整流程耗时：" + (System.currentTimeMillis() - beginTime));
        toast("数据插入成功");
      }
    }, new Realm.Transaction.OnError() {
      @Override public void onError(Throwable error) {
        toast("插入数据时出错");
        Log.d(TAG, "----------插入数据时出错:", error);
      }
    });
  }

  private void queryPerson() {
    RealmResults<Person> results =
        // 查询的表
        mRealm.where(Person.class)
            // name like '%person%'
            .contains("name", "person", Case.INSENSITIVE)
            // 异步查询所有数据
            .findAllAsync();
    results.addChangeListener(new RealmChangeListener<RealmResults<Person>>() {
      @Override public void onChange(RealmResults<Person> element) {
        // RealmResults不支持clear、addAll之类的操作
        System.gc();
        long total = Runtime.getRuntime().totalMemory(); // byte
        long m1 = Runtime.getRuntime().freeMemory();
        Log.d(TAG, "----------before,内存占用:" + (total - m1));
        List<Person> personList;
        if (element.size() > 30) {
          personList = new ArrayList<>(30);
          personList.addAll(element.subList(0, 30));
        } else {
          personList = new ArrayList<>(element.size());
          personList.addAll(element);
        }
        long total1 = Runtime.getRuntime().totalMemory();
        long m2 = Runtime.getRuntime().freeMemory();
        Log.d(TAG, "----------after,内存占用:" + (total1 - m2));
        Log.d(TAG, "----------queryPerson.result.size = " + personList.size());
        mPersonAdapter.resetData(personList.size() > 30 ? personList.subList(0, 30) : personList);
      }
    });
  }

  public void queryDog() {
    // 异步查询，预先返回的结果没数据
    final RealmResults<Dog> results = mRealm.where(Dog.class).findAllAsync();
    results.addChangeListener(new RealmChangeListener<RealmResults<Dog>>() {
      @Override public void onChange(RealmResults<Dog> element) {
        // RealmResults不支持clear、add之类的操作
        ArrayList<Dog> dogList = new ArrayList<>(results.size());
        dogList.addAll(results);
        Log.d(TAG, "----------queryDog.result = " + dogList);
        mDogAdapter.resetData(dogList);
      }
    });
  }

  public void insertDog() {
    mRealm.executeTransactionAsync(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        Dog dog = new Dog();
        dog.name = "dog" + new Random(10).nextInt(Integer.MAX_VALUE);
        realm.copyToRealm(dog);
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override public void onSuccess() {
        toast("数据插入成功");
      }
    }, new Realm.Transaction.OnError() {
      @Override public void onError(Throwable error) {
        toast("插入数据时出错");
        Log.d(TAG, "----------插入数据时出错:", error);
      }
    });
  }

  public void deleteDog() {

  }

  public void updateDog() {

  }
}
