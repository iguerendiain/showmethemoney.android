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

        for (MoneyAccount a : accounts){
            Currency accountCurrency = a.getCurrency();
            Currency dbCurrency = SugarRecord.find(Currency.class, "slug=?",accountCurrency.getSlug()).get(0);
            a.setCurrency(dbCurrency);
        }

        SugarRecord.saveInTx(accounts);

        for (MoneyRecord r : records){
            Currency recordCurrency = r.getCurrency();
            MoneyAccount recordAccount = r.getAccount();
            Currency dbCurrency = SugarRecord.find(Currency.class, "slug=?",recordCurrency.getSlug()).get(0);
            MoneyAccount dbAccount = SugarRecord.find(MoneyAccount.class, "slug=?",recordAccount.getSlug()).get(0);
            r.setCurrency(dbCurrency);
            r.setAccount(dbAccount);
        }

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
    public MoneyRecord addRecord(int amount, MoneyRecord.Type type, String description, MoneyAccount account, Currency currency) {
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
        data.setRecords(records);
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
}
