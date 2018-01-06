package com.github.alexkolpa.rb2d.data;

import com.github.alexkolpa.rb2d.entity.Source;
import com.github.alexkolpa.rb2d.net.SourceService;
import com.github.alexkolpa.rb2d.sources.SourceManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class SourcesPresenter extends Presenter<SourcesView> {

	private final SourceService sourceService;
	private final SourceManager sourceManager;

	@Inject
	SourcesPresenter(SourceService sourceService, SourceManager sourceManager) {
		this.sourceService = sourceService;
		this.sourceManager = sourceManager;
	}

	public Maybe<List<Source>> getSources() {
		return sourceService.getSources().filter(x -> getView() != null);
	}

	public void setSources(List<Source> sources) {
		Set<String> sourceIds = new HashSet<>();
		for (Source source : sources) {
			if (source.isChecked()) {
				sourceIds.add(source.getValue());
			}
		}

		sourceManager.setSources(sourceIds);
	}
}
