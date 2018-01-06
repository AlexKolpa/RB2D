package com.github.alexkolpa.rb2d.sources;

import android.content.SharedPreferences;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * manage locally stored sources
 */
public class SourceManager {

	private static final String SOURCE_KEY = "sources";
	private final SharedPreferences sharedPreferences;

	@Inject
	SourceManager(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	public Set<String> getSourceIds() {
		return sharedPreferences.getStringSet(SOURCE_KEY, Collections.emptySet());
	}

	public void setSources(Collection<String> sourceIds) {
		sharedPreferences.edit()
				.putStringSet(SOURCE_KEY, new HashSet<>(sourceIds))
				.apply();
	}
}
