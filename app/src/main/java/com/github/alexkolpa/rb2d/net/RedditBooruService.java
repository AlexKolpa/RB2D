package com.github.alexkolpa.rb2d.net;

import java.util.List;

import com.github.alexkolpa.rb2d.entity.Image;
import com.github.alexkolpa.rb2d.entity.Source;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditBooruService {

	@GET("sources/")
	Single<List<Source>> getSources();

	@GET("images/")
	Single<List<Image>> getImages(@Query("afterDate") Long afterDate, @Query("limit") Integer limit);
}
