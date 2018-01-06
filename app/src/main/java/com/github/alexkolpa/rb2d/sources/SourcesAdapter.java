package com.github.alexkolpa.rb2d.sources;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.alexkolpa.rb2d.R;
import com.github.alexkolpa.rb2d.entity.Source;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourceViewHolder> {

	private final List<Source> sources = new ArrayList<>();

	@Inject
	SourcesAdapter() {
		setHasStableIds(true);
	}

	@Override
	public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item, parent, false);
		return new SourceViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(SourceViewHolder holder, int position) {
		if(position == 0) {
			boolean allSelected = true;
			for(Source source: sources) {
				if (!source.isChecked()) {
					allSelected = false;
					break;
				}
			}
			holder.name.setText(R.string.all_sources);
			holder.checked.setOnCheckedChangeListener(null);
			holder.checked.setChecked(allSelected);
			holder.checked.setOnCheckedChangeListener((buttonView, isChecked) -> {
				for (Source source : sources) {
					source.setChecked(isChecked);
				}
				notifyItemRangeChanged(1, sources.size());
			});
		} else {
			holder.bind(sources.get(position - 1));
		}
	}

	@Override
	public int getItemCount() {
		int size = sources.size();
		return size > 0 ? size + 1 : 0;
	}

	@Override
	public long getItemId(int position) {
		return position == 0 ? 0 : Long.valueOf(sources.get(position - 1).getValue());
	}

	void setItems(List<Source> newSources) {
		int size = sources.size();
		sources.addAll(newSources);
		notifyItemRangeInserted(size, newSources.size());
	}

	List<Source> getSources() {
		return sources;
	}

	class SourceViewHolder extends RecyclerView.ViewHolder {

		private final TextView name;
		private final CheckBox checked;

		public SourceViewHolder(View itemView) {
			super(itemView);
			this.name = itemView.findViewById(R.id.name);
			this.checked = itemView.findViewById(R.id.checked);
		}

		public void bind(Source source) {
			name.setText(source.getTitle());
			checked.setOnCheckedChangeListener(null);
			checked.setChecked(source.isChecked());
			checked.setOnCheckedChangeListener((view, isChecked) -> {
				source.setChecked(isChecked);
				//Ensure 'All' gets updated accordingly
				notifyItemChanged(0);
			});
		}
	}
}
