package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;

public class MoneyAccount extends DBModel {

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private RelationCurrency currency;

    @Expose
    @Column
    private float balance;

    public MoneyAccount() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelationCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(RelationCurrency currency) {
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
