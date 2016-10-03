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
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.ui.activity.AddAccountActivity;
import nacholab.showmethemoney.ui.adapter.AccountAdapter;
import nacholab.showmethemoney.ui.view.AccountView;
import nacholab.showmethemoney.utils.DialogHelper;

public class AccountsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AccountView.Listener {

    @BindView(R.id.list)RecyclerView list;
    @BindView(R.id.add)View add;
    @BindView(R.id.refresh)SwipeRefreshLayout refresh;

    private AccountAdapter adapter;

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
        adapter = new AccountAdapter(this);
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
        Intent i = new Intent(getActivity(), AddAccountActivity.class);
        startActivity(i);
    }

    private void refreshFromDB(){
        adapter.clear();
        adapter.add(getMainApp().getDal().getAccounts());
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }

    @Override
    public void onDelete(final MoneyAccount a) {
        DialogHelper.showConfirmationDialog(getContext(), getString(R.string.sure_to_delete_account), new DialogHelper.ConfirmationListener() {
            @Override
            public void onConfirmationDialogYes() {
                getMainApp().getDal().markAsDeleted(a);
                adapter.remove(a);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onConfirmationDialogNo() {

            }
        });
    }
}