package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;

public class MoneyAccount extends DBModel {

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private String currency;

    @Expose
    @Column
    private float balance;

    private Currency currencyObject;

    public MoneyAccount() {
    }

    public Currency getCurrencyObject() {
        return currencyObject;
    }

    public void setCurrencyObject(Currency currencyObject) {
        this.currencyObject = currencyObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
