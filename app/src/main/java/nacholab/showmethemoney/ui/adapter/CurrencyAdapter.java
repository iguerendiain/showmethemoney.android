package nacholab.showmethemoney.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.ui.view.CurrencyView;

public class CurrencyAdapter extends RecyclerView.Adapter{

    private List<Currency> currencies;

    public CurrencyAdapter() {
        currencies = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CurrencyView v = new CurrencyView(parent.getContext());
        return new CurrencyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CurrencyViewHolder)holder).v.setCurrency(currencies.get(position));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void clear(){
        currencies.clear();
    }

    public void add(List<Currency> r){
        currencies.addAll(r);
    }

    public void remove(Currency c) {
        currencies.remove(c);
    }

    private class CurrencyViewHolder extends RecyclerView.ViewHolder{
        CurrencyView v;

        public CurrencyViewHolder(CurrencyView _v) {
            super(_v);
            v = _v;
        }
    }

}
