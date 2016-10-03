package nacholab.showmethemoney.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.ui.activity.AddCurrencyActivity;
import nacholab.showmethemoney.ui.adapter.CurrencyAdapter;
import nacholab.showmethemoney.ui.view.CurrencyView;
import nacholab.showmethemoney.utils.DialogHelper;

public class CurrencyListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CurrencyView.Listener{

    @BindView(R.id.list)RecyclerView list;
    @BindView(R.id.add)View add;
    @BindView(R.id.refresh)SwipeRefreshLayout refresh;

    private CurrencyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        add.setOnClickListener(this);
        refresh.setOnRefreshListener(this);
        adapter = new CurrencyAdapter(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        refreshFromDB();
    }

    @Override
    public void onRefresh() {
        refreshFromDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                addNew();
        }
    }

    private void addNew(){
        Intent i = new Intent(getActivity(), AddCurrencyActivity.class);
        startActivity(i);
    }

    private void refreshFromDB(){
        adapter.clear();
        adapter.add(getMainApp().getDal().getCurrencies());
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }

    @Override
    public void onDelete(final Currency c) {
        DialogHelper.showConfirmationDialog(getContext(), getString(R.string.sure_to_delete_currency), new DialogHelper.ConfirmationListener() {
            @Override
            public void onConfirmationDialogYes() {
                getMainApp().getDal().markAsDeleted(c);
                adapter.remove(c);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onConfirmationDialogNo() {

            }
        });
    }

}
