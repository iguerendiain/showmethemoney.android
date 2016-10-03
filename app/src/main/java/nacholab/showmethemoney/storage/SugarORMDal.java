package nacholab.showmethemoney.storage;

import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.List;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;

public class SugarORMDal extends BaseDal {

    private static final String SYNCED = "synced";
    private static final String DELETED = "deleted";
    private static final String WHERE_SYNCED_AND_DELETED = SYNCED+"=? and "+DELETED+"=?";
    private static final String WHERE_DELETED = DELETED+"=?";
    private static final String TRUE = "1";
    private static final String FALSE = "0";


    public SugarORMDal(MainApplication app) {
        SugarContext.init(app);
    }

    @Override
    public void saveOrUpdateMainSyncData(MainSyncData data) {
        SugarRecord.deleteAll(MoneyRecord.class);
        SugarRecord.deleteAll(Currency.class);
        SugarRecord.deleteAll(MoneyAccount.class);

        List<Currency> currencies= data.getCurrencies();
        List<MoneyAccount> accounts = data.getAccounts();
        List<MoneyRecord> records = data.getRecords();

        SugarRecord.saveInTx(currencies);
        SugarRecord.saveInTx(accounts);
        SugarRecord.saveInTx(records);
    }

    @Override
    public List<MoneyAccount> getAccounts() {
        return SugarRecord.find(MoneyAccount.class, WHERE_DELETED, FALSE);
    }

    @Override
    public List<Currency> getCurrencies() {
        return SugarRecord.find(Currency.class, WHERE_DELETED, FALSE);
    }

    @Override
    public List<MoneyRecord> getRecords() {
        return SugarRecord.find(MoneyRecord.class, WHERE_DELETED, FALSE);
    }

    @Override
    public void markAsDeleted(Currency c) {
        c.setDeleted(true);
        c.save();
    }

    @Override
    public void markAsDeleted(MoneyAccount a) {
        a.setDeleted(true);
        a.save();
    }

    @Override
    public void markAsDeleted(MoneyRecord d) {
        d.setDeleted(true);
        d.save();
    }

    @Override
    public MoneyRecord addRecord(int amount, MoneyRecord.Type type, String description, String account, String currency) {
        MoneyRecord record = new MoneyRecord();
        record.setType(type);
        record.setAmount(amount);
        record.setDescription(description);
        record.setAccount(account);
        record.setCurrency(currency);
        record.setSynced(false);
        record.save();
        return record;
    }

    @Override
    public MainSyncData buildUploadMainSyncData(long lastSync) {
        MainSyncData data = new MainSyncData();
        List<MoneyRecord> records = SugarRecord.find(MoneyRecord.class, WHERE_SYNCED_AND_DELETED, FALSE, FALSE);
        List<MoneyAccount> accounts = SugarRecord.find(MoneyAccount.class, WHERE_SYNCED_AND_DELETED, FALSE, FALSE);
        List<Currency> currencies = SugarRecord.find(Currency.class, WHERE_SYNCED_AND_DELETED, FALSE, FALSE);
        List<MoneyRecord> recordsToDelete = SugarRecord.find(MoneyRecord.class, WHERE_SYNCED_AND_DELETED, FALSE, TRUE);
        List<MoneyAccount> accountsToDelete = SugarRecord.find(MoneyAccount.class, WHERE_SYNCED_AND_DELETED, FALSE, TRUE);
        List<Currency> currenciesToDelete = SugarRecord.find(Currency.class, WHERE_SYNCED_AND_DELETED, FALSE, TRUE);
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
        if (data.getRecords()!=null){
            for (MoneyRecord r : data.getRecords()) {
                r.setSynced(synced);
            }
            SugarRecord.saveInTx(data.getRecords());
        }
    }

    @Override
    public void cleanDeleted(){
        SugarRecord.deleteAll(MoneyRecord.class, WHERE_SYNCED_AND_DELETED, TRUE, TRUE);
        SugarRecord.deleteAll(MoneyAccount.class, WHERE_SYNCED_AND_DELETED, TRUE, TRUE);
        SugarRecord.deleteAll(Currency.class, WHERE_SYNCED_AND_DELETED, TRUE, TRUE);
    }

    @Override
    public Currency addCurrency(String name, String slug, float factor) {
        Currency currency = new Currency();
        currency.setName(name);
        currency.setSlug(slug);
        currency.setFactor(factor);
        currency.setSynced(false);
        currency.save();
        return currency;
    }

    @Override
    public MoneyAccount addAccount(String name, String slug, String currency, int balance) {
        MoneyAccount account = new MoneyAccount();
        account.setName(name);
        account.setSlug(slug);
        account.setCurrency(currency);
        account.setBalance(balance);
        account.setSynced(false);
        account.save();
        return account;
    }
}
