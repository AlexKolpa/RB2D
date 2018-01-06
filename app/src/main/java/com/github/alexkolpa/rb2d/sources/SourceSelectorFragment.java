package com.github.alexkolpa.rb2d.sources;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexkolpa.rb2d.R;
import com.github.alexkolpa.rb2d.RB2DApplication;
import com.github.alexkolpa.rb2d.data.SourcesPresenter;
import com.github.alexkolpa.rb2d.data.SourcesView;
import com.github.alexkolpa.rb2d.entity.Source;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SourceSelectorFragment extends DialogFragment implements SourcesView {

	public interface SourceCallback {
		void onSourcesSelected();
	}

	private static final String SAVED_SOURCES = "savedSources";

	@Inject
	SourcesPresenter presenter;
	@Inject
	SourcesAdapter sourcesAdapter;
	@Inject
	ObjectMapper objectMapper;

	private AlertDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((RB2DApplication) getActivity().getApplication()).getSourceComponent().inject(this);
		presenter.attach(this);

		if (savedInstanceState != null) {
			setSources(readSources(savedInstanceState));
		} else {
			presenter.getSources()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(this::setSources,
							throwable -> log.error("Unable to load sources", throwable));
		}
	}

	private void setSources(List<Source> sources) {
		int count = getCheckedCount(sources);
		if (dialog != null) {
			dialog.setTitle(getString(R.string.sources_numbered, count));
		}
		sourcesAdapter.setItems(sources);
	}

	@SneakyThrows
	private List<Source> readSources(Bundle savedInstanceState) {
		return objectMapper.readValue(savedInstanceState.getString(SAVED_SOURCES), new TypeReference<List<Source>>() {
		});
	}

	@Override
	@SneakyThrows
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(SAVED_SOURCES, objectMapper.writeValueAsString(sourcesAdapter.getSources()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		presenter.detach(this);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (sourcesAdapter.getItemCount() > 0) {
			builder.setTitle(getString(R.string.sources_numbered, getCheckedCount(sourcesAdapter.getSources())));
		} else {
			builder.setTitle(R.string.sources);
		}
		RecyclerView list = new RecyclerView(getActivity());
		list.setLayoutManager(new LinearLayoutManager(getActivity()));
		list.setAdapter(sourcesAdapter);
		builder.setView(list);
		builder.setPositiveButton(R.string.save, (dialog, which) -> {
			presenter.setSources(sourcesAdapter.getSources());
			dismiss();
			if (getActivity() instanceof SourceCallback) {
				((SourceCallback) getActivity()).onSourcesSelected();
			}
		});
		builder.setNegativeButton(R.string.cancel, (dialog, which) -> dismiss());

		dialog = builder.create();
		disableSaveOnNoSources();
		return dialog;
	}

	private void disableSaveOnNoSources() {
		sourcesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

			@Override
			public void onItemRangeChanged(int positionStart, int itemCount) {
				int count = getCheckedCount(sourcesAdapter.getSources());
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(count > 0);
				dialog.setTitle(getString(R.string.sources_numbered, count));
			}
		});

	}

	private int getCheckedCount(List<Source> sources) {
		int count = 0;
		for (Source source : sources) {
			if (source.isChecked()) {
				count++;
			}
		}
		return count;
	}
}
