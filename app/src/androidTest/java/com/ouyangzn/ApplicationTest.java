package com.ouyangzn;

import android.app.Application;
import android.test.ApplicationTestCase;
import java.util.ArrayList;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
  public ApplicationTest() {
    super(Application.class);
  }

  public void textFrom() {
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
        .flatMap(new Func1<String, Observable<String>>() {
          @Override public Observable<String> call(String s) {
            return Observable.just(getString(s));
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Action1<String>() {
          @Override public void call(String s) {
            System.out.print("----------------subscribe.result = " + s);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable e) {
            System.out.print("----------------出错:" + e);
          }
        });
  }

  private String firstString(String s) {
    System.out.print("----------------firstString = " + s);
    return s;
  }

  private String getString(String s) {
    System.out.print("----------------getString = " + s);
    return "s" + s;
  }
}