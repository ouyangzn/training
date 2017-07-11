package com.ouyangzn.module.testJobScheduler;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import com.ouyangzn.utils.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class JobSchedulerService extends JobService {
  public JobSchedulerService() {
  }

  @Override public boolean onStartJob(JobParameters params) {
    String data = params.getExtras().getString("data");
    Log.d("JobSchedulerService", "----------data = " + data);
    jobFinished(params, false);
    return true;
  }

  @Override public boolean onStopJob(JobParameters params) {
    return false;
  }
}
