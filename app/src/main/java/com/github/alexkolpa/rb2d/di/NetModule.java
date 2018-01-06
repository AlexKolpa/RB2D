package com.github.alexkolpa.rb2d.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexkolpa.rb2d.net.ImageService;
import com.github.alexkolpa.rb2d.net.SourceInterceptor;
import com.github.alexkolpa.rb2d.net.SourceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class NetModule {

	String baseUrl;

	public NetModule(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Provides
	@Singleton
	Cache provideOkHttpCache(Application application) {
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		return new Cache(application.getCacheDir(), cacheSize);
	}

	@Provides
	@Singleton
	ObjectMapper provideJackson() {
		return new ObjectMapper();
	}

	@Provides
	@Singleton
	OkHttpClient provideOkHttpClient(Cache cache, SourceInterceptor interceptor) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.cache(cache);
		builder.addNetworkInterceptor(interceptor);
		return builder.build();
	}

	@Provides
	@Singleton
	Retrofit provideRetrofit(ObjectMapper objectMapper, OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.addConverterFactory(JacksonConverterFactory.create(objectMapper))
				.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
				.baseUrl(baseUrl)
				.client(okHttpClient)
				.build();
	}

	@Provides
	@Singleton
	ImageService provideImageService(Retrofit retrofit) {
		return retrofit.create(ImageService.class);
	}

	@Provides
	@Singleton
	SourceService provideSourceService(Retrofit retrofit) {
		return retrofit.create(SourceService.class);
	}
}
