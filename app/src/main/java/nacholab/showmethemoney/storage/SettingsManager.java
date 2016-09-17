package nacholab.showmethemoney.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

public class SettingsManager extends TrayPreferences {
    private static final String MODULE_NAME = "Settings";
    private static final int MODULE_VERSION = 1;

    private static final String LAST_SYNC = "last_sync";

    public SettingsManager(@NonNull Context context) {
        super(context, MODULE_NAME, MODULE_VERSION);
    }

    public void setLastSync(long lastSync){
        put(LAST_SYNC, lastSync);
    }

    public long getLastSync(){
        return getLong(LAST_SYNC,-1);
    }

}
