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
        return SugarRecord.listAll(MoneyAccount.class);
    }

    @Override
    public List<Currency> getCurrencies() {
        return SugarRecord.listAll(Currency.class);
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
        List<MoneyRecord> records = SugarRecord.find(MoneyRecord.class, "synced = ?", "0");
        List<MoneyAccount> accounts = SugarRecord.find(MoneyAccount.class, "synced = ?", "0");
        List<Currency> currencies = SugarRecord.find(Currency.class, "synced = ?", "0");
        data.setRecords(records);
        data.setCurrencies(currencies);
        data.setAccounts(accounts);
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
