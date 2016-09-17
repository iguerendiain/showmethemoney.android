package nacholab.showmethemoney.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import nacholab.showmethemoney.MainApplication;

public class SyncService extends Service{

    private static SyncAdapter syncAdapter = null;
    private static final Object threadlock = new Object();

    @Override
    public void onCreate() {

        synchronized (threadlock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter((MainApplication)getApplication());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

}
