package longse.com.learing.http;

import java.util.concurrent.TimeUnit;

import longse.com.learing.http.converter.GsonConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author LY
 */
public class APIRetrofit {

    private static final int DEFAULT_TIME_OUT = 10;

    private static Retrofit sRetrofit;

    private static OkHttpClient sOKHttpClient;

    public static APIRetrofit getAPIService() {
        return getInstance().create(APIRetrofit.class);
    }

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            synchronized (APIRetrofit.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl("BaseAPI")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(getOKHttpClient())
                            .build();
                }
            }
        }
        return sRetrofit;
    }

    public static OkHttpClient getOKHttpClient() {
        if (sOKHttpClient == null) {
            synchronized (APIRetrofit.class) {
                if (sOKHttpClient == null) {
                    sOKHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        return sOKHttpClient;
    }

}
