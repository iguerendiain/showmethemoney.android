package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MainSyncData extends BaseModel {

    @Expose
    private List<MoneyAccount> accounts;

    @Expose
    private List<MoneyRecord> records;

    @Expose
    private List<MoneyAccount> accountsToDelete;

    @Expose
    private List<MoneyRecord> recordsToDelete;

    public MainSyncData() {
    }

    public List<MoneyAccount> getAccountsToDelete() {
        return accountsToDelete;
    }

    public void setAccountsToDelete(List<MoneyAccount> accountsToDelete) {
        this.accountsToDelete = accountsToDelete;
    }

    public List<MoneyRecord> getRecordsToDelete() {
        return recordsToDelete;
    }

    public void setRecordsToDelete(List<MoneyRecord> recordsToDelete) {
        this.recordsToDelete = recordsToDelete;
    }

    public List<MoneyAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<MoneyAccount> accounts) {
        this.accounts = accounts;
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
                "\trecords=["+records+"]\n" +
                "\taccountsToDelete=["+accountsToDelete+"]\n"+
                "\trecordsToDelete=["+recordsToDelete+"]\n" +
                "}";
    }
}
