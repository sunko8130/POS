package com.example.pos.di.module;

import com.example.pos.network.NetworkRepository;
import com.example.pos.network.RetrofitService;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.pos.util.Constant.BASE_URL;
import static com.example.pos.util.Constant.CONNECTION_TIMEOUT;

@Module(includes = {ViewModelModule.class})
public class ApplicationModule {

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Provides
    @Singleton
    @Named("retrofit_1")
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    static RetrofitService provideRetrofitService(@Named("retrofit_1") Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }


    @Singleton
    @Provides
    static NetworkRepository provideNetworkRepository(RetrofitService retrofitService) {
        return new NetworkRepository(retrofitService);
    }

}
