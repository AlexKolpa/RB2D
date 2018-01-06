package com.github.alexkolpa.rb2d.data;

import javax.inject.Inject;

import java.util.List;

import com.github.alexkolpa.rb2d.entity.Image;
import com.github.alexkolpa.rb2d.net.ImageScroller;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class OverviewPresenter extends Presenter<OverviewView> {

	public enum State {
		LOADED,
		EMPTY,
		ERROR,
		LOADING,
		END
	}

	private final ImageScroller imageScroller;
	private final BehaviorSubject<State> stateSubject;

	@Inject
	OverviewPresenter(ImageScroller imageScroller) {
		this.imageScroller = imageScroller;
		this.stateSubject = BehaviorSubject.create();
	}

	public Flowable<List<Image>> getImages() {
		return Observable.just(new Object()).concatWith(getView().getPageEvents())
				.toFlowable(BackpressureStrategy.DROP)
				.filter(aVoid -> canLoadMoreItems())
				.compose(bindToView())
				.concatMap(aVoid -> {
							stateSubject.onNext(State.LOADING);
							return imageScroller.loadNextPage()
									.doOnError(throwable -> stateSubject.onNext(State.ERROR))
									.doOnSuccess(this::updateState)
									.map(ImageScroller.ImageBatch::getImages)
									.toFlowable();
						}
				);
	}

	private void updateState(ImageScroller.ImageBatch imageBatch) {
		if (imageBatch.getPreviousDate() == null && imageBatch.getImages().isEmpty()) {
			stateSubject.onNext(State.EMPTY);
		}
		else if (!imageScroller.isHasMoreItems()) {
			stateSubject.onNext(State.END);
		}
		else {
			stateSubject.onNext(State.LOADED);
		}
	}

	public Observable<State> getOverviewStateEvents() {
		return stateSubject;
	}

	private boolean canLoadMoreItems() {
		return imageScroller.isHasMoreItems() && !imageScroller.isLoading();
	}

	public void clear() {
		imageScroller.reset();
	}
}

