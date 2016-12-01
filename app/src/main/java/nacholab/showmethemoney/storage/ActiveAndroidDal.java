package nacholab.showmethemoney.storage;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Configuration;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;

public class ActiveAndroidDal extends BaseDal{

    private static final String SYNCED = "synced";
    private static final String DELETED = "deleted";
    private static final String WHERE_SYNCED_AND_DELETED = SYNCED+"=? and "+DELETED+"=?";
    private static final String WHERE_DELETED = DELETED+"=?";
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    public ActiveAndroidDal(Context context) {
        Configuration.Builder configurationBuilder = new Configuration.Builder(context);
        ActiveAndroid.initialize(configurationBuilder.create(), true);
    }

    private static void truncate(Class<? extends Model> type){
        TableInfo tableInfo = Cache.getTableInfo(type);
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;",tableInfo.getTableName()));
        ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';",tableInfo.getTableName()));
    }

    @Override
    public void saveOrUpdateMainSyncData(MainSyncData data) {
        List<Currency> currencies= data.getCurrencies();
        List<MoneyAccount> accounts = data.getAccounts();
        List<MoneyRecord> records = data.getRecords();

        int currenciesCount = 0;
        int accountsCount = 0;
        int recordsCount = 0;

        if (currencies!=null)currenciesCount = currencies.size();
        if (accounts!=null)accountsCount = accounts.size();
        if (records!=null)recordsCount = records.size();

        if (currenciesCount>0 || accountsCount>0 || recordsCount>0) {
            ActiveAndroid.beginTransaction();
            try {
                truncate(MoneyRecord.class);
                truncate(Currency.class);
                truncate(MoneyAccount.class);

                if (currenciesCount>0)for (Currency c : currencies) {
                    c.save();
                }

                if (accountsCount>0)for (MoneyAccount a : accounts) {
                    a.save();
                }

                if (recordsCount>0)for (MoneyRecord r : records) {
                    r.save();
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    @Override
    public List<MoneyAccount> getAccounts() {
        return new Select().from(MoneyAccount.class).where(WHERE_DELETED, FALSE).execute();
    }

    @Override
    public List<Currency> getCurrencies() {
        return new Select().from(Currency.class).where(WHERE_DELETED, FALSE).execute();
    }

    @Override
    public List<MoneyRecord> getRecords() {
        return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).execute();
    }

    @Override
    public MoneyRecord addRecord(int amount, MoneyRecord.Type type, String description, String account, String currency, long time) {
        MoneyRecord record = new MoneyRecord();
        record.setUuid(BaseDal.buildId());
        record.setType(type);
        record.setAmount(amount);
        record.setDescription(description);
        record.setAccount(account);
        record.setCurrency(currency);
        record.setSynced(false);
        record.setTime(time);
        record.save();
        return record;
    }

    @Override
    public MainSyncData buildUploadMainSyncData(long lastSync) {
        MainSyncData data = new MainSyncData();
        List<MoneyRecord> records = new Select().from(MoneyRecord.class).where(WHERE_SYNCED_AND_DELETED, FALSE, FALSE).execute();
        List<MoneyAccount> accounts = new Select().from(MoneyAccount.class).where(WHERE_SYNCED_AND_DELETED, FALSE, FALSE).execute();
        List<Currency> currencies = new Select().from(Currency.class).where(WHERE_SYNCED_AND_DELETED, FALSE, FALSE).execute();
        List<MoneyRecord> recordsToDelete = new Select().from(MoneyRecord.class).where(WHERE_SYNCED_AND_DELETED, FALSE, TRUE).execute();
        List<MoneyAccount> accountsToDelete = new Select().from(MoneyAccount.class).where(WHERE_SYNCED_AND_DELETED, FALSE, TRUE).execute();
        List<Currency> currenciesToDelete = new Select().from(Currency.class).where(WHERE_SYNCED_AND_DELETED, FALSE, TRUE).execute();
        data.setRecords(records);
        data.setCurrencies(currencies);
        data.setAccounts(accounts);
        data.setRecordsToDelete(recordsToDelete);
        data.setCurrenciesToDelete(currenciesToDelete);
        data.setAccountsToDelete(accountsToDelete);
        return data;
    }

    @Override
    public void setSynced(MainSyncData data, boolean synced) {
        int recordsCount = 0;
        int accountsCount = 0;
        int currenciesCount = 0;

        if (data.getRecords()!=null)recordsCount = data.getRecords().size();
        if (data.getAccounts()!=null)accountsCount = data.getAccounts().size();
        if (data.getCurrencies()!=null)currenciesCount = data.getCurrencies().size();

        if (recordsCount>0||accountsCount>0||currenciesCount>0) {
            ActiveAndroid.beginTransaction();
            try {
                if (recordsCount > 0) {
                    for (MoneyRecord r : data.getRecords()) {
                        r.setSynced(synced);
                        r.save();
                    }
                }

                if (accountsCount > 0) {
                    for (MoneyAccount a : data.getAccounts()) {
                        a.setSynced(synced);
                        a.save();
                    }
                }

                if (currenciesCount > 0) {
                    for (Currency c : data.getCurrencies()) {
                        c.setSynced(synced);
                        c.save();
                    }
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    @Override
    public Currency addCurrency(String name, float factor) {
        Currency currency = new Currency();
        currency.setUuid(BaseDal.buildId());
        currency.setName(name);
        currency.setFactor(factor);
        currency.setSynced(false);
        currency.save();
        return currency;
    }

    @Override
    public MoneyAccount addAccount(String name, String currency, int balance) {
        MoneyAccount account = new MoneyAccount();
        account.setUuid(BaseDal.buildId());
        account.setName(name);
        account.setCurrency(currency);
        account.setBalance(balance);
        account.setSynced(false);
        account.save();
        return account;
    }

    @Override
    public void cleanDeleted() {
        new Delete().from(MoneyRecord.class).where(WHERE_DELETED,TRUE).execute();
        new Delete().from(MoneyAccount.class).where(WHERE_DELETED,TRUE).execute();
        new Delete().from(Currency.class).where(WHERE_DELETED,TRUE).execute();
    }

    @Override
    public void markAsDeleted(Currency c) {
        c.setSynced(false);
        c.setDeleted(true);
        c.save();
    }

    @Override
    public void markAsDeleted(MoneyAccount a) {
        a.setSynced(false);
        a.setDeleted(true);
        a.save();
    }

    @Override
    public void markAsDeleted(MoneyRecord d) {
        d.setSynced(false);
        d.setDeleted(true);
        d.save();
    }
}
