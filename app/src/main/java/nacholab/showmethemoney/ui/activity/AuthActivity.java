package nacholab.showmethemoney.ui.activity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.internal.Excluder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.wangyuwei.loadingview.LoadingView;
import nacholab.showmethemoney.R;
import nacholab.showmethemoney.model.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 6547;

    @BindView(R.id.sign_in_button) View signInButton;
    @BindView(R.id.loadingContainer) View loadingContainer;
    @BindView(R.id.loading_view) LoadingView loadingView;
    @BindView(R.id.mainContainer) View mainContainer;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getMainApp().getSession().isAuthenticated()){
            overridePendingTransition(0,0);
            goToMain();
        }else{
            setContentView(R.layout.activity_auth);
            ButterKnife.bind(this);
            setLoading(false);
            signInButton.setOnClickListener(this);

            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        }
    }

    private void setLoading(boolean loading){
        if (loading){
            loadingView.start();
            mainContainer.setVisibility(View.GONE);
            loadingContainer.setVisibility(View.VISIBLE);
        }else{
            loadingView.stop();
            mainContainer.setVisibility(View.VISIBLE);
            loadingContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                requestGoogleSignIn();
                break;
        }
    }

    private void requestGoogleSignIn(){
        setLoading(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GOOGLE_SIGN_IN_REQUEST_CODE:
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (googleSignInResult.isSuccess()) {
                    GoogleSignInAccount googleUserAccount = googleSignInResult.getSignInAccount();
                    getMainApp().getApiClient().createSessionWithGoogle(googleUserAccount.getIdToken(), new Callback<Session>() {
                        @Override
                        public void onResponse(Call<Session> call, Response<Session> response) {
                            if (response.code()==200) {
                                Session session = response.body();
                                getMainApp().getSession().setToken(session.getSessionId());
                                goToMain();
                            }else{
                                String error;
                                try{
                                    error = response.errorBody().string();
                                }catch (Exception e){
                                    error = getString(R.string.api_connection_error);
                                }

                                error+=" ("+response.code()+")";

                                Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();
                                setLoading(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<Session> call, Throwable t) {
                            Toast.makeText(AuthActivity.this, R.string.api_connection_error, Toast.LENGTH_LONG).show();
                            setLoading(false);
                        }
                    });
                }else{
                    setLoading(false);
                    Toast.makeText(this, googleSignInResult.getStatus().toString(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
