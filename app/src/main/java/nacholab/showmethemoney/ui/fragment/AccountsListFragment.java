package nacholab.showmethemoney.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.sync.MainSyncTask;
import nacholab.showmethemoney.ui.activity.AddEditAccountActivity;
import nacholab.showmethemoney.ui.adapter.AccountAdapter;
import nacholab.showmethemoney.ui.view.AccountView;
import nacholab.showmethemoney.utils.DialogHelper;

public class AccountsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AccountView.Listener {

    public final static int ADD_EDIT_ACCOUNT_REQUEST_CODE = 1;

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
        new MainSyncTask(getMainApp(), null).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                addNew();
        }
    }

    private void addNew(){
        if (getMainApp().getDal().getAccounts(false).size() > 0) {
            Intent i = new Intent(getActivity(), AddEditAccountActivity.class);
            startActivity(i);
        }else{
            DialogHelper.showInformationDialog(getContext(), R.string.cant_add_record_no_accounts, R.string.ok, null);
        }
    }

    private void refreshFromDB(){
        adapter.clear();
        adapter.add(getMainApp().getDal().getAccounts(true));
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }

    @Override
    public void onDelete(final MoneyAccount a) {
        List<MoneyRecord> accountRecords = getMainApp().getDal().getRecordsByAccount(a);
        if (accountRecords==null || accountRecords.size()==0){
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
        }else{
            DialogHelper.showInformationDialog(getContext(), R.string.cant_delete_account_with_records, R.string.ok, null);
        }

    }

    @Override
    public void onOpen(MoneyAccount a) {
        Intent i = new Intent(getActivity(), AddEditAccountActivity.class);
        i.putExtra(AddEditAccountActivity.ACCOUNT_UUID, a.getUuid());
        startActivityForResult(i, ADD_EDIT_ACCOUNT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            refreshFromDB();
        }
    }
}
