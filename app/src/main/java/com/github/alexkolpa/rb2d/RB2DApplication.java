package com.github.alexkolpa.rb2d;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.github.alexkolpa.rb2d.di.AppModule;
import com.github.alexkolpa.rb2d.di.DaggerRB2DComponent;
import com.github.alexkolpa.rb2d.di.NetModule;
import com.github.alexkolpa.rb2d.di.RB2DComponent;

import lombok.Getter;

public class RB2DApplication extends Application {
	private static final String BASE_URL = "https://redditbooru.com";
	@Getter
	private RB2DComponent component;

	@Override
	public void onCreate() {
		super.onCreate();
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

		component = DaggerRB2DComponent.builder()
				.appModule(new AppModule(this))
				.netModule(new NetModule(BASE_URL))
				.build();
	}
}
