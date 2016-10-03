package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Currency;

public class CurrencyView extends RelativeLayout implements View.OnLongClickListener {

    public interface Listener{
        void onDelete(Currency c);
    }

    @BindView(R.id.name)TextView name;
    @BindView(R.id.slug)TextView slug;
    @BindView(R.id.factor)TextView factor;

    private Listener listener;
    private Currency currency;

    @Override
    public boolean onLongClick(View v) {
        if (listener!=null){
            listener.onDelete(currency);
            return true;
        }else {
            return false;
        }
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

    public CurrencyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_currency_list_item, this);
        ButterKnife.bind(this);
        setOnLongClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCurrency(Currency c){
        currency = c;
        name.setText(c.getName());
        slug.setText(c.getSlug());
        factor.setText("x"+c.getFactor());

        if (!c.isSynced()){
            setBackgroundResource(R.color.accent);
        }else{
            setBackgroundResource(android.R.color.transparent);
        }

    }
}
