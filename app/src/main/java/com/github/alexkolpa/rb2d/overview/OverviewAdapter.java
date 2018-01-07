package com.github.alexkolpa.rb2d.overview;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.alexkolpa.rb2d.R;
import com.github.alexkolpa.rb2d.entity.Image;
import com.squareup.picasso.Picasso;

import lombok.Setter;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ImageViewHolder> {

	public interface ImageClickListener {
		void onClick(Image image);
	}

	private final List<Image> images = new ArrayList<>();

	@Setter
	private ImageClickListener listener;

	@Inject
	OverviewAdapter() {
	}

	@Override
	public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		return new ImageViewHolder(layoutInflater.inflate(R.layout.overview_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ImageViewHolder holder, int position) {
		Image item = images.get(position);
		holder.itemView.setOnClickListener(v -> {
			if(listener != null) {
				listener.onClick(item);
			}
		});
		holder.bind(item);
	}

	@Override
	public int getItemCount() {
		return images.size();
	}

	public void addImages(Collection<Image> additionalItems) {
		int position = images.size();
		images.addAll(additionalItems);
		notifyItemRangeInserted(position, additionalItems.size());
	}

	public void clear() {
		int size = images.size();
		images.clear();
		notifyItemRangeRemoved(0, size);
	}

	static class ImageViewHolder extends RecyclerView.ViewHolder {
		private final ImageView image;
		private final TextView subreddit;
		private final TextView score;

		ImageViewHolder(View itemView) {
			super(itemView);
			this.image = itemView.findViewById(R.id.image);
			this.subreddit = itemView.findViewById(R.id.subreddit_name);
			this.score = itemView.findViewById(R.id.score);
		}

		void bind(Image item) {
			loadImage(item);
			subreddit.setText(item.getSourceName());
			score.setText(item.getScore());
		}

		private void loadImage(Image item) {
			Picasso.with(image.getContext())
					.load(Image.getThumb(item.getThumb(), 300, 300))
					.into(image);
		}
	}
}
