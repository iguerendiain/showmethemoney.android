package nacholab.showmethemoney.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class CurrencyUpdaterJob extends JobService implements CurrencyUpdaterTask.Listener {

    public static final int CYCLE_TIME = 3600 * 1000;

    private CurrencyUpdaterTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        task = new CurrencyUpdaterTask(getApplicationContext(), this);
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
