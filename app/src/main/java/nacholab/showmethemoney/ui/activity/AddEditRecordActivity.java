package nacholab.showmethemoney.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.ui.adapter.AccountSpinnerAdapter;
import nacholab.showmethemoney.ui.adapter.CurrencySpinnerAdapter;
import nacholab.showmethemoney.utils.MoneyHelper;
import nacholab.showmethemoney.utils.StringUtils;

public class AddEditRecordActivity extends AuthenticatedActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String RECORD_UUID = "uuid";

    @BindView(R.id.accounts)
    Spinner accounts;

    @BindView(R.id.currencies)
    Spinner currencies;

    @BindView(R.id.amount)
    EditText amount;

    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.createRecord)
    View createRecord;

    @BindView(R.id.income_switch)
    Switch incomeSwitch;

    private List<MoneyAccount> dbAccounts;
    private List<Currency> dbCurrencies;

    private MoneyAccount currentAccount;
    private Currency currentCurrency;
    private Currency accountCurrency;

    private MoneyRecord editingRecord;

    // Set to false to ignore the onItemSelected for the currency and account selector
    // this are reseted after one run of onItemSelected for each flag
    private boolean ignoreCurrencySelection;
    private boolean ignoreAccountSelection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecord);
        ButterKnife.bind(this);
        dbAccounts = getMainApp().getDal().getAccounts();
        accounts.setAdapter(new AccountSpinnerAdapter(this, dbAccounts));
        dbCurrencies = getMainApp().getDal().getCurrencies();
        currencies.setAdapter(new CurrencySpinnerAdapter(this, dbCurrencies));
        createRecord.setOnClickListener(this);
        accounts.setOnItemSelectedListener(this);
        currencies.setOnItemSelectedListener(this);
        setAccount(0);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            String requestedRecordUUID = getIntent().getExtras().getString(RECORD_UUID);
            if (requestedRecordUUID!=null){
                editingRecord = getMainApp().getDal().getRecordByUUID(requestedRecordUUID);
            }
        }

        if (editingRecord!=null){
            for (int a=0; a<dbAccounts.size(); a++){
                if (dbAccounts.get(a).getUuid().equals(editingRecord.getAccount())){
                    setAccount(a);
                }
            }

            for (int c=0; c<dbCurrencies.size(); c++){
                if (dbCurrencies.get(c).getCode().equals(editingRecord.getCurrency())){
                    currentCurrency = dbCurrencies.get(c);
                    currencies.setSelection(c);
                    break;
                }
            }

            int writtenAmount;
            if (!currentCurrency.getCode().equals(currentAccount.getCurrency())){
                writtenAmount = MoneyHelper.convert(editingRecord.getAmount(), accountCurrency.getFactor(), currentCurrency.getFactor());
            }else{
                writtenAmount = editingRecord.getAmount();
            }

            amount.setText(StringUtils.floatToStringLocalized(this, Math.abs(writtenAmount) / 100f));
            incomeSwitch.setChecked(editingRecord.getType() == MoneyRecord.Type.income);
            description.setText(editingRecord.getDescription());
        }else{
            currentCurrency = dbCurrencies.get(0);
        }

        // Because after setSelection on the spinners trigger a call to onItemSelected after
        // onCreate and that will overwrite data I'm adding these two flags
        ignoreCurrencySelection = true;
        ignoreAccountSelection = true;
    }

    private void setAccount(int idx){
        currentAccount = dbAccounts.get(idx);
        accounts.setSelection(idx);
        String currencyCode = currentAccount.getCurrency();
        for (int c=0; c<dbCurrencies.size(); c++){
            if (dbCurrencies.get(c).getCode().equals(currencyCode)){
                currencies.setSelection(c);
                currentCurrency = dbCurrencies.get(c);
                accountCurrency = dbCurrencies.get(c);
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createRecord:
                int writtenAmount = (int) Math.floor(Float.parseFloat(amount.getText().toString()) * 100);
                int realAmount;

                if (currentCurrency.getCode().equals(currentAccount.getCurrency())) {
                    realAmount = writtenAmount;
                } else {
                    realAmount = MoneyHelper.convert(writtenAmount, currentCurrency.getFactor(), accountCurrency.getFactor());
                }

                MoneyRecord.Type recordType = incomeSwitch.isChecked() ? MoneyRecord.Type.income : MoneyRecord.Type.expense;

                if (editingRecord!=null) {
                    if (realAmount != editingRecord.getAmount()) {
                        if (currentAccount.getUuid().equals(editingRecord.getAccount())) {
                            int difference = realAmount - editingRecord.getAmount();
                            currentAccount.setBalance(currentAccount.getBalance() + difference);
                            currentAccount.setSynced(false);
                            currentAccount.save();
                        } else {
                            MoneyAccount oldAccount = null;
                            for (MoneyAccount a : dbAccounts){
                                if (a.getUuid().equals(editingRecord.getAccount())){
                                    oldAccount = a;
                                    break;
                                }
                            }

                            oldAccount.setBalance(oldAccount.getBalance() - editingRecord.getAmount());
                            oldAccount.setSynced(false);
                            oldAccount.save();
                            currentAccount.setBalance(currentAccount.getBalance() + realAmount);
                            currentAccount.setSynced(false);
                            currentAccount.save();
                        }
                    }

                    editingRecord.setAmount(realAmount);
                    editingRecord.setType(recordType);
                    editingRecord.setDescription(description.getText().toString());
                    editingRecord.setSynced(false);
                    editingRecord.setCurrency(currentCurrency.getCode());
                    editingRecord.save();
                }else {
                    getMainApp().getDal().addRecord(
                            (int) Math.floor(realAmount),
                            recordType,
                            description.getText().toString(),
                            currentAccount.getUuid(),
                            currentCurrency.getCode(),
                            (long) (System.currentTimeMillis() / 1000f),
                            true);
                }
                finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.accounts:
                if (!ignoreAccountSelection) {
                    setAccount(i);
                }

                ignoreAccountSelection = false;
                break;
            case R.id.currencies:
                if (!ignoreCurrencySelection) {
                    currentCurrency = dbCurrencies.get(i);
                }

                ignoreCurrencySelection = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
