package nacholab.showmethemoney.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.adapter.CurrencyAdapter;
import nacholab.showmethemoney.ui.view.CurrencyView;
import nacholab.showmethemoney.utils.DialogHelper;
import nacholab.showmethemoney.utils.MoneyHelper;
import nacholab.showmethemoney.utils.StringUtils;

public class AddEditAccountActivity extends AuthenticatedActivity implements View.OnClickListener, CurrencyView.Listener, TextWatcher {

    public static final String ACCOUNT_UUID = "uuid";

    @BindView(R.id.name) EditText name;
    @BindView(R.id.balance) EditText balance;
    @BindView(R.id.currencies) RecyclerView currencies;
    @BindView(R.id.createAccount) ImageView createAccount;
    @BindView(R.id.currency_search) EditText currencySearch;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.add_container) View addContainer;
    @BindView(R.id.edit_container) View editContainer;
    @BindView(R.id.saved_balance) TextView savedBalance;
    @BindView(R.id.saved_currency) TextView savedCurrency;

    private List<Currency> dbCurrencies;
    private MoneyAccount editingAccount;

    private Currency currentCurrency;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaccount);
        ButterKnife.bind(this);
        createAccount.setOnClickListener(this);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            String requestedAccountUUID = getIntent().getExtras().getString(ACCOUNT_UUID);
            if (requestedAccountUUID!=null){
                editingAccount = getMainApp().getDal().getAccountByUUID(requestedAccountUUID);
            }
        }

        if (editingAccount!=null){
            setEditMode(true);
            name.setText(editingAccount.getName());
            currentCurrency = getMainApp().getDal().getCurrencyByCode(editingAccount.getCurrency());
            savedCurrency.setText(currentCurrency.getName());
            savedBalance.setText(StringUtils.formatMoneyLocalized(this, currentCurrency.getDisplaySymbol(), editingAccount.getBalance()/100));
        }else{
            setEditMode(false);
            dbCurrencies = getMainApp().getDal().getCurrencies();
            currencyAdapter = new CurrencyAdapter(getMainApp(), this);
            currencies.setAdapter(currencyAdapter);
            currencies.setLayoutManager(new LinearLayoutManager(this));
            currencySearch.addTextChangedListener(this);
        }
    }

    private void setEditMode(boolean editMode){
        if (editMode){
            title.setText(R.string.edit_account);
            createAccount.setImageResource(R.drawable.ic_save);
            editContainer.setVisibility(View.VISIBLE);
            addContainer.setVisibility(View.GONE);
        }else{
            title.setText(R.string.create_account);
            createAccount.setImageResource(R.drawable.ic_action_action_done);
            editContainer.setVisibility(View.GONE);
            addContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createAccount:
                String nameText = name.getText().toString();

                if (editingAccount!=null){
                    if (StringUtils.isNotBlank(nameText)) {
                        getMainApp().getDal().updateAccount(editingAccount.getUuid(), nameText);
                        finish();
                    }else{
                        DialogHelper.showInformationDialog(this, R.string.name_cant_be_empty, R.string.ok, null);
                    }
                }else {
                    if (StringUtils.isNotBlank(nameText) && currentCurrency!=null) {
                        String currency = currentCurrency.getCode();
                        String balanceText = balance.getText().toString();

                        int parsedBalance = 0;
                        boolean validBalance = false;
                        if (StringUtils.isNotBlank(balanceText)) {
                            try {
                                parsedBalance = (int) Math.floor(Float.parseFloat(balanceText) * 100);
                                validBalance = true;
                            } catch(Exception e) {
                                DialogHelper.showInformationDialog(this, R.string.invalid_balance, R.string.ok, null);
                            }
                        }else{
                            parsedBalance = 0;
                            validBalance = true;
                        }

                        if (validBalance){
                            getMainApp().getDal().addAccount(nameText, currency, parsedBalance);
                            finish();
                        }
                    }else {
                        DialogHelper.showInformationDialog(this, R.string.name_and_currency_cant_be_empty, R.string.ok, null);
                    }
                }
        }
    }

    @Override
    public void onCurrencySelected(Currency c) {
        currentCurrency = c;
        currencySearch.setText(c.getName());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        currencyAdapter.search(s.toString());
    }
}