package nacholab.showmethemoney;

import android.accounts.Account;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.facebook.stetho.Stetho;

import nacholab.showmethemoney.api.APIClient;
import nacholab.showmethemoney.storage.ActiveAndroidDal;
import nacholab.showmethemoney.storage.BaseDal;
import nacholab.showmethemoney.storage.SessionManager;
import nacholab.showmethemoney.storage.SettingsManager;
import nacholab.showmethemoney.sync.CurrencyUpdaterJob;
import nacholab.showmethemoney.sync.CurrencyUpdaterTask;
import nacholab.showmethemoney.sync.MainSyncJob;
import nacholab.showmethemoney.sync.MainSyncTask;

public class MainApplication extends Application{

    private APIClient apiClient;
    private BaseDal dal;
    private SettingsManager settings;
    private SessionManager session;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        settings = new SettingsManager(this);
        session = new SessionManager(this);
        dal = new ActiveAndroidDal(this);
        apiClient = new APIClient(this);

        scheduleCurrencyUpdate();
        scheduleMainSync();

        // Update all on start
        if (session.isAuthenticated()) {
            downloadCurrencyAndDoMainSync();
        }
    }

    public void downloadCurrencyAndDoMainSync(){
        new CurrencyUpdaterTask(this, new CurrencyUpdaterTask.Listener() {
            @Override
            public void onFinish() {
                new MainSyncTask(MainApplication.this, null).execute();
            }
        }).execute();
    }

    private void scheduleCurrencyUpdate(){
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(1,new ComponentName(this, CurrencyUpdaterJob.class));

        jobBuilder.setPeriodic(CurrencyUpdaterJob.CYCLE_TIME);
        JobInfo currencyUpdaterJob = jobBuilder.build();

        jobScheduler.schedule(currencyUpdaterJob);
    }

    private void scheduleMainSync(){
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(1,new ComponentName(this, MainSyncJob.class));

        jobBuilder.setPeriodic(MainSyncJob.CYCLE_TIME);
        JobInfo mainSyncJob = jobBuilder.build();

        jobScheduler.schedule(mainSyncJob);
    }

    public APIClient getApiClient() {
        return apiClient;
    }

    public BaseDal getDal() {
        return dal;
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public SessionManager getSession() {
        return session;
    }
}
