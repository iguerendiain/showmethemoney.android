package nacholab.showmethemoney.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncAuthService extends Service {

    private SyncAuthenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new SyncAuthenticator(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
