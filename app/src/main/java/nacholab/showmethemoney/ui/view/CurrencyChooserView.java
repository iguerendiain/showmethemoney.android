package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MoneyAccount;
import nacholab.showmethemoney.utils.StringUtils;

public class CurrencyChooserView extends RelativeLayout{

    Currency currency;

    @BindView(R.id.name) TextView name;

    public CurrencyChooserView(Context context) {
        super(context);
        init();
    }

    public CurrencyChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Currency getCurrency(){
        return currency;
    }

    private void init(){
        inflate(getContext(), R.layout.view_currency_chooser_item, this);
        ButterKnife.bind(this);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;

        String displayName = StringUtils.formatLocalized(
                getContext(),
                "%s (%s)",
                currency.getDisplaySymbol(),
                currency.getName()
        );

        name.setText(displayName);
    }
}
