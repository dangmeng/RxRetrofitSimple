package com.dhcc.simple.manager;

import com.dhcc.simple.service.GitHubService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/10/8.
 */

public class RetrofitManager {

    //http://gank.io/api/day/history

    public static OkHttpClient client = null;
    public static Retrofit retrofit = null;
//    public static final String BASE_URL = "https://api.github.com/";
    public static final String BASE_URL = "http://gank.io/";
    public static GitHubService service = null;

    public static OkHttpClient getOkHttpClient(){

        if (client == null){
            client = new OkHttpClient().newBuilder()
                    //.readTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();
        }
        return client;
    }

    public static Retrofit getRetrofit(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    public static GitHubService getService(){

        if (service == null) {
            service = getRetrofit().create(GitHubService.class);
        }

        return service;
    }
}
