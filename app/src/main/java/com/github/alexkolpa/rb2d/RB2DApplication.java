package com.github.alexkolpa.rb2d;

import android.app.Application;
import com.github.alexkolpa.rb2d.di.AppModule;
import com.github.alexkolpa.rb2d.di.DaggerOverviewComponent;
import com.github.alexkolpa.rb2d.di.DaggerSourceComponent;
import com.github.alexkolpa.rb2d.di.OverviewComponent;
import com.github.alexkolpa.rb2d.di.NetModule;
import com.github.alexkolpa.rb2d.di.SourceComponent;

public class RB2DApplication extends Application {
	private static final String BASE_URL = "https://redditbooru.com";
	private OverviewComponent overviewComponent;
	private SourceComponent sourceComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		AppModule appModule = new AppModule(this);
		NetModule netModule = new NetModule(BASE_URL);
		overviewComponent = DaggerOverviewComponent.builder()
				.appModule(appModule)
				.netModule(netModule)
				.build();
		sourceComponent = DaggerSourceComponent.builder()
				.appModule(appModule)
				.netModule(netModule)
				.build();
	}

	public OverviewComponent getOverviewComponent() {
		return overviewComponent;
	}

	public SourceComponent getSourceComponent() {
		return sourceComponent;
	}
}
