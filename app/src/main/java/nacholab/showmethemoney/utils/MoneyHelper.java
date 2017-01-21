package nacholab.showmethemoney.utils;

public class MoneyHelper {

    public static int convert(int amount, float factorFrom, float factorTo){
        return (int) Math.floor(amount / (factorFrom / factorTo));
    }

}
