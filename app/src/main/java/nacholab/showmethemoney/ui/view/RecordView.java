package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.MoneyRecord;
import nacholab.showmethemoney.utils.StringUtils;

public class RecordView extends RelativeLayout implements View.OnLongClickListener {

    public interface Listener{
        void onDelete(MoneyRecord r);
    }

    @BindView(R.id.description)TextView description;
    @BindView(R.id.account)TextView account;
    @BindView(R.id.currency)TextView currency;
    @BindView(R.id.amount)TextView amount;
    @BindView(R.id.type)ImageView type;

    private Listener listener;
    private MoneyRecord record;

    public RecordView(Context context) {
        super(context);
        init();
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_record_list_item, this);
        ButterKnife.bind(this);
        setOnLongClickListener(this);
    }

    public void setRecord(MoneyRecord r){
        record = r;

        if (r.getType()!=null){
            type.setVisibility(VISIBLE);
            switch (r.getType()){
                case expense:
                    type.setImageResource(R.drawable.expense_minus_sign);
                    break;
                case income:
                    type.setImageResource(R.drawable.income_plus_sign);
                    break;
                case patch:
                    type.setImageResource(R.drawable.adjustment_pound_sign);
                    break;
            }
        }else{
            type.setVisibility(INVISIBLE);
        }

        description.setText(r.getDescription());
        account.setText(r.getAccount());
        currency.setText(r.getCurrency());
        amount.setText(StringUtils.formatLocalized(getContext(), "%f", r.getAmount()/100f));

        if (!r.isSynced()){
            setBackgroundResource(R.color.accent);
        }else{
            setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener!=null){
            listener.onDelete(record);
            return true;
        }else {
            return false;
        }
    }
}
