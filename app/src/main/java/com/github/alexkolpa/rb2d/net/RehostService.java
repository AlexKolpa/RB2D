package com.github.alexkolpa.rb2d.net;

import com.github.alexkolpa.rb2d.entity.Similarity;
import com.github.alexkolpa.rb2d.entity.UploadImage;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RehostService {

	@GET("images/")
	Single<Similarity> checkReposts(@Query("imageUri") String uri);

	@POST("images/")
	Single<Similarity> checkReposts(@Part("upload") MultipartBody.Part image);

	@GET("upload/?action=upload&force=true")
	Single<UploadImage> rehost(@Query("imageUrl") String uri);
}
