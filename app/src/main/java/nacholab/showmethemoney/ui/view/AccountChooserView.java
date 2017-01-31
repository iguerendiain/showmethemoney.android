package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.utils.StringUtils;

public class AccountChooserView extends RelativeLayout{

    MoneyAccount account;

    @BindView(R.id.name) TextView name;

    public AccountChooserView(Context context) {
        super(context);
        init();
    }

    public AccountChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccountChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MoneyAccount getAccount(){
        return account;
    }

    private void init(){
        inflate(getContext(), R.layout.view_account_chooser_item, this);
        ButterKnife.bind(this);
    }

    public void setAccount(MoneyAccount account) {
        this.account = account;

        String displayName = StringUtils.formatLocalized(
                getContext(),
                "%s %s",
                account.getName(),
                StringUtils.formatMoneyLocalized(
                        getContext(),
                        account.getCurrency(),
                        account.getBalance()/100
                    )
                );

        name.setText(displayName);
    }
}
