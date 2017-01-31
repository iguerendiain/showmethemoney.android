package nacholab.showmethemoney.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class AuthenticatedActivity extends BaseActivity {

    @Override
    protected void onResume() {
        ensureAuthentication();
        super.onResume();
    }

    public void ensureAuthentication(){
        if (!getMainApp().getSession().isAuthenticated()){
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(0,0);
            startActivity(intent);
            finish();
        }
    }
}
