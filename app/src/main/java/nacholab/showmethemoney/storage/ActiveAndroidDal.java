package nacholab.showmethemoney.storage;

import android.content.Context;
import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Configuration;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;

public class ActiveAndroidDal extends BaseDal{

    private static final String LAST_SYNC = "lastSync";
    private static final String UUID = "uuid";
    private static final String BALANCE = "balance";
    private static final String CURRENCY = "currency";
    private static final String NAME = "name";
    private static final String CODE = "code";
    private static final String FACTOR = "factor";
    private static final String SYMBOL = "symbol";
    private static final String SYNCED = "synced";
    private static final String DELETED = "deleted";
    private static final String WHERE_SYNCED_AND_DELETED = SYNCED+"=? and "+DELETED+"=?";
    private static final String WHERE_DELETED = DELETED+"=?";
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private static final String WHERE_CURRENCY = CURRENCY+" = ?";
    private static final String WHERE_UUID = UUID+" = ?";
    private static final String WHERE_CODE = CODE+" = ?";

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
        List<MoneyAccount> accounts = data.getAccounts();
        List<MoneyRecord> records = data.getRecords();

        int accountsCount = 0;
        int recordsCount = 0;

        if (accounts!=null)accountsCount = accounts.size();
        if (records!=null)recordsCount = records.size();

        if (accountsCount>0 || recordsCount>0) {
            ActiveAndroid.beginTransaction();
            try {
                truncate(MoneyRecord.class);
                truncate(MoneyAccount.class);

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
    public List<MoneyAccount> getAccounts(boolean populateCurrencies) {
        if (populateCurrencies){
            String query =
                "select a.*, c."+CODE+", c."+FACTOR+", c."+NAME+" as "+CURRENCY+"_"+NAME+", c."+SYMBOL+" " +
                "from "+Cache.getTableInfo(MoneyAccount.class).getTableName()+" a "+
                "left join "+Cache.getTableInfo(Currency.class).getTableName()+" c on a."+CURRENCY+"= c."+CODE+" " +
                "where a."+DELETED+" = 0 order by a."+NAME;

            Cursor accountXcurrency = ActiveAndroid.getDatabase().rawQuery(query, null);

            List<MoneyAccount> accounts = new ArrayList<>();
            while (accountXcurrency.moveToNext()){
                MoneyAccount account = new MoneyAccount();
                Currency currency = new Currency();

                account.setDeleted(accountXcurrency.getInt(accountXcurrency.getColumnIndex(DELETED))!=0);
                account.setLastSync(accountXcurrency.getLong(accountXcurrency.getColumnIndex(LAST_SYNC)));
                account.setSynced(accountXcurrency.getInt(accountXcurrency.getColumnIndex(SYNCED))!=0);
                account.setUuid(accountXcurrency.getString(accountXcurrency.getColumnIndex(UUID)));
                account.setBalance(accountXcurrency.getInt(accountXcurrency.getColumnIndex(BALANCE)));
                account.setCurrency(accountXcurrency.getString(accountXcurrency.getColumnIndex(CURRENCY)));
                account.setName(accountXcurrency.getString(accountXcurrency.getColumnIndex(NAME)));

                currency.setName(accountXcurrency.getString(accountXcurrency.getColumnIndex(CURRENCY+"_"+NAME)));
                currency.setUuid(accountXcurrency.getString(accountXcurrency.getColumnIndex(CURRENCY)));
                currency.setCode(accountXcurrency.getString(accountXcurrency.getColumnIndex(CODE)));
                currency.setFactor(accountXcurrency.getFloat(accountXcurrency.getColumnIndex(FACTOR)));
                currency.setSymbol(accountXcurrency.getString(accountXcurrency.getColumnIndex(SYMBOL)));

                account.setCurrencyObject(currency);

                accounts.add(account);
            }

            accountXcurrency.close();

            return accounts;
        }else {
            return new Select().from(MoneyAccount.class).where(WHERE_DELETED, FALSE).execute();
        }
    }

    @Override
    public MoneyAccount getAccountByUUID(String uuid) {
        return new Select().from(MoneyAccount.class).where(WHERE_DELETED, FALSE).where(WHERE_UUID, uuid).executeSingle();
    }

    @Override
    public List<MoneyAccount> getAccountsByCurrency(Currency c) {
        return new Select().from(MoneyAccount.class).where(WHERE_DELETED, FALSE).where(WHERE_CURRENCY, c.getCode()).execute();
    }

    @Override
    public List<Currency> getCurrencies() {
        return new Select().from(Currency.class).where(WHERE_DELETED, FALSE).execute();
    }

    @Override
    public Currency getCurrencyByUUID(String uuid) {
        return new Select().from(Currency.class).where(WHERE_DELETED, FALSE).where(WHERE_UUID, uuid).executeSingle();
    }

    @Override
    public List<MoneyRecord> getRecords() {
        return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).execute();
    }

    @Override
    public List<MoneyRecord> getRecordsByCurrency(Currency c) {
        return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).where(WHERE_CURRENCY, c.getCode()).execute();
    }

    @Override
    public MoneyRecord getRecordByUUID(String uuid) {
        return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).where(WHERE_UUID, uuid).executeSingle();
    }

    @Override
    public MoneyRecord addRecord(int absAmount, MoneyRecord.Type type, String description, String account, String currency, long time, boolean updateAccountBalance) {
        MoneyRecord record = new MoneyRecord();
        record.setUuid(BaseDal.buildId());
        record.setType(type);
        if (type == MoneyRecord.Type.income) {
            record.setAmount(absAmount);
        }else{
            record.setAmount(-absAmount);
        }
        record.setDescription(description);
        record.setAccount(account);
        record.setCurrency(currency);
        record.setSynced(false);
        record.setTime(time);
        record.save();

        if (updateAccountBalance){
            MoneyAccount dbAccount = getAccountByUUID(record.getAccount());
            dbAccount.setBalance(dbAccount.getBalance() + record.getAmount());
            dbAccount.setSynced(false);
            dbAccount.save();
        }

        return record;
    }

    @Override
    public MainSyncData buildUploadMainSyncData(long lastSync) {
        MainSyncData data = new MainSyncData();
        List<MoneyRecord> records = new Select().from(MoneyRecord.class).where(WHERE_SYNCED_AND_DELETED, FALSE, FALSE).execute();
        List<MoneyAccount> accounts = new Select().from(MoneyAccount.class).where(WHERE_SYNCED_AND_DELETED, FALSE, FALSE).execute();
        List<MoneyRecord> recordsToDelete = new Select().from(MoneyRecord.class).where(WHERE_SYNCED_AND_DELETED, FALSE, TRUE).execute();
        List<MoneyAccount> accountsToDelete = new Select().from(MoneyAccount.class).where(WHERE_SYNCED_AND_DELETED, FALSE, TRUE).execute();
        data.setRecords(records);
        data.setAccounts(accounts);
        data.setRecordsToDelete(recordsToDelete);
        data.setAccountsToDelete(accountsToDelete);
        return data;
    }

    @Override
    public void setSynced(MainSyncData data, boolean synced) {
        int recordsCount = 0;
        int accountsCount = 0;

        if (data.getRecords()!=null)recordsCount = data.getRecords().size();
        if (data.getAccounts()!=null)accountsCount = data.getAccounts().size();

        if (recordsCount>0||accountsCount>0) {
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

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
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
    }

    @Override
    public void markAsDeleted(MoneyAccount a) {
        a.setSynced(false);
        a.setDeleted(true);
        a.save();
    }

    @Override
    public void markAsDeleted(MoneyRecord d, boolean updateAccountBalance) {
        d.setSynced(false);
        d.setDeleted(true);
        d.save();

        if (updateAccountBalance){
            MoneyAccount dbAccount = getAccountByUUID(d.getAccount());
            dbAccount.setBalance(dbAccount.getBalance() - d.getAmount());
            dbAccount.setSynced(false);
            dbAccount.save();
        }
    }

    @Override
    public void saveOrUpdateCurrency(List<Currency> currencies) {
        for (Currency currency : currencies){
            Currency dbCurrency = getCurrencyByCode(currency.getCode());
            if (dbCurrency==null){
                currency.save();
            }else{
                dbCurrency.update(currency);
                dbCurrency.save();
            }
        }
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        return new Select().from(Currency.class).where(WHERE_DELETED, FALSE).where(WHERE_CODE, code).executeSingle();
    }
}
