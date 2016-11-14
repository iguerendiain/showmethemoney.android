package nacholab.showmethemoney.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

class LogInterceptor implements Interceptor {

    private final String logTag;

    LogInterceptor(String _logTag) {
        logTag = _logTag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        final Request copy = request.newBuilder().build();
        final Buffer buffer = new Buffer();
        String requestBody = "";
        if (copy.body()!=null) {
            copy.body().writeTo(buffer);
            requestBody = buffer.readUtf8();
        }
        Log.d(logTag, "SENDING REQUEST\n" + request.method() + " " + request.url() + "\n" + request.headers() + requestBody);

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }

        Log.d(logTag,String.format("GOT %d RESPONSE in %.1fms",response.code(), (t2 - t1) / 1e6d));
        Log.d(logTag,response.headers()+bodyString);

        if (response.body() != null) {
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }
}
