package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MainSyncData extends BaseModel {

    @Expose
    private List<MoneyAccount> accounts;

    @Expose
    private List<Currency> currencies;

    @Expose
    private List<MoneyRecord> records;

    public MainSyncData() {
    }

    public List<MoneyAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<MoneyAccount> accounts) {
        this.accounts = accounts;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<MoneyRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MoneyRecord> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return  "{\n"+
                "\taccounts=["+accounts+"]\n"+
                "\tcurrencies=["+currencies+"]\n" +
                "\trecords=["+records+"]\n" +
                "}";
    }
}
