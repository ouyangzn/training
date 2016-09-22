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
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class RealmActivity extends BaseActivity {

  @BindView(R.id.recycler_dog) RecyclerView mDogRecycler;
  @BindView(R.id.recycler_person) RecyclerView mPersonRecycler;
  @BindView(R.id.btn_insert_dog) Button mAddBtn;
  @BindView(R.id.btn_delete_dog) Button mDeleteBtn;
  @BindView(R.id.btn_query_dog) Button mQueryBtn;
  @BindView(R.id.btn_update_dog) Button mUpdateBtn;
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

  }

  private void insertPerson() {
    mRealm.executeTransactionAsync(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        Person person = new Person();
        person.setName("Person" + new Random(10).nextInt(Integer.MAX_VALUE));
        person.setId(UUID.randomUUID().getLeastSignificantBits());
        realm.copyToRealm(person);
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

  private void queryPerson() {
    final RealmResults<Person> results = mRealm.where(Person.class).findAllAsync();
    results.addChangeListener(new RealmChangeListener<RealmResults<Person>>() {
      @Override public void onChange(RealmResults<Person> element) {
        // RealmResults不支持clear、add之类的操作
        ArrayList<Person> personList = new ArrayList<>(results.size());
        personList.addAll(results);
        Log.d(TAG, "----------queryPerson.result = " + personList);
        mPersonAdapter.resetData(personList);
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
