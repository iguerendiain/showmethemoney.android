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
import nacholab.showmethemoney.ui.activity.AddEditCurrencyActivity;
import nacholab.showmethemoney.ui.adapter.CurrencyAdapter;

public class CurrencyListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

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
        adapter = new CurrencyAdapter();
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
        Intent i = new Intent(getActivity(), AddEditCurrencyActivity.class);
        startActivity(i);
    }

    private void refreshFromDB(){
        adapter.clear();
        adapter.add(getMainApp().getDal().getCurrencies());
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}
