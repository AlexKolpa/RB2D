package com.github.alexkolpa.rb2d.di;

import javax.inject.Singleton;

import com.github.alexkolpa.rb2d.overview.OverviewActivity;
import com.github.alexkolpa.rb2d.rehost.AlbumActivity;
import com.github.alexkolpa.rb2d.rehost.RehostActivity;
import com.github.alexkolpa.rb2d.sources.SourceSelectorFragment;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, NetModule.class })
public interface RB2DComponent {
	void inject(OverviewActivity activity);
	void inject(SourceSelectorFragment fragment);
	void inject(RehostActivity activity);
}
