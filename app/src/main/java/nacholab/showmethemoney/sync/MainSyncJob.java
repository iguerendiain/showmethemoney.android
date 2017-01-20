package nacholab.showmethemoney.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class MainSyncJob extends JobService implements MainSyncTask.Listener {

    public static final int CYCLE_TIME = 60 * 5 * 1000;

    private MainSyncTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        task = new MainSyncTask(this, this);
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
        jobFinished(null, false);
    }
}
