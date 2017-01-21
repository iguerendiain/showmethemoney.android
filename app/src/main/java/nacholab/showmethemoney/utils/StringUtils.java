package nacholab.showmethemoney.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

public class StringUtils {

    public static boolean isNullEmptyOrBlank(String text){
        return text==null || text.trim().isEmpty();
    }

    public static boolean isNotBlank(String text){
        return text!=null && !text.trim().isEmpty();
    }

    public static Locale getCurrentLocale(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ctx.getResources().getConfiguration().getLocales().get(0);
        }else{
            return ctx.getResources().getConfiguration().locale;
        }
    }

    public static String formatLocalized(Context ctx, String format, Object... args){
        return String.format(getCurrentLocale(ctx), format, args);
    }

    public static String floatToStringLocalized(Context ctx, float number){
        return formatLocalized(ctx, "%f", number);
    }

    public static String formatMoneyLocalized(Context ctx, String symbol, float amount){
        return formatLocalized(ctx, symbol+" %.2f", amount);
    }
}
