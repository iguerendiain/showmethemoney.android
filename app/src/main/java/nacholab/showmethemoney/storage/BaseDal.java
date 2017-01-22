package nacholab.showmethemoney.storage;

import java.util.List;
import java.util.UUID;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;

public abstract class BaseDal {
    public abstract void saveOrUpdateMainSyncData(MainSyncData data);
    public abstract List<MoneyAccount> getAccounts(boolean populateCurrencies);
    public abstract MoneyAccount getAccountByUUID(String uuid);
    public abstract List<MoneyAccount> getAccountsByCurrency(Currency c);
    public abstract List<Currency> getCurrencies();
    public abstract Currency getCurrencyByUUID(String uuid);
    public abstract MoneyRecord addRecord(int amount, MoneyRecord.Type type, String description, String account, String currency, long time, boolean updateAccountBalance);
    public abstract MoneyRecord updateRecord(String uuid, int amount, MoneyRecord.Type type, String description, String account, String currency, long time, boolean updateAccountBalance);
    public abstract MainSyncData buildUploadMainSyncData(long lastSync);
    public abstract void setSynced(MainSyncData data, boolean synced);
    public abstract MoneyAccount addAccount(String name, String currency, int balance);
    public abstract MoneyAccount updateAccount(String uuid, String name);
    public abstract void cleanDeleted();
    public abstract List<MoneyRecord> getRecords(boolean populateCurrencies, boolean populateAccounts);
    public abstract List<MoneyRecord> getRecordsByCurrency(Currency c);
    public abstract MoneyRecord getRecordByUUID(String uuid);
    public abstract void markAsDeleted(MoneyAccount a);
    public abstract void markAsDeleted(MoneyRecord d, boolean updateAccountBalance);
    public abstract void saveOrUpdateCurrency(List<Currency> currencies);
    public abstract Currency getCurrencyByCode(String code);

    static String buildId(){
        return UUID.randomUUID().toString();
    }
}
