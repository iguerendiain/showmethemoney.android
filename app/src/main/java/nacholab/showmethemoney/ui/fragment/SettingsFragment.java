package nacholab.showmethemoney.ui.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.ui.activity.AuthenticatedActivity;
import nacholab.showmethemoney.utils.DialogHelper;

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.logout) View logoutView;
    @BindView(R.id.tech_info1) TextView textInfo1View;
    @BindView(R.id.tech_info2) TextView textInfo2View;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        logoutView.setOnClickListener(this);

        try {
            PackageInfo pkgInfo = getBaseActivity().getPackageManager().getPackageInfo(getBaseActivity().getPackageName(), 0);
            String versionName = pkgInfo.versionName;
            int versionCode = pkgInfo.versionCode;
            String pkgName = pkgInfo.packageName;

            textInfo1View.setText(pkgName);
            textInfo2View.setText(getString(R.string.version_info,versionName,versionCode));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            textInfo1View.setVisibility(View.GONE);
            textInfo2View.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                logout();
        }
    }

    private void logout(){
        DialogHelper.showConfirmationDialog(getContext(), R.string.logout_confirmation, new DialogHelper.ConfirmationListener() {
            @Override
            public void onConfirmationDialogYes() {
                getMainApp().getDal().clearAllData();
                getMainApp().getSession().clear();
                getMainApp().getSettings().clear();
                if (getBaseActivity() instanceof AuthenticatedActivity) {
                    ((AuthenticatedActivity) getBaseActivity()).ensureAuthentication();
                }else{
                    getBaseActivity().finish();
                }
            }

            @Override
            public void onConfirmationDialogNo() {

            }
        });
    }
}
