package com.github.alexkolpa.rb2d.net;

import com.github.alexkolpa.rb2d.entity.Source;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface SourceService {

	@GET("sources/")
	Single<List<Source>> getSources();
}
