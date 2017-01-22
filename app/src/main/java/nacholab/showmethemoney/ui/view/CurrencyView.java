package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;

public class CurrencyView extends RelativeLayout implements View.OnClickListener {

    @BindView(R.id.name) TextView name;

    private Listener listener;
    private Currency currency;

    @Override
    public void onClick(View v) {
        if (v.getId() == getId() && listener!=null){
            listener.onCurrencySelected(currency);
        }
    }

    public interface Listener{
        void onCurrencySelected(Currency c);
    }

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
        inflate(getContext(), R.layout.view_currency_item_list, this);
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCurrency(Currency c, boolean selected){
        currency = c;
        name.setText(currency.getName());
        if (selected){
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.accent));
        }else{
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryLight));
        }
    }
}
