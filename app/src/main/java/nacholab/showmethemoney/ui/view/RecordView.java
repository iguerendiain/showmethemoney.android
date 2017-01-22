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

public class RecordView extends RelativeLayout implements View.OnLongClickListener, View.OnClickListener {

    public interface Listener{
        void onDelete(MoneyRecord r);
        void onOpen(MoneyRecord r);
    }

    @BindView(R.id.description)TextView description;
    @BindView(R.id.account)TextView account;
    @BindView(R.id.amount)TextView amount;
    @BindView(R.id.type)ImageView type;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.not_synced) View notSynced;

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
        setOnClickListener(this);
    }

    public void setRecord(MoneyRecord r) {
        record = r;

        if (r.getType() != null) {
            type.setVisibility(VISIBLE);
            switch (r.getType()) {
                case expense:
                    type.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    break;
                case income:
                    type.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    break;
                case patch:
                    type.setImageResource(R.drawable.adjustment_pound_sign);
                    break;
            }
        } else {
            type.setVisibility(INVISIBLE);
        }

        description.setText(r.getDescription());
        account.setText(r.getAccountObject().getName());
        amount.setText(StringUtils.formatMoneyLocalized(getContext(), r.getCurrencyObject().getDisplaySymbol(), r.getAmount()/100f));
        date.setText(StringUtils.formatDateCompact(getContext(), r.getTime()));
        setSynced(r.isSynced());
    }


    private void setSynced(boolean synced){
        if (!synced){
            notSynced.setVisibility(VISIBLE);
        }else{
            notSynced.setVisibility(GONE);
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

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onOpen(record);
        }
    }
}
