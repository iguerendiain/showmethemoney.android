package nacholab.showmethemoney.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

import nacholab.showmethemoney.utils.StringUtils;

public class SessionManager extends TrayPreferences {
    private static final String MODULE_NAME = "Session";
    private static final int MODULE_VERSION = 1;

    private static final String TOKEN = "token";
    private static final String TOKEN_DATE = "token_date";


    public SessionManager(@NonNull Context context) {
        super(context, MODULE_NAME, MODULE_VERSION);
    }

    public void setToken(String token){
        put(TOKEN_DATE, System.currentTimeMillis());
        put(TOKEN, token);
    }

    public String getToken(){
        return getString(TOKEN, null);
    }

    public void clearToken(){
        put(TOKEN_DATE, -1);
        put(TOKEN, null);
    }

    public boolean isAuthenticated(){
        return !StringUtils.isNullEmptyOrBlank(getToken());
    }

}
