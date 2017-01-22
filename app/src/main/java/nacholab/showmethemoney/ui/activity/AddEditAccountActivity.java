package nacholab.showmethemoney.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.adapter.CurrencySpinnerAdapter;
import nacholab.showmethemoney.utils.StringUtils;

public class AddEditAccountActivity extends AuthenticatedActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String ACCOUNT_UUID = "uuid";

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.balance)
    EditText balance;

    @BindView(R.id.currencies)
    Spinner currencies;

    @BindView(R.id.createAccount)
    View createAccount;

    private List<Currency> dbCurrencies;
    private MoneyAccount editingAccount;

    private Currency currentCurrency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaccount);
        ButterKnife.bind(this);
        dbCurrencies = getMainApp().getDal().getCurrencies();
        currencies.setAdapter(new CurrencySpinnerAdapter(this, dbCurrencies));
        currencies.setOnItemSelectedListener(this);
        createAccount.setOnClickListener(this);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            String requestedAccountUUID = getIntent().getExtras().getString(ACCOUNT_UUID);
            if (requestedAccountUUID!=null){
                editingAccount = getMainApp().getDal().getAccountByUUID(requestedAccountUUID);
            }
        }

        if (editingAccount!=null){
            name.setText(editingAccount.getName());

            for (int c=0; c<dbCurrencies.size(); c++){
                if (dbCurrencies.get(c).getCode().equals(editingAccount.getCurrency())){
                    currentCurrency = dbCurrencies.get(c);
                    currencies.setSelection(c);
                    break;
                }
            }

            balance.setText(StringUtils.floatToStringLocalized(this, editingAccount.getBalance()/100f));

        }else{
            currentCurrency = dbCurrencies.get(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createAccount:
                int parsedBalance = (int) Math.floor(Float.parseFloat(balance.getText().toString()) * 100);
                String nameText = name.getText().toString();
                String currency = currentCurrency.getCode();

                if (editingAccount!=null){
                    getMainApp().getDal().updateAccount(editingAccount.getUuid(), nameText);
                }else {
                    getMainApp().getDal().addAccount(nameText, currency, parsedBalance);
                }
                finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.currencies:
                currentCurrency = dbCurrencies.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}