package nacholab.showmethemoney.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.utils.DialogHelper;
import nacholab.showmethemoney.utils.IntentHelper;
import nacholab.showmethemoney.utils.StringUtils;

public class AddEditCurrencyActivity extends AuthenticatedActivity implements View.OnClickListener{

    public static final String EXTRA_CURRENCY_UUID = "currency_uuid";

    @BindView(R.id.back)
    View back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.delete)
    View delete;

    @BindView(R.id.save)
    View save;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.symbol)
    EditText symbol;

    @BindView(R.id.code)
    EditText code;

    @BindView(R.id.factor)
    EditText factor;

    private Currency editingCurrency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcurrency);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        String currencyUUID = IntentHelper.getExtraString(this, EXTRA_CURRENCY_UUID);
        if (currencyUUID!=null){
            editingCurrency = getMainApp().getDal().getCurrencyByUUID(currencyUUID);

            if (editingCurrency==null){
                Toast.makeText(this, R.string.currency_not_found, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                setEditMode(true);
            }
        }else{
            setEditMode(false);
        }
    }

    private void setEditMode(boolean editMode){
        if (editMode){
            title.setText(R.string.edit_currency);
            delete.setVisibility(View.VISIBLE);
            name.setText(editingCurrency.getName());
            symbol.setText(editingCurrency.getSymbol());
            code.setText(editingCurrency.getCode());
            factor.setText(StringUtils.floatToStringLocalized(this, editingCurrency.getFactor()));
        }else{
            title.setText(R.string.add_new_currency);
            delete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                if (editingCurrency!=null){
                    editingCurrency.setName(name.getText().toString());
                    editingCurrency.setSymbol(symbol.getText().toString());
                    editingCurrency.setCode(code.getText().toString());
                    editingCurrency.setFactor(Float.parseFloat(factor.getText().toString()));
                    editingCurrency.setSynced(false);
                    editingCurrency.save();
                }else {
                    getMainApp().getDal().addCurrency(
                            name.getText().toString(),
                            symbol.getText().toString(),
                            code.getText().toString(),
                            Float.parseFloat(factor.getText().toString())
                    );
                }
                finish();
            case R.id.delete:
                DialogHelper.showConfirmationDialog(this, getString(R.string.sure_to_delete_currency), new DialogHelper.ConfirmationListener() {
                    @Override
                    public void onConfirmationDialogYes() {
                        List<MoneyAccount> accounts = getMainApp().getDal().getAccountsByCurrency(editingCurrency);
                        List<MoneyRecord> records = getMainApp().getDal().getRecordsByCurrency(editingCurrency);

                        if (accounts.size()==0 && records.size()==0) {
                            getMainApp().getDal().markAsDeleted(editingCurrency);
                            finish();
                        }else{
                            Toast.makeText(AddEditCurrencyActivity.this, R.string.cant_remove_currency_stuff_using_it, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onConfirmationDialogNo() {

                    }
                });
                break;
            case R.id.back:
                onBackPressed();
        }
    }
}
