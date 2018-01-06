package com.github.alexkolpa.rb2d.net;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.github.alexkolpa.rb2d.entity.Image;
import io.reactivex.Single;
import lombok.Getter;
import lombok.Value;

public class ImageScroller {

	@Value
	public static class ImageBatch {
		private Long previousDate;
		private final List<Image> images;
	}

	private static final int limit = 30;
	private final RedditBooruService redditBooruService;

	@Getter
	private boolean isLoading = false;
	@Getter
	private boolean hasMoreItems = true;

	private final AtomicReference<Long> lastDate = new AtomicReference<>(null);

	@Inject
	ImageScroller(RedditBooruService redditBooruService) {
		this.redditBooruService = redditBooruService;
	}

	public Single<ImageBatch> loadNextPage() {
		isLoading = true;
		Long lastDate = this.lastDate.get();
		return redditBooruService.getImages(lastDate, limit)
				.doOnSuccess(images -> {
					if (images.isEmpty()) {
						hasMoreItems = false;
					}
					else {
						Image image = images.get(images.size() - 1);
						this.lastDate.set(image.getDateCreated());
					}
				})
				.map(images -> new ImageBatch(lastDate, images))
				.doFinally(() -> isLoading = false);
	}
}
