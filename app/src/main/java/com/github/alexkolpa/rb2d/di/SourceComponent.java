package com.github.alexkolpa.rb2d.di;

import com.github.alexkolpa.rb2d.sources.SourceSelectorFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, NetModule.class })
public interface SourceComponent {
	void inject(SourceSelectorFragment fragment);
}
