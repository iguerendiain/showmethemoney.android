package nacholab.showmethemoney.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class SyncUtils {
    private static final String TAG = "SyncAdapter";

    public static final String AUTHORITY = "nacholab.showmethemoney.provider";
    private static final String ACCOUNT_TYPE = "nacholab.net";
    private static final String ACCOUNT = "dummyaccount";

    private static final String SCHEME = "content://";
    private static final String MAIN_SYNC_PATH = "/mainSync";


    public static Account createSyncAccount(Context ctx){
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) ctx.getSystemService(Context.ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d(TAG,"Account created");
        } else {
            Log.d(TAG,"Error while trying to addAccountExplicitly");
        }

        return newAccount;
    }

    public static Uri buildMainSyncUri(){
        return new Uri.Builder().scheme(SCHEME).authority(AUTHORITY).path(MAIN_SYNC_PATH).build();
    }
}
