package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.ui.activity.AddEditCurrencyActivity;

public class CurrencyView extends RelativeLayout implements View.OnClickListener {

    @BindView(R.id.name)TextView name;
    @BindView(R.id.factor)TextView factor;

    private Currency currency;

    public CurrencyView(Context context) {
        super(context);
        init();
    }

    public CurrencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_currency_list_item, this);
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    public void setCurrency(Currency c){
        currency = c;
        name.setText(c.getName());
        factor.setText("x"+c.getFactor());

        if (!c.isSynced()){
            setBackgroundResource(R.color.accent);
        }else{
            setBackgroundResource(android.R.color.transparent);
        }

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getContext(), AddEditCurrencyActivity.class);
        i.putExtra(AddEditCurrencyActivity.EXTRA_CURRENCY_UUID, currency.getUuid());
        getContext().startActivity(i);
    }
}
