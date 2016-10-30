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
        currentAccount = dbAccounts.get(0);
        currentCurrency = dbCurrencies.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createRecord:
                getMainApp().getDal().addRecord(
                        Integer.parseInt(amount.getText().toString()),
                        incomeSwitch.isChecked()? MoneyRecord.Type.INCOME: MoneyRecord.Type.EXPENSE,
                        description.getText().toString(),
                        currentAccount.getSlug(),
                        currentCurrency.getSlug());
                finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.accounts:
                currentAccount = dbAccounts.get(i);
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
