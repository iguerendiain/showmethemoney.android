package nacholab.showmethemoney.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;

public class TagView extends RelativeLayout{

    @BindView(R.id.text) TextView textView;

    public TagView(Context context) {
        super(context);
        init();
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_tag, this);
        ButterKnife.bind(this);
    }

    public void setText(String txt){
        textView.setText(txt);
    }

}
