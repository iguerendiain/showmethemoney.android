package nacholab.showmethemoney.sync;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.MainSyncData;

public class MainSyncTask extends AsyncTask<Void, Void, Boolean>{

    public static final String TAG = MainSyncTask.class.getSimpleName();
    private final MainApplication app;

    public MainSyncTask(MainApplication _app) {
        super();
        app = _app;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Log.d(TAG, "Running Main Sync");
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
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        app.getOtto().post(new Event(result));
    }

    public class Event{
        public final boolean success;

        Event(boolean s){
            success = s;
        }
    }
}
