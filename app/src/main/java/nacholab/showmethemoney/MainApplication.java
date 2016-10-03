package nacholab.showmethemoney;

import android.accounts.Account;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orm.SugarContext;

import nacholab.showmethemoney.api.APIClient;
import nacholab.showmethemoney.storage.ActiveAndroidDal;
import nacholab.showmethemoney.storage.BaseDal;
import nacholab.showmethemoney.storage.SettingsManager;
import nacholab.showmethemoney.sync.SyncUtils;

public class MainApplication extends Application{

    private APIClient apiClient;
    private Account syncAccount;
    private BaseDal dal;
    private SettingsManager settings;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        settings = new SettingsManager(this);

//        dal = new SugarORMDal(this);
        dal = new ActiveAndroidDal(this);

        apiClient = new APIClient(this);

        syncAccount = SyncUtils.createSyncAccount(this);

//        ContentResolver contentResolver = getContentResolver();
//
//        Uri syncUri = SyncUtils.buildMainSyncUri();

//        ContentResolver.addPeriodicSync(
//                SyncAdapter.ACCOUNT,
//                AUTHORITY,
//                Bundle.EMPTY,
//                SYNC_INTERVAL);

    }

    public APIClient getApiClient() {
        return apiClient;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public Account getSyncAccount() {
        return syncAccount;
    }

    public BaseDal getDal() {
        return dal;
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public void setSettings(SettingsManager settings) {
        this.settings = settings;
    }
}
