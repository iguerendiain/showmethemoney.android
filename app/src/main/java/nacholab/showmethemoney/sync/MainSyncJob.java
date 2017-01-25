package nacholab.showmethemoney.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.squareup.otto.Subscribe;

import nacholab.showmethemoney.MainApplication;

public class MainSyncJob extends JobService {

    public static final int CYCLE_TIME = 60 * 5 * 1000;

    private MainSyncTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(MainSyncTask.TAG, "Starting from Scheduled Job");
        task = new MainSyncTask((MainApplication) getApplicationContext());
        task.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        task.cancel(true);
        return false;
    }

    @Subscribe
    public void onSyncFinish(MainSyncTask.Event ev) {
        Log.d(MainSyncTask.TAG, "Finished from Scheduled Job");
        jobFinished(null, false);
    }
}
