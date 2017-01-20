package nacholab.showmethemoney.sync;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.Currency;

public class CurrencyUpdaterTask extends AsyncTask<Void, Void, Void>{

    private final Context ctx;
    private final Listener listener;

    public interface Listener{
        void onFinish();
    }

    public CurrencyUpdaterTask(Context _ctx, Listener _listener) {
        super();
        ctx = _ctx;
        listener = _listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MainApplication app = (MainApplication) ctx.getApplicationContext();
            List<Currency> currencies = app.getApiClient().getCurrency();
            app.getDal().saveOrUpdateCurrency(currencies);
        } catch (IOException e) {
            e.printStackTrace();
            if (listener!=null) {
                listener.onFinish();
            }
        }

        if (listener!=null) {
            listener.onFinish();
        }
        return null;
    }
}
