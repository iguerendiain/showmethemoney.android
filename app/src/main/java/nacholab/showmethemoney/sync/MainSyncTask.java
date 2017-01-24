package nacholab.showmethemoney.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.MainSyncData;

public class MainSyncTask extends AsyncTask<Void, Void, Void>{

    private static final String TAG = MainSyncTask.class.getSimpleName();
    private final Context ctx;
    private final Listener listener;

    public interface Listener{
        void onFinish();
    }

    public MainSyncTask(Context _ctx, Listener _listener) {
        super();
        ctx = _ctx;
        listener = _listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Log.d(TAG, "Running Main Sync");
            MainApplication app = (MainApplication) ctx.getApplicationContext();

            MainSyncData uploadPayload = app.getDal().buildUploadMainSyncData(app.getSettings().getLastSync());
            app.getApiClient().postMainSyncData(uploadPayload);
            app.getDal().setSynced(uploadPayload, true);
            app.getDal().cleanDeleted();
            MainSyncData downloadPayload = app.getApiClient().getMainSyncData();
            app.getDal().saveOrUpdateMainSyncData(downloadPayload);
            Log.d(TAG, "Main Sync succesful");

        } catch (IOException e) {
            Log.d(TAG, "Main Sync failed: "+e.getMessage());
            e.printStackTrace();
            if (listener!=null) {
                listener.onFinish();
            }
        }

        if (listener!=null) {
            listener.onFinish();
        }
        return null;
    }
}
