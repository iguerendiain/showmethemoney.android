package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.activeandroid.annotation.Column;

public class MoneyAccount extends DBModel {
    @Expose
    @SerializedName("id")
    @Column
    private int DBid;

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private String slug;

    @Expose
    @Column
    private String currency;

    private Currency currencyObject;

    @Expose
    @Column
    private float balance;

    public MoneyAccount() {
    }

    public Currency getCurrencyObject() {
        return currencyObject;
    }

    public void setCurrencyObject(Currency currencyObject) {
        this.currencyObject = currencyObject;
    }

    public int getDBid() {
        return DBid;
    }

    public void setDBid(int DBid) {
        this.DBid = DBid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        String render = name+" ("+currency+"): $"+balance;
        if (!isSynced()){
            render+=" NOT SYNCED";
        }
        return render;

    }
}
