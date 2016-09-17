package nacholab.showmethemoney.api;

import android.util.Log;

import java.io.IOException;

import nacholab.showmethemoney.BuildConfig;
import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.model.MainSyncData;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String TAG = "APICONNECTION";

    private static final String BASE_URL = "http://192.168.1.2:7474/";

    private final MainApplication app;
    private Retrofit retrofit;
    private APICalls apiCalls;

    public APIClient(MainApplication _app) {
        app = _app;
        if (retrofit == null || apiCalls == null) {

//            Gson gson = new GsonBuilder()
//                    .excludeFieldsWithoutExposeAnnotation()
//                    .create();

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                okHttpClientBuilder.addInterceptor(new LogInterceptor(TAG));
            }

            OkHttpClient okHttpClient = okHttpClientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiCalls = retrofit.create(APICalls.class);
        }
    }

    public MainSyncData getMainSyncData() {
        try {
            return apiCalls.getMainSync().execute().body();
        } catch (IOException e) {
            handleIOException(e,"getMainSyncData");
            return null;
        }
    }

    public void postMainSyncData(MainSyncData data) {
        try {
            apiCalls.postMainSync(data).execute();
        } catch (IOException e) {
            handleIOException(e,"postMainSyncData");
        }
    }

    private void handleIOException(IOException e, String source){
        Log.e(TAG,"An exception was thrown while running "+source);
        e.printStackTrace();
    }

}
