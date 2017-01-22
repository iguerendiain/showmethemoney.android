package nacholab.showmethemoney.utils;

import android.content.Context;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public static String formatDateCompact(Context ctx, long time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time * 1000);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, getCurrentLocale(ctx));
        return day+" "+month+" - "+hour+":"+minute;
    }
}
