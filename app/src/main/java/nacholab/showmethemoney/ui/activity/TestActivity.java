package nacholab.showmethemoney.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.orm.SugarRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.sync.SyncUtils;

public class TestActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.syncNow)
    View syncNow;

    @BindView(R.id.currencies)
    TextView currenciesDisplay;

    @BindView(R.id.records)
    TextView recordsDisplay;

    @BindView(R.id.accounts)
    TextView accountsDisplay;

    @BindView(R.id.refresh)
    View refresh;

    @BindView(R.id.createRecord)
    View createRecord;

    private String toStringLines(List stuff){
        String result = "";
        for (Object o : stuff){
            result+=o.toString()+"\n";
        }

        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        syncNow.setOnClickListener(this);
        refresh.setOnClickListener(this);
        createRecord.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.syncNow:
                requestSyncNow();
                break;
            case R.id.createRecord:
                Intent i = new Intent(this, AddRecordActivity.class);
                startActivity(i);
                break;
            case R.id.refresh:
                refreshData();
        }
    }

    private void refreshData(){
        currenciesDisplay.setText(toStringLines(SugarRecord.listAll(Currency.class)));
        recordsDisplay.setText(toStringLines(SugarRecord.listAll(MoneyRecord.class)));
        accountsDisplay.setText(toStringLines(SugarRecord.listAll(MoneyAccount.class)));
    }

    private void requestSyncNow(){
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getMainApp().getSyncAccount(), SyncUtils.AUTHORITY, settingsBundle);
    }
}
