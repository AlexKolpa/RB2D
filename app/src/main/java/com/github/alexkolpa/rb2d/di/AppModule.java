package com.github.alexkolpa.rb2d.di;

import javax.inject.Singleton;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

	Application application;

	public AppModule(Application application) {
		this.application = application;
	}

	@Provides
	@Singleton
	Application providesApplication() {
		return application;
	}

	@Provides
	@Singleton
	SharedPreferences providesSharedPreferences(Application application) {
		return PreferenceManager.getDefaultSharedPreferences(application);
	}
}
