package com.github.alexkolpa.rb2d.overview;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.github.alexkolpa.rb2d.R;
import com.github.alexkolpa.rb2d.RB2DApplication;
import com.github.alexkolpa.rb2d.data.OverviewPresenter;
import com.github.alexkolpa.rb2d.data.OverviewView;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverviewActivity extends AppCompatActivity implements OverviewView {

	private static final Object EVENT = new Object();

	@Inject
	OverviewAdapter overviewAdapter;
	@Inject
	OverviewPresenter overviewPresenter;

	private GridLayoutManager layoutManager;
	private RecyclerView overview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((RB2DApplication) getApplication()).getNetComponent().inject(this);

		setContentView(R.layout.activity_overview);
		overviewPresenter.attach(this);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener((View view) -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show());

		overview = findViewById(R.id.overview);
		overview.setAdapter(overviewAdapter);

		layoutManager = new GridLayoutManager(this, 2);
		overview.setLayoutManager(layoutManager);

		overviewPresenter.getImages()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(galleryEntries -> overviewAdapter.addImages(galleryEntries),
						throwable -> log.error("Couldn't retrieve gallery items", throwable));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		overviewPresenter.detach(this);
	}

	private boolean canLoadPage(RecyclerViewScrollEvent event) {
		int visibleItemCount = layoutManager.getChildCount();
		int totalItemCount = layoutManager.getItemCount();
		int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
		return visibleItemCount + firstVisibleItem >= totalItemCount;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_overview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Observable<Object> getPageEvents() {
		return RxRecyclerView.scrollEvents(overview)
						.filter(this::canLoadPage)
						.map(event -> EVENT);
	}
}
