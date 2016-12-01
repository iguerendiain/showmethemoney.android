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

    @Expose
    @Column
    private String currency;

    @Expose
    @Column
    private Type type;

    @Expose
    @Column
    private long time;

    @Expose
    @Column
    private int amount;

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

    @Override
    public String toString() {
        String render = "["+type+"] "+description+" on "+account+" ("+amount+"/"+currency+")";
        if (!isSynced()){
            render+=" NOT SYNCED";
        }

        return render;
    }
}
