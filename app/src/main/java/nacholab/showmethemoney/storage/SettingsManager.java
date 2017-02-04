package nacholab.showmethemoney.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

public class SettingsManager extends TrayPreferences {

    public static double SUGGESTED_TAGS_LOCATION_MARGIN = .005d;
    public static float SUGGESTED_TAGS_AMOUNT_MARGIN_PERCENT = .2f;
    public static int SUGGESTED_TAGS_TIME_MARGIN_HOURS = 1;

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
