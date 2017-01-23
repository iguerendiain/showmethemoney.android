package nacholab.showmethemoney.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.view.AccountChooserView;
import nacholab.showmethemoney.ui.view.CurrencyChooserView;

public class CurrencyChooserAdapter extends PagerAdapter{

    private final Context ctx;
    private List<Currency> currencies;

    public CurrencyChooserAdapter(Context _ctx, List<Currency> _currencies) {
        currencies = _currencies;
        ctx = _ctx;
    }

    @Override
    public int getItemPosition(Object object) {
        return currencies.indexOf(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CurrencyChooserView v = new CurrencyChooserView(ctx);
        v.setCurrency(currencies.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
