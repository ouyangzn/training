package com.ouyangzn.module.testJobScheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.utils.Log;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class TestJobSchedulerActivity extends BaseActivity {

  @Override protected int getContentResId() {
    return R.layout.activity_test_job_scheduler;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  public void sendScheduler(View v) {
    if (SDK_INT >= LOLLIPOP) {
      JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
      ComponentName component = new ComponentName(this, JobSchedulerService.class);
      JobInfo.Builder job = new JobInfo.Builder(1, component);
      job.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
      // 5s发送一次job---------不能5s，正常情况下必须大于15分钟(MIN_PERIOD_MILLIS = 15 * 60 * 1000L)
      job.setPeriodic(15 * 60 * 1000L);
      PersistableBundle bundle = new PersistableBundle();
      bundle.putString("data", "hello, jobScheduler");
      job.setExtras(bundle);
      int result;
      if ((result = jobScheduler.schedule(job.build())) < 0) {
        Log.w(TAG, "任务发送失败, result = " + result);
      }
      // -------反射也不能改变最小间隔时间，直接导致job失效-------
      //try {
      //  JobInfo.Builder builder = new JobInfo.Builder(1, component);
      //  builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
      //  builder.setPeriodic(5000);
      //  PersistableBundle bundle = new PersistableBundle();
      //  bundle.putString("data", "hello, jobScheduler");
      //  builder.setExtras(bundle);
      //  Class<?> clazz = Class.forName("android.app.job.JobInfo");
      //  Constructor<?> constructor = clazz.getDeclaredConstructor(JobInfo.Builder.class);
      //  constructor.setAccessible(true);
      //  JobInfo job = (JobInfo) constructor.newInstance(builder);
      //  Field field = clazz.getDeclaredField("MIN_PERIOD_MILLIS");
      //  field.setAccessible(true);
      //  field.setLong(job, 1000);
      //  int result;
      //  if ((result = jobScheduler.schedule(job)) <= 0) {
      //    Log.w(TAG, "任务发送失败, result = " + result);
      //  }
      //} catch (ClassNotFoundException e) {
      //  e.printStackTrace();
      //} catch (NoSuchFieldException e) {
      //  e.printStackTrace();
      //} catch (InstantiationException e) {
      //  e.printStackTrace();
      //} catch (IllegalAccessException e) {
      //  e.printStackTrace();
      //} catch (NoSuchMethodException e) {
      //  e.printStackTrace();
      //} catch (InvocationTargetException e) {
      //  e.printStackTrace();
      //}
    }
  }
}
