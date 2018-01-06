package com.github.alexkolpa.rb2d;

import android.app.Application;
import com.github.alexkolpa.rb2d.di.AppModule;
import com.github.alexkolpa.rb2d.di.DaggerNetComponent;
import com.github.alexkolpa.rb2d.di.NetComponent;
import com.github.alexkolpa.rb2d.di.NetModule;

public class RB2DApplication extends Application {
	private static final String BASE_URL = "https://redditbooru.com";
	private NetComponent netComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		netComponent = DaggerNetComponent.builder()
				.appModule(new AppModule(this))
				.netModule(new NetModule(BASE_URL))
				.build();
	}

	public NetComponent getNetComponent() {
		return netComponent;
	}
}
