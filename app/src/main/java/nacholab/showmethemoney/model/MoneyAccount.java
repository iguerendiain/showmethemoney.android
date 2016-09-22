package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoneyAccount extends DBModel {
    @Expose
    @SerializedName("id")
    private int DBid;

    @Expose
    private String name;

    @Expose
    private String slug;

    @Expose
    private String currency;

    @Expose
    private float balance;

    public MoneyAccount() {
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
