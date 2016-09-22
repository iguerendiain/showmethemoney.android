package nacholab.showmethemoney.api;

import nacholab.showmethemoney.model.MainSyncData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APICalls {

    @GET("/mainSync")
    Call<MainSyncData> getMainSync();

    @POST("/mainSync")
    Call<Void> postMainSync(@Body MainSyncData data);

}
