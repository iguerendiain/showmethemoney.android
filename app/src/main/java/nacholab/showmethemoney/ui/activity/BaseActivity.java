package nacholab.showmethemoney.ui.activity;

import android.support.v4.app.FragmentActivity;

import nacholab.showmethemoney.MainApplication;

public abstract class BaseActivity extends FragmentActivity{
    protected MainApplication getMainApp(){
        return (MainApplication) getApplication();
    }
}
