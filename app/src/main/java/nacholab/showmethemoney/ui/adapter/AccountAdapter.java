package nacholab.showmethemoney.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.view.AccountView;

public class AccountAdapter extends RecyclerView.Adapter{

    private List<MoneyAccount> accounts;
    private AccountView.Listener listener;

    public AccountAdapter(AccountView.Listener _listener) {
        accounts = new ArrayList<>();
        listener = _listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountView v = new AccountView(parent.getContext());
        v.setListener(listener);
        return new AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AccountViewHolder)holder).v.setAccount(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void clear(){
        accounts.clear();
    }

    public void add(List<MoneyAccount> r){
        accounts.addAll(r);
    }

    public void remove(MoneyAccount a) {
        accounts.remove(a);
    }

    private class AccountViewHolder extends RecyclerView.ViewHolder{
        AccountView v;

        public AccountViewHolder(AccountView _v) {
            super(_v);
            v = _v;
        }
    }

}
