package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;

public class MoneyRecord extends DBModel {

    public enum Type{patch, expense, income}

    @Expose
    @Column
    private String description;

    @Expose
    @Column
    private String account;

    private MoneyAccount accountObject;

    @Expose
    @Column
    private String currency;

    private Currency currencyObject;

    @Expose
    @Column
    private Type type;

    @Expose
    @Column
    private long time;

    @Expose
    @Column
    private int amount;

    @Expose
    @Column
    private double loclat;

    @Expose
    @Column
    private double loclng;

    public MoneyRecord() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public Currency getCurrencyObject() {
        return currencyObject;
    }

    public void setCurrencyObject(Currency currencyObject) {
        this.currencyObject = currencyObject;
    }

    public MoneyAccount getAccountObject() {
        return accountObject;
    }

    public void setAccountObject(MoneyAccount accountObject) {
        this.accountObject = accountObject;
    }

    public double getLoclat() {
        return loclat;
    }

    public void setLoclat(double loclat) {
        this.loclat = loclat;
    }

    public double getLoclng() {
        return loclng;
    }

    public void setLoclng(double loclng) {
        this.loclng = loclng;
    }

    @Override
    public String toString() {
        String render = "["+type+"] "+description+" on "+account+" ("+amount+"/"+currency+")";
        if (!isSynced()){
            render+=" NOT SYNCED";
        }

        return render;
    }
}
