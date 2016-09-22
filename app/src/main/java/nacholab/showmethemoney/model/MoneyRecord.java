package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyRecord extends DBModel {

    public enum Type{PATCH, EXPENSE, INCOME}

    @Expose
    @SerializedName("id")
    private int DBid;

    @Expose
    private String description;

    @Expose
    private String account;

    @Expose
    private String currency;

    @Expose
    private Type type;

    @Expose
    private int amount;

    public MoneyRecord() {
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
