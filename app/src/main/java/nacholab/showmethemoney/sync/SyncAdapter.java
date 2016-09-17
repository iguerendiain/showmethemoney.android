package nacholab.showmethemoney.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.MainSyncData;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private final MainApplication app;

    public SyncAdapter(MainApplication _app) {
        super(_app, true, true);
        app = _app;
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        MainSyncData uploadPayload = app.getDal().buildUploadMainSyncData(app.getSettings().getLastSync());
        app.getApiClient().postMainSyncData(uploadPayload);
        app.getDal().setSynced(uploadPayload, true);
        MainSyncData downloadPayload = app.getApiClient().getMainSyncData();
        app.getDal().saveOrUpdateMainSyncData(downloadPayload);
    }

}
