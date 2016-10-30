package nacholab.showmethemoney.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.provider.Settings;
import android.util.Log;

import java.io.IOException;

import nacholab.showmethemoney.BuildConfig;
import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.Session;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String TAG = "APICONNECTION";

    private static final String BASE_URL = "http://192.168.1.2:8050/";

    private final MainApplication app;
    private Retrofit retrofit;
    private APICalls apiCalls;

    public APIClient(MainApplication _app) {
        app = _app;
        if (retrofit == null || apiCalls == null) {

            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.addInterceptor(new LogInterceptor(TAG));
            }

            OkHttpClient okHttpClient = okHttpClientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiCalls = retrofit.create(APICalls.class);
        }
    }

    public MainSyncData getMainSyncData() throws IOException {
        try {
            return apiCalls.getMainSync().execute().body();
        } catch (IOException e) {
            handleIOException(e,"getMainSyncData");
            return null;
        }
    }

    public void postMainSyncData(MainSyncData data) throws IOException {
        try {
            apiCalls.postMainSync(data).execute();
        } catch (IOException e) {
            handleIOException(e,"postMainSyncData");
        }
    }

    public Session createSessionWithGoogle(String googleToken) throws IOException {
        try{
            String androidInstallationID = Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
            return apiCalls.createSessionWithGoogle(googleToken, androidInstallationID, "ANDROID").execute().body();
        } catch (IOException e){
            handleIOException(e, "createSessionWithGoogle");
        }

        return null;
    }

    public void createSessionWithGoogle(String googleToken, Callback<Session> cb){
        String androidInstallationID = Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
        apiCalls.createSessionWithGoogle(googleToken, androidInstallationID, "ANDROID").enqueue(cb);
    }

    private void handleIOException(IOException e, String source) throws IOException {
        Log.e(TAG,"An exception was thrown while running "+source);
        e.printStackTrace();
        throw e;
    }

}
