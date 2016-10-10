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

package com.ouyangzn.module.testRxJava;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.lib.utils.ThreadUtil;
import com.ouyangzn.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends BaseActivity {

  private List<Subscription> mSubscriptionList = new ArrayList<>();
  private RxJavaTest mRxJavaTest;
  private TextView mTvResult;
  private EditText mEtSearch;

  @Override protected void initData() {
    mRxJavaTest = new RxJavaTest(mContext);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle("rxJava测试");
    //TextView textView = getTextView();
    //ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_container);
    //viewGroup.addView(textView);
    mTvResult = (TextView) findViewById(R.id.tv_result);
    mEtSearch = (EditText) findViewById(R.id.et_search);
    initSearchView();
  }

  private void initSearchView() {
    Subscription subscribe = RxTextView.textChanges(mEtSearch)
        // 绑定textChanges事件必须使用主线程
        .subscribeOn(AndroidSchedulers.mainThread())
        .debounce(300, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .filter(new Func1<CharSequence, Boolean>() {
          @Override public Boolean call(CharSequence charSequence) {
            Log.d(TAG, "-----------filter.当前线程：" + Thread.currentThread().getName());
            int length = charSequence.length();
            return length > 0;
          }
        })
        .observeOn(Schedulers.io())
        .switchMap(new Func1<CharSequence, Observable<String>>() {
          @Override public Observable<String> call(final CharSequence charSequence) {
            return Observable.create(new Observable.OnSubscribe<String>() {
              @Override public void call(Subscriber<? super String> subscriber) {
                Log.d(TAG, "-----------switchMap.当前线程：" + Thread.currentThread().getName());
                ThreadUtil.sleep(3000);
                subscriber.onNext("搜索key为:" + charSequence.toString());
              }
            })/*.subscribeOn(Schedulers.newThread())*/;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            mTvResult.setText(s);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "----------出错啦:", throwable);
          }
        });
    mSubscriptionList.add(subscribe);
  }

  public void testFrom(View view) {
    mRxJavaTest.testFrom();
  }

  public void testBuffer(View view) {
    mRxJavaTest.testBuffer();
  }

  public void testMerge(View view) {
    mRxJavaTest.testMerge();
  }

  public void testMapOrSwitchMap(View view) {
    Button button = (Button) view;
    if (button.getTag() == null) {
      toast("测试SwitchMap");
      button.setText("测试Map");
      mRxJavaTest.testMapOrSwitchMap(false);
      view.setTag("");
    } else {
      button.setText("测试SwitchMap");
      toast("测试Map");
      mRxJavaTest.testMapOrSwitchMap(true);
      view.setTag(null);
    }
  }

  public void testUnsubcribe(View view) {
    testUnsubcribe(mRxJavaTest);
  }

  public void testChangeThread(View view) {
    Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext(getTestStr());
      }
    })
    // just创建的无法指定线程，运行在创建时的线程
    //Observable.just(getTestStr())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .filter(new Func1<CharSequence, Boolean>() {
          @Override public Boolean call(CharSequence charSequence) {
            Log.d(TAG, "-----------filter.当前线程：" + Thread.currentThread().getName());
            int length = charSequence.length();
            return length > 0;
          }
        })
        .subscribeOn(Schedulers.io())
        // 运行在Observable.create中的call方法之前，运行线程：事实是离下面的subscribeOn更近，受下面的影响
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            Log.d(TAG, "-----------doOnSubscribe.当前线程：" + Thread.currentThread().getName());
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.io())
        .switchMap(new Func1<CharSequence, Observable<String>>() {
          @Override public Observable<String> call(final CharSequence charSequence) {
            return Observable.create(new Observable.OnSubscribe<String>() {
              @Override public void call(Subscriber<? super String> subscriber) {
                Log.d(TAG, "-----------switchMap.当前线程：" + Thread.currentThread().getName());
                subscriber.onNext("搜索key为:" + charSequence.toString());
              }
            });
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            Log.d(TAG, "-----------subscribe.当前线程：" + Thread.currentThread().getName());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "----------出错啦:", throwable);
          }
        });
  }

  public void testConcatAndFirst(View view) {
    mRxJavaTest.testConcatAndFirst();
  }

  private String getTestStr() {
    Log.d(TAG, "-----------getTestStr.当前线程：" + Thread.currentThread().getName());
    return "测试字符串";
  }

  private void testUnsubcribe(RxJavaTest rxJavaTest) {
    Observable<Integer> observable = rxJavaTest.testUnsubscribe(5000);
    Subscription subscribe = observable.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        Log.d(TAG, "----------------testUnsubcribe.subscribe = " + integer);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.d(TAG, "----------------testUnsubcribe.throwable = " + throwable);
      }
    });
    mSubscriptionList.add(subscribe);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        finish();
      }
    }, 1000);
  }

  public void showDialog(View view) {
    DialogPlusBuilder builder = DialogPlus.newDialog(this);
    builder.setContentBackgroundResource(R.color.colorAccent)
        .setGravity(Gravity.CENTER).setContentHolder(new ViewHolder(getTextView())).create().show();
  }

  @NonNull private TextView getTextView() {
    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    TextView textView = new TextView(mContext.getApplicationContext());
    textView.setLayoutParams(params);
    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
    textView.setText("我是代码添加的文字");
    return textView;
  }

  @Override protected int getContentResId() {
    return R.layout.activity_rx_java;
  }

  @Override protected void onDestroy() {
    Log.d(TAG, "----------onDestroy----------");
    for (Subscription s : mSubscriptionList) {
      Log.d(TAG, "----------onDestroy.解除订阅----------");
      if (s != null && !s.isUnsubscribed()) {
        s.unsubscribe();
      }
    }
    mSubscriptionList.clear();
    mSubscriptionList = null;
    super.onDestroy();
  }
}
