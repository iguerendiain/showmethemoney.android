package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.activeandroid.annotation.Column;

public class MoneyRecord extends DBModel {

    public enum Type{PATCH, EXPENSE, INCOME}

    @Expose
    @SerializedName("id")
    @Column
    private int DBid;

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
    private int amount;

    public MoneyRecord() {
    }

    public MoneyAccount getAccountObject() {
        return accountObject;
    }

    public void setAccountObject(MoneyAccount accountObject) {
        this.accountObject = accountObject;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
