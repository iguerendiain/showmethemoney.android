package nacholab.showmethemoney.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.ui.adapter.AccountChooserAdapter;
import nacholab.showmethemoney.ui.adapter.CurrencyChooserAdapter;
import nacholab.showmethemoney.ui.view.TagView;
import nacholab.showmethemoney.ui.view.TagsInputView;
import nacholab.showmethemoney.utils.MoneyHelper;
import nacholab.showmethemoney.utils.StringUtils;

public class AddEditRecordActivity extends AuthenticatedActivity implements View.OnClickListener  {

    public static final String RECORD_UUID = "uuid";

    @BindView(R.id.account_chooser) ViewPager accountChooser;
    @BindView(R.id.currency_chooser) ViewPager currencyChooser;
    @BindView(R.id.income) RadioButton income;
    @BindView(R.id.expense) RadioButton expense;
    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.description) EditText description;
    @BindView(R.id.createRecord) View createRecord;
    @BindView(R.id.back) View back;
    @BindView(R.id.tagsInput) TagsInputView tagsInput;
    @BindView(R.id.suggested_tags) ViewGroup suggestedTagsContainer;

    private List<MoneyAccount> dbAccounts;
    private List<Currency> dbCurrencies;

    private MoneyAccount currentAccount;
    private Currency currentCurrency;
    private Currency accountCurrency;

    private MoneyRecord editingRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeditrecord);
        ButterKnife.bind(this);
        createRecord.setOnClickListener(this);
        back.setOnClickListener(this);

        dbAccounts = getMainApp().getDal().getAccountsByUse();
        dbCurrencies = getMainApp().getDal().getCurrencyByUse();

        accountChooser.setAdapter(new AccountChooserAdapter(this, dbAccounts));
        accountChooser.addOnPageChangeListener(new AccountListener());

        currencyChooser.setAdapter(new CurrencyChooserAdapter(this, dbCurrencies));
        currencyChooser.addOnPageChangeListener(new CurrencyListener());

        ArrayAdapter tagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getMainApp().getDal().findTagsByUsage());
        tagsInput.setAdapter(tagsAdapter);

        String[] suggestedTags = getMainApp().getDal().findSuggestedTags(-1,-1,-1,-1);

        for (String tag : suggestedTags){
            TagView tagView = new TagView(this);
            tagView.setText(tag);
            suggestedTagsContainer.addView(tagView);
        }

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
                    break;
                }
            }

            for (int c=0; c<dbCurrencies.size(); c++){
                if (dbCurrencies.get(c).getCode().equals(editingRecord.getCurrency())){
                    setCurrency(c);
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
            if (editingRecord.getType()==MoneyRecord.Type.income) {
                income.setChecked(true);
            }else{
                expense.setChecked(true);
            }

            description.setText(editingRecord.getDescription());
        }else{
            setAccount(0);
            expense.setChecked(true);
            amount.requestFocus();
        }
    }

    private void setAccount(int idx){
        currentAccount = dbAccounts.get(idx);
        accountChooser.setCurrentItem(idx);
        String currencyCode = currentAccount.getCurrency();
        for (int c=0; c<dbCurrencies.size(); c++){
            if (dbCurrencies.get(c).getCode().equals(currencyCode)){
                currencyChooser.setCurrentItem(c);
                currentCurrency = dbCurrencies.get(c);
                accountCurrency = dbCurrencies.get(c);
                break;
            }
        }
    }

    private void setCurrency(int idx){
        currencyChooser.setCurrentItem(idx);
        currentCurrency = dbCurrencies.get(idx);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.createRecord:
                int realAmount;
                int writtenAmount;

                if (StringUtils.isNotBlank(amount.getText().toString())) {
                    try {
                        writtenAmount = (int) Math.floor(Float.parseFloat(amount.getText().toString()) * 100);
                    }catch (Exception e){
                        Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    writtenAmount = 0;
                }


                if (currentCurrency.getCode().equals(currentAccount.getCurrency())) {
                    realAmount = writtenAmount;
                } else {
                    realAmount = MoneyHelper.convert(writtenAmount, currentCurrency.getFactor(), accountCurrency.getFactor());
                }

                MoneyRecord.Type recordType = income.isChecked() ? MoneyRecord.Type.income : MoneyRecord.Type.expense;

                String descriptionText = description.getText().toString();
                String account = currentAccount.getUuid();
                String currency = currentCurrency.getCode();
                long time = System.currentTimeMillis();

                setResult(Activity.RESULT_OK);
                if (editingRecord!=null) {
                    getMainApp().getDal().updateRecord(
                        editingRecord.getUuid(),
                        realAmount,
                        recordType,
                        descriptionText,
                        account,
                        currency,
                        time,
                        editingRecord.getLoclat(),
                        editingRecord.getLoclng(),
                        true
                    );
                }else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    Location loc=null;
                    if (locationManager != null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }

                    double lat = 0;
                    double lng = 0;
                    if (loc!=null){
                        lat = loc.getLatitude();
                        lng = loc.getLongitude();
                    }

                    getMainApp().getDal().addRecord(
                        realAmount,recordType,descriptionText,account,currency,time,lat,lng,true
                    );
                }
                finish();
        }
    }

    private class CurrencyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setCurrency(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class AccountListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setAccount(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
