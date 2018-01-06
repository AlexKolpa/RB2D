package com.github.alexkolpa.rb2d.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexkolpa.rb2d.net.RedditBooruService;

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

	// Constructor needs one parameter to instantiate.
	public NetModule(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	// Dagger will only look for methods annotated with @Provides
	@Provides
	@Singleton
	// Application reference must come from AppModule.class
	SharedPreferences providesSharedPreferences(Application application) {
		return PreferenceManager.getDefaultSharedPreferences(application);
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
	OkHttpClient provideOkHttpClient(Cache cache) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.cache(cache);
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
	RedditBooruService provideRedditBooruService(Retrofit retrofit) {
		return retrofit.create(RedditBooruService.class);
	}
}
