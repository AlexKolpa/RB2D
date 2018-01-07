package com.github.alexkolpa.rb2d.data;

import android.content.Context;
import android.net.Uri;

import com.github.alexkolpa.rb2d.entity.Similarity;
import com.github.alexkolpa.rb2d.entity.UploadImage;
import com.github.alexkolpa.rb2d.net.RehostService;
import com.github.alexkolpa.rb2d.net.UriRequestBody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class RehostPresenter extends Presenter<RehostView> {

	private static final Set<String> BROWSER_SCHEMES = new HashSet<>(Arrays.asList("http", "https"));
	private static final Set<String> CONTENT_SCHEMES = new HashSet<>(Arrays.asList("content", "file"));

	private final RehostService rehostService;
	private final Context context;

	@Inject
	RehostPresenter(RehostService rehostService, Context context) {
		this.rehostService = rehostService;
		this.context = context;
	}

	public Single<Similarity> getReposts(Uri uri) {
		if (BROWSER_SCHEMES.contains(uri.getScheme())) {
			return rehostService.checkReposts(uri.toString());
		} else if (CONTENT_SCHEMES.contains(uri.getScheme())) {
			MultipartBody.Part part = MultipartBody.Part.create(new UriRequestBody(context, uri));
			return rehostService.checkReposts(part);
		} else {
			return Single.error(new IllegalArgumentException("Not a supported scheme!"));
		}
	}

	public Single<UploadImage> rehost(Similarity similarity) {
		return rehostService.rehost(similarity.getOriginal());
	}
}
