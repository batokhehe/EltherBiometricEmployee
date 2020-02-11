package com.eltherbiometric.employee.retrofit;

import android.content.Context;

import com.eltherbiometric.employee.utils.Config;
import com.orhanobut.hawk.Hawk;
import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        String BASE_URL = "http://" + Hawk.get(Config.IpAddress) + "/elther_biometrics/Api/";
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ChuckInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
