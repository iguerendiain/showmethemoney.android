package nacholab.showmethemoney.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.view.AccountChooserView;

public class AccountChooserAdapter extends PagerAdapter{

    private final Context ctx;
    private List<MoneyAccount> accounts;

    public AccountChooserAdapter(Context _ctx, List<MoneyAccount> _accounts) {
        accounts = _accounts;
        ctx = _ctx;
    }

    @Override
    public int getItemPosition(Object object) {
        return accounts.indexOf(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AccountChooserView v = new AccountChooserView(ctx);
        v.setAccount(accounts.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
