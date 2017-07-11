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

import android.app.AlertDialog;
import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.Toast;
import com.ouyangzn.lib.utils.ThreadUtil;
import com.ouyangzn.utils.Log;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description：
 */
public class RxJavaTest extends AndroidTestCase {

  private static final String TAG = RxJavaTest.class.getSimpleName();
  private Context mContext;

  private int count = 0;

  public RxJavaTest() {
  }

  public RxJavaTest(Context context) {
    mContext = context.getApplicationContext();
  }

  public static void main(String[] args) {
    String str = "a\nb";
    System.out.println("----------str = " + str);
    String[] strings = str.split("\n");
    System.out.println("----------strings = " + Arrays.toString(strings));
  }

  public void testFrom() {
    Flowable.create(new FlowableOnSubscribe<String>() {
      @Override public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {

      }
    }, BackpressureStrategy.BUFFER).subscribe(new org.reactivestreams.Subscriber<String>() {
      @Override public void onSubscribe(Subscription s) {
        s.request(1);
      }

      @Override public void onNext(String s) {

      }

      @Override public void onError(Throwable t) {

      }

      @Override public void onComplete() {

      }
    });
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("");
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    list.add("4");
    Flowable.fromIterable(list).flatMap(new Function<String, Publisher<String>>() {
      @Override public Publisher<String> apply(@NonNull String s) throws Exception {
        count++;
        if (count == 3) {
          return Flowable.error(new RuntimeException("testFrom.抛个异常玩玩"));
        }
        return Flowable.just(firstString(s));
      }
    }).subscribe(new Consumer<String>() {
      @Override public void accept(@NonNull String s) throws Exception {
        Log.d(TAG, "----------------testFrom.subscribe.result = " + s);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(@NonNull Throwable throwable) throws Exception {
        Log.d(TAG, "----------------testFrom.出错:" + throwable);
      }
    });
    //Observable.from(list).flatMap(new Func1<String, Observable<String>>() {
    //  @Override public Observable<String> call(String s) {
    //    count++;
    //    if (count == 2) {
    //      return Observable.error(new RuntimeException("testFrom.抛个异常玩玩"));
    //    }
    //    return Observable.just(firstString(s));
    //  }
    //}).flatMap(new Func1<String, Observable<String>>() {
    //  @Override public Observable<String> call(String s) {
    //    return Observable.just(getString(s));
    //  }
    //}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Action1<String>() {
    //  @Override public void call(String s) {
    //    Log.d(TAG, "----------------testFrom.subscribe.result = " + s);
    //  }
    //}, new Action1<Throwable>() {
    //  @Override public void call(Throwable e) {
    //    Log.d(TAG, "----------------testFrom.出错:" + e);
    //  }
    //});
  }

  public void testBuffer() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    Flowable.fromIterable(list)
        .buffer(list.size())
        .flatMap(new Function<List<String>, Publisher<String>>() {
          @Override public Publisher<String> apply(@NonNull List<String> list) throws Exception {
            return Flowable.just(list.get(0));
      }
        })
        .subscribe();
    //Observable.from(list).flatMap(new Func1<String, Observable<String>>() {
    //  @Override public Observable<String> call(String s) {
    //    return Observable.just(firstString(s));
    //  }
    //}).buffer(list.size()).flatMap(new Func1<List<String>, Observable<String>>() {
    //  @Override public Observable<String> call(List<String> strings) {
    //    Log.d(TAG, "--------------testBuffer.strings = " + strings);
    //    //return Observable.just(getString(strings.get(strings.size() - 1)));
    //    return Observable.error(new Exception("testBuffer.抛个异常玩玩"));
    //  }
    //}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Action1<String>() {
    //  @Override public void call(String s) {
    //    Log.d(TAG, "----------------testBuffer.subscribe.result = " + s);
    //  }
    //}, new Action1<Throwable>() {
    //  @Override public void call(Throwable e) {
    //    Log.d(TAG, "----------------testBuffer.出错:" + e);
    //  }
    //});
  }

  public void testMerge() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
      @Override public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
        e.onNext("a");
        e.onNext("b");
        e.onComplete();
      }
    }).subscribe(new Observer<String>() {
      @Override public void onSubscribe(@NonNull Disposable d) {
        d.dispose();
      }

      @Override public void onNext(@NonNull String s) {

      }

      @Override public void onError(@NonNull Throwable e) {

      }

      @Override public void onComplete() {

      }
    });
    Flowable.merge(Flowable.just("1"), Flowable.just("2"))
        .subscribe(new org.reactivestreams.Subscriber<String>() {
          @Override public void onSubscribe(Subscription s) {

            s.request(1);
          }

          @Override public void onNext(String s) {

          }

          @Override public void onError(Throwable t) {

          }

          @Override public void onComplete() {

          }
        });
    Flowable.merge(Flowable.just("1"), Flowable.just("2")).subscribe(new Consumer<String>() {
      @Override public void accept(@NonNull String s) throws Exception {
        Log.d(TAG, "--------------testMerge = " + s);
      }
    });
    //Observable<String> from = Observable.from(list);
    //Observable.merge(from, Observable.just(new TestClass("test")))
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(Schedulers.io())
    //    .subscribe(new Action1<Object>() {
    //      @Override public void call(Object testClass) {
    //        Log.d(TAG, "--------------testClass = " + testClass);
    //      }
    //    });
    //Observable.merge(Observable.just(1), Observable.just(2))
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(Schedulers.io())
    //    .subscribe(new Action1<Integer>() {
    //      @Override public void call(Integer integer) {
    //        Log.d(TAG, "--------------testMerge = " + integer);
    //      }
    //    });
  }

  public void testMapOrSwitchMap(boolean map) {
    String[] strings = { "testMapOrSwitchMap-1", "testMapOrSwitchMap-2", "testMapOrSwitchMap-3" };
    Observable<String> observable = Observable.from(strings).subscribeOn(Schedulers.io());
    if (map) {
      observable.map(new Func1<String, String>() {
        @Override public String call(String s) {
          return s;
        }
      }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
        @Override public void call(String s) {
          Toast.makeText(mContext, "testMap-->subscribe结果: " + s, Toast.LENGTH_SHORT).show();
          Log.d(TAG, "----------testMap-->subscribe结果: " + s);
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          Toast.makeText(mContext, "testMap-->onError", Toast.LENGTH_SHORT).show();
          Log.e(TAG, "----------testMap-->onError:", throwable);
        }
      });
    } else {
      observable.switchMap(new Func1<String, Observable<String>>() {
        @Override public Observable<String> call(final String s) {
          return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
              ThreadUtil.sleep(2000);
              subscriber.onNext(s);
            }
          }).subscribeOn(Schedulers.newThread());
        }
      })
          // 如果跟上面分开写，会导致switchMap的作用失效
          .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
        @Override public void call(String s) {
          Toast.makeText(mContext, "testSwitchMap-->subscribe结果: " + s, Toast.LENGTH_SHORT).show();
          Log.d(TAG, "----------testSwitchMap-->subscribe结果: " + s);
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          Toast.makeText(mContext, "testSwitchMap-->onError", Toast.LENGTH_SHORT).show();
          Log.e(TAG, "----------testSwitchMap-->onError:", throwable);
        }
      });
    }
  }

  public Observable<Integer> testUnsubscribe(final long duration) {
    return Observable.just(1)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnNext(new Action1<Integer>() {
          @Override public void call(Integer integer) {
            try {
              Thread.sleep(duration);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
  }

  public void testConcatAndFirst() {
    Observable.concat(getTestFirstObservable(1), getTestFirstObservable(2),
        getTestFirstObservable(3)).filter(new Func1<Integer, Boolean>() {
      @Override public Boolean call(Integer integer) {
        return integer != 1;
      }
    }).first().subscribeOn(Schedulers.io()).subscribe(new Action1<Integer>() {
      @Override public void call(Integer o) {
        Log.d(TAG, "----------testFirst = " + o);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.e(TAG, "----------出错啦：", throwable);
      }
    });
  }

  private String firstString(String s) {
    Log.d(TAG, "----------------firstString = " + s);
    return s;
  }

  private String getString(String s) {
    Log.d(TAG, "----------------getString = " + s);
    return "s" + s;
  }

  private Observable<Integer> getTestFirstObservable(final int sleepTime) {
    return Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {
        Log.d(TAG, "----------getTestFirstObservable.sleepTime = " + sleepTime);
        ThreadUtil.sleep(sleepTime * 1000);
        Log.d(TAG, "----------getTestFirstObservable.sleepTime = " + sleepTime);
        subscriber.onNext(sleepTime);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }

  class TestClass {
    public String name;

    public TestClass(String name) {
      this.name = name;
    }
  }
}
