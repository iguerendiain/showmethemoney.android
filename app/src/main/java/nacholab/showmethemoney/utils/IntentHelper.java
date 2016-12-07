package nacholab.showmethemoney.utils;

import android.app.Activity;

public class IntentHelper {

    public static String getExtraString(Activity activity, String key){
        if (activity.getIntent()!=null && activity.getIntent().getExtras()!=null){
            return activity.getIntent().getExtras().getString(key);
        }else{
            return null;
        }
    }
}
