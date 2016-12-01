package nacholab.showmethemoney.api;

import java.io.IOException;

import nacholab.showmethemoney.MainApplication;
import nacholab.showmethemoney.utils.StringUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private MainApplication app;

    public AuthInterceptor(MainApplication app) {
        this.app = app;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (StringUtils.isNotBlank(app.getSession().getToken())) {
            Request.Builder requestBuilder = request.newBuilder().header(AUTHORIZATION_HEADER, app.getSession().getToken());
            return chain.proceed(requestBuilder.build());
        }else{
            return chain.proceed(request);
        }
    }

}
