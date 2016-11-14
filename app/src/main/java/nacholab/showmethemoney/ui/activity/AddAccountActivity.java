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
import nacholab.showmethemoney.ui.adapter.CurrencySpinnerAdapter;

public class AddAccountActivity extends AuthenticatedActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.balance)
    EditText balance;

    @BindView(R.id.currencies)
    Spinner currencies;

    @BindView(R.id.createAccount)
    View createAccount;

    private List<Currency> dbCurrencies;

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
        currentCurrency = dbCurrencies.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createAccount:
                getMainApp().getDal().addAccount(
                        name.getText().toString(),
                        currentCurrency.getId(),
                        Integer.parseInt(balance.getText().toString())
                );
                finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.currencies:
                currentCurrency = dbCurrencies.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
