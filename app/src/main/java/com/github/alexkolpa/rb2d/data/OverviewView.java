package com.github.alexkolpa.rb2d.data;

import io.reactivex.Observable;

public interface OverviewView extends PresenterView {
	Observable<Object> getPageEvents();
}
