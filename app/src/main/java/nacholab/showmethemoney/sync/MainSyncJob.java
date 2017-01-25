package nacholab.showmethemoney.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MainSyncJob extends JobService implements MainSyncTask.Listener {

    public static final int CYCLE_TIME = 60 * 5 * 1000;

    private MainSyncTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(MainSyncTask.TAG, "Starting from Scheduled Job");
        task = new MainSyncTask(getApplicationContext(), this);
        task.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        task.cancel(true);
        return false;
    }

    @Override
    public void onFinish() {
        Log.d(MainSyncTask.TAG, "Finished from Scheduled Job");
        jobFinished(null, false);
    }
}
