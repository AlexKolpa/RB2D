package com.github.alexkolpa.rb2d.entity;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UploadImage implements Parcelable {
	private String imageId;
	private String imageUrl;
	private String thumb;
	private String uploadId;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(imageUrl);
		dest.writeString(thumb);
		dest.writeString(uploadId);
	}

	public static final Parcelable.Creator<UploadImage> CREATOR
			= new Parcelable.Creator<UploadImage>() {
		public UploadImage createFromParcel(Parcel in) {
			return new UploadImage(in);
		}

		public UploadImage[] newArray(int size) {
			return new UploadImage[size];
		}
	};

	private UploadImage(Parcel in) {
		imageId = in.readString();
		imageUrl = in.readString();
		thumb = in.readString();
		uploadId = in.readString();
	}
}
