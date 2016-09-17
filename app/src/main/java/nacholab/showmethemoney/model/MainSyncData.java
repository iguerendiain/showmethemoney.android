package nacholab.showmethemoney.model;

import java.util.List;

public class MainSyncData extends BaseModel {
    private List<MoneyAccount> accounts;
    private List<Currency> currencies;
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
