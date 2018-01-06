package com.github.alexkolpa.rb2d.data;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class Presenter<T extends PresenterView> {

	@Getter(AccessLevel.PROTECTED)
	private T view;

	public void attach(T view) {
		this.view = view;
	}

	public void detach(T view) {
		if (this.view == view) {
			this.view = null;
		}
	}

	protected <X> FlowableTransformer<X, X> bindToView() {
		return xObservable -> xObservable.filter(x -> view != null);
	}
}
