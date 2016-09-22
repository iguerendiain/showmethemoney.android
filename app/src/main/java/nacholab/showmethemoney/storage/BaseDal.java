package nacholab.showmethemoney.storage;

import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;

public abstract class BaseDal {
    public abstract void saveOrUpdateMainSyncData(MainSyncData data);
    public abstract List<MoneyAccount> getAccounts();
    public abstract List<Currency> getCurrencies();
    public abstract MoneyRecord addRecord(int amount, MoneyRecord.Type type, String description, String account, String currency);
    public abstract MainSyncData buildUploadMainSyncData(long lastSync);
    public abstract void setSynced(MainSyncData data, boolean synced);
    public abstract Currency addCurrency(String name, String slug, float factor);
    public abstract MoneyAccount addAccount(String name, String slug, String currency, int balance);
}
