package nacholab.showmethemoney.api;

import java.util.List;

import nacholab.showmethemoney.model.Currency;
import nacholab.showmethemoney.model.MainSyncData;
import nacholab.showmethemoney.model.Session;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface APICalls {

    @GET("/mainSync")
    Call<MainSyncData> getMainSync();

    @POST("/mainSync")
    Call<Void> postMainSync(@Body MainSyncData data);

    @FormUrlEncoded
    @POST("/sessionFromGoogle")
    Call<Session> createSessionWithGoogle(@Field("googleToken") String googleToken, @Field("clientId") String clientId, @Field("clientType") String clientType);

    @GET("/currency")
    Call<List<Currency>> getCurrency();

}
