package nacholab.showmethemoney.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.AlphabeticIndex;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Configuration;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.utils.IntentHelper;
import nacholab.showmethemoney.utils.StringUtils;

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
    private static final String ACCOUNT = "account";
    private static final String RECORD = "record";
    private static final String TAG = "tag";
    private static final String TAG_RECORD = "tag_record";
    private static final String TIME = "time";
    private static final String ID = "id";
    private static final String WHERE_SYNCED_AND_DELETED = SYNCED+"=? and "+DELETED+"=?";
    private static final String WHERE_DELETED = DELETED+"=?";
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private static final String WHERE_CURRENCY = CURRENCY+" = ?";
    private static final String WHERE_UUID = UUID+" = ?";
    private static final String WHERE_CODE = CODE+" = ?";
    private static final String WHERE_ACCOUNT = ACCOUNT+" = ?";

    public ActiveAndroidDal(Context context) {
        Configuration.Builder configurationBuilder = new Configuration.Builder(context);
        ActiveAndroid.initialize(configurationBuilder.create(), true);
        ensureTagTables();
    }

    private void ensureTagTables(){
        ActiveAndroid.execSQL("create table if not exists "+TAG+" ("+ID+" integer primary key not null, "+TAG+" varchar(64) not null unique)");
        ActiveAndroid.execSQL("create table if not exists "+TAG_RECORD+" ("+RECORD+" char(36) not null, "+TAG+" int not null)");
    }

    private static void truncate(Class<? extends Model> type){
        truncate(Cache.getTableName(type));
    }

    private static void truncate(String table){
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;",table));
        ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';",table));
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
                truncate(TAG);

                if (accountsCount>0)for (MoneyAccount a : accounts) {
                    a.save();
                }

                if (recordsCount>0)for (MoneyRecord r : records) {
                    r.save();
                    if (r.getTags()!=null && r.getTags().length>0) {
                        saveTags(r.getUuid(), r.getTags());
                    }
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    // I don't care about the localization since I'm using String.format for building SQL queries
    @Override
    @SuppressLint("DefaultLocale")
    public void saveTags(String recordUUID, String[] tags){
        ActiveAndroid.execSQL("delete from " + TAG_RECORD + " where " + RECORD + "= '" + recordUUID + "'");

        if (tags!=null && tags.length>0) {
            String[] tagValues = new String[tags.length];
            String[] tagsEscaped = new String[tags.length];

            for (int t = 0; t < tags.length; t++) {
                tagsEscaped[t] = "'" + tags[t] + "'";
                tagValues[t] = "(" + tagsEscaped[t] + ")";
            }

            ActiveAndroid.execSQL("insert or replace into " + TAG + " (" + TAG + ") values " + TextUtils.join(",", tagValues));
            Cursor savedTags = ActiveAndroid.getDatabase().rawQuery("select " + ID + " from " + TAG + " where " + TAG + " in (" + TextUtils.join(",", tagsEscaped) + ")", null);
            int[] tagIds = new int[savedTags.getCount()];
            while (savedTags.moveToNext()) {
                tagIds[savedTags.getPosition()] = savedTags.getInt(0);
            }
            savedTags.close();

            String[] rowsToInsert = new String[tagIds.length];

            for (int t = 0; t < tagIds.length; t++) {
                rowsToInsert[t] = String.format("('%s',%d)", recordUUID, tagIds[t]);
            }

            ActiveAndroid.execSQL("insert into " + TAG_RECORD + " (" + RECORD + "," + TAG + ") values " + TextUtils.join(",", rowsToInsert));
        }
    }

    @Override
    public String[] findSuggestedTags(int amount, long time, double lat, double lng) {
        return findTagsByUsage();
    }

    @Override
    public String[] findTagsByUsage() {
        String query =
            "select t."+TAG+",count(1) usecount "+
            "from "+TAG+" t "+
            "left join "+TAG_RECORD+" x on x."+TAG+" = t."+ID+
            " group by t."+ID+
            " order by usecount desc, t."+TAG+" asc";

        Cursor tagsCursor = ActiveAndroid.getDatabase().rawQuery(query,null);
        String[] tags = new String[tagsCursor.getCount()];
        while (tagsCursor.moveToNext()){
            tags[tagsCursor.getPosition()] = tagsCursor.getString(0);
        }
        tagsCursor.close();

        return tags;
    }

    @Override
    public List<MoneyAccount> getAccounts(boolean populateCurrencies) {
        if (populateCurrencies){
            String query =
                "select a.*, c."+CODE+", c."+FACTOR+", c."+NAME+" as "+CURRENCY+"_"+NAME+", c."+SYMBOL+" " +
                "from "+Cache.getTableName(MoneyAccount.class)+" a "+
                "left join "+Cache.getTableName(Currency.class)+" c on a."+CURRENCY+"= c."+CODE+" " +
                "where a."+DELETED+" = "+FALSE+" order by a."+NAME;

            Cursor accountXcurrency = ActiveAndroid.getDatabase().rawQuery(query, null);

            List<MoneyAccount> accounts = new ArrayList<>();
            while (accountXcurrency.moveToNext()){
                MoneyAccount account = new MoneyAccount();
                Currency currency = new Currency();

                account.loadFromCursor(accountXcurrency);
                currency.loadFromCursor(accountXcurrency);
                currency.setName(accountXcurrency.getString(accountXcurrency.getColumnIndex(CURRENCY+"_"+NAME)));
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
    public List<MoneyAccount> getAccountsByUse() {
        String query =
                "select a.*, (select count(1) from "+Cache.getTableName(MoneyRecord.class)+" r where r."+ACCOUNT+"=a."+UUID+") as records " +
                "from "+Cache.getTableName(MoneyAccount.class)+" a order by records desc";

        Cursor accountsCursor = ActiveAndroid.getDatabase().rawQuery(query, null);

        List<MoneyAccount> accounts = new ArrayList<>();
        while (accountsCursor.moveToNext()){
            MoneyAccount account = new MoneyAccount();
            account.loadFromCursor(accountsCursor);
            accounts.add(account);
        }

        return accounts;
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
    public List<Currency> getCurrencyByUse() {
        String query =
                "select c.*, count(r."+UUID+") as records from "+Cache.getTableName(Currency.class)+" c " +
                "left join "+Cache.getTableName(MoneyRecord.class)+" r on r."+CURRENCY+" = c."+CODE+" " +
                "where r."+UUID+" is not null group by c."+UUID+" union " +
                "select *,0 from "+Cache.getTableName(Currency.class)+" order by records desc, "+NAME;

        Cursor currencyCursor = ActiveAndroid.getDatabase().rawQuery(query, null);

        List<Currency> currencies = new ArrayList<>();
        while (currencyCursor.moveToNext()){
            Currency currency = new Currency();
            currency.loadFromCursor(currencyCursor);
            currencies.add(currency);
        }

        return currencies;
    }

    @Override
    public List<Currency> findCurrencies(String filter) {
        From query = new Select().from(Currency.class);

        if (StringUtils.isNotBlank(filter)){
            query.where(CODE+" like '%"+filter+"%' or "+NAME+" like '%"+filter+"%'");
        }

        query.orderBy(NAME);

        return query.execute();
    }

    @Override
    public Currency getCurrencyByUUID(String uuid) {
        return new Select().from(Currency.class).where(WHERE_DELETED, FALSE).where(WHERE_UUID, uuid).executeSingle();
    }

    @Override
    public List<MoneyRecord> getRecords(boolean populateCurrencies, boolean populateAccounts, boolean populateTags) {

        if (populateCurrencies || populateAccounts) {
            String query = "select r.*";
            String currenciesProjection = ", c." + CODE + ", c." + FACTOR + ", c." + NAME + " as " + CURRENCY + "_" + NAME + ", c." + SYMBOL;
            String accountsProjection = ", a." + NAME + " as " + ACCOUNT + "_" + NAME + ", a." + CURRENCY + " as " + ACCOUNT + "_" + CURRENCY + ", a." + BALANCE;
            String tagsProjection = ", group_concat(t." + TAG + ") as "+TAG+"_"+TAG;
            String currenciesSelection = " left join " + Cache.getTableName(Currency.class) + " c on r." + CURRENCY + "=c." + CODE;
            String accountsSelection = " left join " + Cache.getTableName(MoneyAccount.class) + " a on r." + ACCOUNT + "=a." + UUID;
            String tagsSelection = " left join " + TAG_RECORD + " x on x." + RECORD + " = r." + UUID + " left join " + TAG + " t on t." + ID + " = x." + TAG;
            String tagsGroupBy = " group by r."+UUID;

            if (populateCurrencies) {
                query += currenciesProjection;
            }

            if (populateAccounts) {
                query += accountsProjection;
            }

            if (populateTags){
                query += tagsProjection;
            }

            query+=" from "+Cache.getTableName(MoneyRecord.class)+" r";

            if (populateCurrencies){
                query+=currenciesSelection;
            }

            if (populateAccounts){
                query+=accountsSelection;
            }

            if (populateTags){
                query+=tagsSelection;
            }

            query+=" where r."+DELETED+" = "+FALSE;

            if (populateTags){
                query+=tagsGroupBy;
            }

            query+=" order by "+TIME+" desc";

            Cursor recordsPopulated = ActiveAndroid.getDatabase().rawQuery(query, null);

            List<MoneyRecord> records = new ArrayList<>();
            while (recordsPopulated.moveToNext()){
                MoneyRecord record = new MoneyRecord();

                record.loadFromCursor(recordsPopulated);

                if (populateCurrencies){
                    Currency currency = new Currency();
                    currency.loadFromCursor(recordsPopulated);
                    currency.setName(recordsPopulated.getString(recordsPopulated.getColumnIndex(CURRENCY+"_"+NAME)));
                    record.setCurrencyObject(currency);
                }

                if (populateAccounts) {
                    MoneyAccount account = new MoneyAccount();
                    account.loadFromCursor(recordsPopulated);
                    account.setName(recordsPopulated.getString(recordsPopulated.getColumnIndex(ACCOUNT+"_"+NAME)));
                    record.setAccountObject(account);
                }

                if (populateTags){
                    String rawTags = recordsPopulated.getString(recordsPopulated.getColumnIndex(TAG+"_"+TAG));
                    if (rawTags!=null) {
                        String[] tags = rawTags.split(",");
                        record.setTags(tags);
                    }
                }

                records.add(record);
            }

            recordsPopulated.close();

            return records;
        }else {
            return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).execute();
        }
    }

    @Override
    public List<MoneyRecord> getRecordsByAccount(MoneyAccount a) {
        if (a!=null) {
            String accountUUID = a.getUuid();
            return new Select().from(MoneyRecord.class)
                    .where(WHERE_ACCOUNT, accountUUID)
                    .and(WHERE_DELETED, FALSE)
                    .execute();
        }else{
            return null;
        }
    }

    @Override
    public List<MoneyRecord> getRecordsByCurrency(Currency c) {
        return new Select().from(MoneyRecord.class).where(WHERE_DELETED, FALSE).where(WHERE_CURRENCY, c.getCode()).execute();
    }

    @Override
    public MoneyRecord getRecordByUUID(String uuid) {
        String query =
            "select r.*, group_concat(t." + TAG + ") as "+TAG+"_"+TAG+
            " from "+Cache.getTableName(MoneyRecord.class)+" r"+
            " left join " + TAG_RECORD + " x on x." + RECORD + " = r." + UUID +
            " left join " + TAG + " t on t." + ID + " = x." + TAG +
            " where "+WHERE_DELETED+" and "+WHERE_UUID +
            " group by r."+UUID;

        Cursor recordCursor = ActiveAndroid.getDatabase().rawQuery(query, new String[]{FALSE, uuid});

        MoneyRecord record = new MoneyRecord();
        recordCursor.moveToFirst();
        record.loadFromCursor(recordCursor);
        String rawTags = recordCursor.getString(recordCursor.getColumnIndex(TAG+"_"+TAG));
        if (StringUtils.isNotBlank(rawTags)) {
            String[] tags = recordCursor.getString(recordCursor.getColumnIndex(TAG + "_" + TAG)).split(",");
            record.setTags(tags);
        }
        recordCursor.close();

        return record;
    }

    @Override
    public MoneyRecord addRecord(int absAmount, MoneyRecord.Type type, String description, String account, String currency, long time, double lat, double lng, boolean updateAccountBalance) {
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
        record.setTime(time/1000);
        record.setLoclat(lat);
        record.setLoclng(lng);
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
    public MoneyRecord updateRecord(String uuid, int amount, MoneyRecord.Type type, String description, String account, String currency, long time, double lat, double lng, boolean updateAccountBalance) {
        MoneyRecord record = getRecordByUUID(uuid);
        MoneyAccount newAccount = getAccountByUUID(account);

        if (record!=null && newAccount!=null) {
            int realAmount;
            if (type == MoneyRecord.Type.income){
                realAmount = amount;
            }else{
                realAmount = -amount;
            }

            if (updateAccountBalance) {
                if (realAmount != record.getAmount()) {
                    if (newAccount.getUuid().equals(record.getAccount())) {
                        int difference = realAmount - record.getAmount();
                        newAccount.setBalance(newAccount.getBalance() + difference);
                        newAccount.setSynced(false);
                        newAccount.save();
                    } else {
                        MoneyAccount oldAccount = getAccountByUUID(record.getAccount());
                        if (oldAccount!=null) {
                            oldAccount.setBalance(oldAccount.getBalance() - record.getAmount());
                            oldAccount.setSynced(false);
                            oldAccount.save();
                            newAccount.setBalance(newAccount.getBalance() + realAmount);
                            newAccount.setSynced(false);
                            newAccount.save();
                        }else{
                            return null;
                        }
                    }
                }
            }

            record.setAmount(realAmount);
            record.setType(type);
            record.setDescription(description);
            record.setSynced(false);
            record.setCurrency(currency);
            record.setLoclat(lat);
            record.setLoclng(lng);
            record.save();

            return record;
        }else {
            return null;
        }
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
    public MoneyAccount updateAccount(String uuid, String name) {
        MoneyAccount account = getAccountByUUID(uuid);
        if (account!=null) {
            account.setName(name);
            account.setSynced(false);
            account.save();
        }

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
