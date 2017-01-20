package nacholab.showmethemoney.sync;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;

public class MainSyncTask extends AsyncTask<Void, Void, Void>{

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
            MainApplication app = (MainApplication) ctx.getApplicationContext();

            MainSyncData uploadPayload = app.getDal().buildUploadMainSyncData(app.getSettings().getLastSync());
            app.getApiClient().postMainSyncData(uploadPayload);
            app.getDal().setSynced(uploadPayload, true);
            app.getDal().cleanDeleted();
            MainSyncData downloadPayload = app.getApiClient().getMainSyncData();
            app.getDal().saveOrUpdateMainSyncData(downloadPayload);

        } catch (IOException e) {
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
