package com.github.alexkolpa.rb2d.entity;

import lombok.Data;

@Data
public class Image {
	private static final String FORMAT = "%s_%d_%d.jpg";
	private long id;
	private long imageId;
	private String cdnUrl;
	private int width;
	private int height;
	private String caption;
	private String sourceUrl;
	private String type;
	private long sourceId;
	private String sourceName;
	private long postId;
	private String title;
	private String keywords;
	private long dateCreated;
	private String externalId;
	private String score;
	private boolean visible;
	private long userId;
	private String userName;
	private boolean nsfw;
	private String thumb;
	private Integer idxInAlbum;
	private long age;

	public static String getThumb(String thumb, int width, int height) {
		return String.format(FORMAT, thumb, width, height);
	}
}
