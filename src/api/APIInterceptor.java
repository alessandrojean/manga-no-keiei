package api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIInterceptor implements Interceptor {
    private final String userAgent;

    public APIInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", userAgent)
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}
