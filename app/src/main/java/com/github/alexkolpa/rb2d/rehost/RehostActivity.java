package com.github.alexkolpa.rb2d.rehost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.alexkolpa.rb2d.R;
import com.github.alexkolpa.rb2d.RB2DApplication;
import com.github.alexkolpa.rb2d.data.RehostPresenter;
import com.github.alexkolpa.rb2d.data.RehostView;
import com.github.alexkolpa.rb2d.entity.Image;
import com.github.alexkolpa.rb2d.entity.Similarity;
import com.github.alexkolpa.rb2d.overview.OverviewAdapter;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RehostActivity extends AppCompatActivity implements RehostView {

	static final String IMAGE_EXTRA = "image";

	@Inject
	RehostPresenter rehostPresenter;
	@Inject
	OverviewAdapter overviewAdapter;

	private EditText textInput;
	private View deviceInput;
	private Button mainAction;
	private ProgressBar progress;
	private View repostContainer;
	private TextView isRepost;
	private ImageView original;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((RB2DApplication) getApplication()).getComponent().inject(this);
		rehostPresenter.attach(this);

		setContentView(R.layout.activity_rehost);
		textInput = findViewById(R.id.text_input);
		deviceInput = findViewById(R.id.device_input);
		progress = findViewById(R.id.progress);
		repostContainer = findViewById(R.id.repost_container);
		original = findViewById(R.id.image_container);
		isRepost = findViewById(R.id.is_repost);
		mainAction = findViewById(R.id.main_action);

		RecyclerView reposts = findViewById(R.id.reposts);
		reposts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		reposts.setAdapter(overviewAdapter);

		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_VIEW.equals(action)) {
			loadForUri(intent.getData());
		}
		else {
			mainAction.setEnabled(false);
			textInput.addTextChangedListener(new TextWatcher(s -> {
				boolean validUrl = URLUtil.isValidUrl(s.toString());
				mainAction.setEnabled(validUrl);
			}));
			mainAction.setOnClickListener(v -> loadForUri(Uri.parse(textInput.getText().toString())));
		}
	}

	private void loadForUri(Uri uri) {
		repostContainer.setVisibility(View.GONE);
		textInput.setText(uri.toString());
		showLoading(true);
		rehostPresenter.getReposts(uri)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
					showLoading(false);
					showRepost(result);
					prepareUpload(result);
				}, throwable -> log.error("Unable to fetch repost results", throwable));
	}

	private void prepareUpload(Similarity result) {
		if(result.getIdentical().isEmpty()) {
			mainAction.setText(R.string.upload);
		} else {
			mainAction.setText(R.string.upload_anyway);
		}

		mainAction.setOnClickListener(v -> upload(result));
	}

	private void upload(Similarity result) {
		repostContainer.setVisibility(View.GONE);
		showLoading(true);
		rehostPresenter.rehost(result)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(image -> {
					finish();
				});
	}

	private void showLoading(boolean loading) {
		textInput.setEnabled(!loading);
		deviceInput.setEnabled(!loading);
		mainAction.setEnabled(!loading);
		progress.setVisibility(loading ? View.VISIBLE : View.GONE);
	}

	private void showRepost(Similarity similarity) {
		repostContainer.setVisibility(View.VISIBLE);
		overviewAdapter.clear();
		overviewAdapter.addImages(similarity.getResults());
		isRepost.setVisibility(similarity.getIdentical().isEmpty() ? View.GONE : View.VISIBLE);
		String subs = TextUtils.join(", ", similarity.getIdentical());
		isRepost.setText(getString(R.string.repost, subs));
		Picasso.with(this)
				.load(Image.getThumb(similarity.getPreview(), 300, 300))
				.into(original);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		rehostPresenter.detach(this);
	}

	@RequiredArgsConstructor
	private static class TextWatcher implements android.text.TextWatcher {

		public interface Consumer {
			void accept(Editable s);
		}

		private final Consumer consumer;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			consumer.accept(s);
		}
	}
}
