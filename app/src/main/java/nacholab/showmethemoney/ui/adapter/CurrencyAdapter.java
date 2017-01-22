package nacholab.showmethemoney.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.ui.view.CurrencyView;

public class CurrencyAdapter extends RecyclerView.Adapter implements CurrencyView.Listener {

    private final MainApplication app;
    private List<Currency> currencies;
    private final CurrencyView.Listener listener;
    private Currency selectedCurrency;

    public CurrencyAdapter(MainApplication _app, CurrencyView.Listener _listener) {
        app = _app;
        listener = _listener;
        rebuildList(null);
        notifyDataSetChanged();
    }

    public void search(String searchQuery){
        rebuildList(searchQuery);
        notifyDataSetChanged();
    }

    private void rebuildList(String searchQuery){
        currencies = app.getDal().findCurrencies(searchQuery);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CurrencyView v = new CurrencyView(parent.getContext());
        v.setListener(this);
        return new CurrencyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Currency currency = currencies.get(position);
        ((CurrencyViewHolder)holder).v.setCurrency(currency, currency==selectedCurrency);
    }

    @Override
    public int getItemCount() {
        if (currencies!=null) {
            return currencies.size();
        }else{
            return 0;
        }
    }

    @Override
    public void onCurrencySelected(Currency c) {
        selectedCurrency = c;
        if (listener!=null){
            listener.onCurrencySelected(c);
        }
    }

    private class CurrencyViewHolder extends RecyclerView.ViewHolder{
        CurrencyView v;

        CurrencyViewHolder(CurrencyView _v) {
            super(_v);
            v = _v;
        }
    }

}
