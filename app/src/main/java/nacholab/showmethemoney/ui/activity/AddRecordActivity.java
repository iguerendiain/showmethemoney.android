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

public class AddRecordActivity extends AuthenticatedActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecord);
        ButterKnife.bind(this);
        dbAccounts = getMainApp().getDal().getAccounts();
        accounts.setAdapter(new AccountSpinnerAdapter(this, dbAccounts));
        dbCurrencies = getMainApp().getDal().getCurrencies();
        currencies.setAdapter(new CurrencySpinnerAdapter(this, dbCurrencies));
        accounts.setOnItemSelectedListener(this);
        currencies.setOnItemSelectedListener(this);
        createRecord.setOnClickListener(this);
        setAccount(0);
    }

    private void setAccount(int idx){
        currentAccount = dbAccounts.get(0);
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
                int writtenAmount = (int) Math.floor(Integer.parseInt(amount.getText().toString())*100);
                int realAmount;

                if (currentCurrency.getCode().equals(currentAccount.getCurrency())){
                    realAmount = writtenAmount;
                }else{
                    realAmount = (int) Math.floor(writtenAmount * (accountCurrency.getFactor() / currentCurrency.getFactor()));
                }

                getMainApp().getDal().addRecord(
                        (int) Math.floor(realAmount),
                        incomeSwitch.isChecked()? MoneyRecord.Type.income: MoneyRecord.Type.expense,
                        description.getText().toString(),
                        currentAccount.getUuid(),
                        currentCurrency.getCode(),
                        (long) (System.currentTimeMillis() / 1000f),
                        true);
                finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.accounts:
                setAccount(i);
                break;
            case R.id.currencies:
                currentCurrency = dbCurrencies.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
