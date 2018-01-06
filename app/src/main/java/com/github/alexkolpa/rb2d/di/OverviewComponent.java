package com.github.alexkolpa.rb2d.di;

import javax.inject.Singleton;

import com.github.alexkolpa.rb2d.overview.OverviewActivity;
import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, NetModule.class })
public interface OverviewComponent {
	void inject(OverviewActivity activity);
}
