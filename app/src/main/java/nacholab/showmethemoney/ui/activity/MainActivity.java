package nacholab.showmethemoney.ui.activity;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.sync.MainSyncTask;
import nacholab.showmethemoney.ui.fragment.AccountsListFragment;
import nacholab.showmethemoney.ui.fragment.RecordListFragment;

public class MainActivity extends AuthenticatedActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String FRAGMENT_TAG_RECORDS = "records";
    private static final String FRAGMENT_TAG_ACCOUNTS = "accounts";

    @BindView(R.id.tabs)SmartTabLayout tabs;
    @BindView(R.id.panes)ViewPager panes;
    @BindView(R.id.title)TextView title;
    @BindView(R.id.refresh)View refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        refresh.setOnClickListener(this);

        initTabs();

        initDefaultTab();
    }

    private void initDefaultTab(){
        title.setText(R.string.records);
    }

    private void initTabs(){
        FragmentPagerItems.Creator paneItemsCreator = FragmentPagerItems.with(this);

        paneItemsCreator.add(FRAGMENT_TAG_RECORDS, RecordListFragment.class);
        paneItemsCreator.add(FRAGMENT_TAG_ACCOUNTS, AccountsListFragment.class);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), paneItemsCreator.create());

        final LayoutInflater inflater = LayoutInflater.from(tabs.getContext());

        tabs.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                return inflater.inflate(R.layout.view_tab, container,false);
            }
        });

        panes.setAdapter(adapter);
        tabs.setViewPager(panes);
        panes.setOffscreenPageLimit(4);

        ((ImageView) tabs.getTabAt(0)).setImageResource(R.drawable.ic_action_action_view_list);
        ((ImageView) tabs.getTabAt(1)).setImageResource(R.drawable.ic_action_action_account_balance);

        tabs.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
            case 0:
                title.setText(R.string.records);
                return;
            case 1:
                title.setText(R.string.accounts);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                backendSync();
        }
    }

    private void backendSync(){
        new MainSyncTask(this, null).execute();
    }
}