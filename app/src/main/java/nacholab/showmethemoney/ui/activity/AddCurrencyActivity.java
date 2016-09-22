package nacholab.showmethemoney.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;

public class AddCurrencyActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.slug)
    EditText slug;

    @BindView(R.id.factor)
    EditText factor;

    @BindView(R.id.createCurrency)
    View createCurrency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcurrency);
        ButterKnife.bind(this);
        createCurrency.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createCurrency:
                getMainApp().getDal().addCurrency(
                        name.getText().toString(),
                        slug.getText().toString(),
                        Float.parseFloat(factor.getText().toString())
                );
                finish();
        }
    }

}
