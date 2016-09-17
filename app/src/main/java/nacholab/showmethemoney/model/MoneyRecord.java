package nacholab.showmethemoney.model;

public class MoneyRecord extends DBModel {

    public enum Type{PATCH, EXPENSE, INCOME}

    public static final int PATCH = 0;
    public static final int EXPENSE = 1;
    public static final int INCOME = 2;

    private String _id;
    private String description;
    private MoneyAccount account;
    private Currency currency;

    public MoneyRecord() {
    }

    private Type type;

    private int amount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoneyAccount getAccount() {
        return account;
    }

    public void setAccount(MoneyAccount account) {
        this.account = account;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
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
        String render = "["+type+"] "+description+" on "+account.getName()+" ("+amount+"/"+currency.getSlug()+")";
        if (!isSynced()){
            render+=" NOT SYNCED";
        }

        return render;
    }
}
