package nacholab.showmethemoney.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.MoneyAccount;

public class AccountSpinnerAdapter implements SpinnerAdapter {

    private final List<MoneyAccount> accounts;
    private final Context ctx;

    public AccountSpinnerAdapter(Context _ctx, List<MoneyAccount> _accounts) {
        accounts = _accounts;
        ctx = _ctx;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getView(i,view);
    }

    private View getView(int i, View view) {
        if (view==null){
            view = View.inflate(ctx, R.layout.view_base_spinner, null);
        }

        ((TextView)view).setText(getItem(i).getName());

        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public MoneyAccount getItem(int i) {
        return accounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getView(i,view);
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
