package com.ouyangzn.module.testRxJava;

import android.content.Context;
import android.test.AndroidTestCase;

import android.widget.Toast;
import com.ouyangzn.utils.Log;
import com.ouyangzn.lib.utils.ThreadUtil;
import java.util.ArrayList;
import java.util.List;
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

  private final String TAG = RxJavaTest.class.getSimpleName();
  private Context mContext;

  private int count = 0;

  public RxJavaTest() {
  }

  public RxJavaTest(Context context) {
    mContext = context.getApplicationContext();
  }

  public void testFrom() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    Observable.from(list)
        .flatMap(new Func1<String, Observable<String>>() {
          @Override public Observable<String> call(String s) {
            count++;
            if (count == 2) {
              return Observable.error(new RuntimeException("testFrom.抛个异常玩玩"));
            }
            return Observable.just(firstString(s));
          }
        })
        .flatMap(new Func1<String, Observable<String>>() {
          @Override public Observable<String> call(String s) {
            return Observable.just(getString(s));
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            Log.d(TAG, "----------------testFrom.subscribe.result = " + s);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable e) {
            Log.d(TAG, "----------------testFrom.出错:" + e);
          }
        });
  }

  public void testBuffer() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    Observable.from(list)
        .flatMap(new Func1<String, Observable<String>>() {
          @Override public Observable<String> call(String s) {
            return Observable.just(firstString(s));
          }
        })
        .buffer(list.size())
        .flatMap(new Func1<List<String>, Observable<String>>() {
          @Override public Observable<String> call(List<String> strings) {
            Log.d(TAG, "--------------testBuffer.strings = " + strings);
            //return Observable.just(getString(strings.get(strings.size() - 1)));
            return Observable.error(new Exception("testBuffer.抛个异常玩玩"));
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            Log.d(TAG, "----------------testBuffer.subscribe.result = " + s);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable e) {
            Log.d(TAG, "----------------testBuffer.出错:" + e);
          }
        });
  }

  public void testMerge() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    list.add("3");
    Observable<String> from = Observable.from(list);
    Observable.merge(from, Observable.just(new TestClass("test")))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Action1<Object>() {
          @Override public void call(Object testClass) {
            Log.d(TAG, "--------------testClass = " + testClass);
          }
        });
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
    String[] strings = {"testMapOrSwitchMap-1", "testMapOrSwitchMap-2", "testMapOrSwitchMap-3"};
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

  private String firstString(String s) {
    Log.d(TAG, "----------------firstString = " + s);
    return s;
  }

  private String getString(String s) {
    Log.d(TAG, "----------------getString = " + s);
    return "s" + s;
  }

  class TestClass {
    public String name;

    public TestClass(String name) {
      this.name = name;
    }
  }

}
