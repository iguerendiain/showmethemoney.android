package nacholab.showmethemoney.utils;

public class StringUtils {

    public static boolean isNullEmptyOrBlank(String text){
        return text==null || text.trim().isEmpty();
    }

    public static boolean isNotBlank(String text){
        return text!=null && !text.trim().isEmpty();
    }

}
