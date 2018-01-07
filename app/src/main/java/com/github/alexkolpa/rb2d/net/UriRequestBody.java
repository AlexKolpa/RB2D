package com.github.alexkolpa.rb2d.net;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class UriRequestBody extends RequestBody {

	private final Context context;
	private final Uri uri;

	public UriRequestBody(Context context, Uri uri) {
		this.context = context;
		this.uri = uri;
	}

	@Nullable
	@Override
	public MediaType contentType() {
		String type = context.getContentResolver().getType(uri);
		return MediaType.parse(type);
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		try (Source source = Okio.source(context.getContentResolver().openInputStream(uri))) {
			sink.writeAll(source);
		}
	}
}
