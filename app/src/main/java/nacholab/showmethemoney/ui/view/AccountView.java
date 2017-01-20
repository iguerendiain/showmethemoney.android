package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.utils.StringUtils;

public class AccountView extends RelativeLayout implements View.OnLongClickListener, View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onOpen(account);
        }else{
        }
    }

    public interface Listener{
        void onDelete(MoneyAccount a);
        void onOpen(MoneyAccount a);
    }

    @BindView(R.id.name)TextView name;
    @BindView(R.id.currency)TextView currency;
    @BindView(R.id.balance)TextView balance;

    private AccountView.Listener listener;
    private MoneyAccount account;

    @Override
    public boolean onLongClick(View v) {
        if (listener!=null){
            listener.onDelete(account);
            return true;
        }else {
            return false;
        }
    }

    public AccountView(Context context) {
        super(context);
        init();
    }

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_account_list_item, this);
        ButterKnife.bind(this);
        setOnLongClickListener(this);
        setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setAccount(MoneyAccount a){
        account = a;
        name.setText(a.getName());
        currency.setText(a.getCurrencyObject().toString());
        balance.setText(StringUtils.formatLocalized(getContext(), "%s%f", a.getCurrencyObject().getSymbol(), a.getBalance()/100f));

        if (!a.isSynced()){
            setBackgroundResource(R.color.accent);
        }else{
            setBackgroundResource(android.R.color.transparent);
        }

    }
}
