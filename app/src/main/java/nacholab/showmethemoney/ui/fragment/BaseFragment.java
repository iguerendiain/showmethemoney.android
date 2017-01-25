package nacholab.showmethemoney.ui.fragment;

import android.support.v4.app.Fragment;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.ui.activity.BaseActivity;

public abstract class BaseFragment extends Fragment{

    protected BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }

    protected MainApplication getMainApp() {
        return (MainApplication) getActivity().getApplication();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainApp().getOtto().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getMainApp().getOtto().unregister(this);
    }
}
