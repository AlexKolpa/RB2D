package com.github.alexkolpa.rb2d.net;

import android.text.TextUtils;

import com.github.alexkolpa.rb2d.sources.SourceManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Set;

import javax.inject.Inject;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SourceInterceptor implements Interceptor {

	private static final String HEADER = "Cookie";
	private final SourceManager sourceManager;

	@Inject
	SourceInterceptor(SourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Set<String> sourceIds = sourceManager.getSourceIds();
		if (sourceIds.isEmpty()) {
			return chain.proceed(chain.request());
		}

		Request.Builder builder = chain.request().newBuilder();

		builder.addHeader(HEADER, "sources=" + buildCookieHeader(sourceIds));

		return chain.proceed(builder.build());
	}

	@SneakyThrows
	private String buildCookieHeader(Set<String> sourceIds) {
		return URLEncoder.encode(TextUtils.join(",", sourceIds), "UTF-8");
	}
}
