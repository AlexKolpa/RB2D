package com.github.alexkolpa.rb2d.entity;

import java.util.List;

import lombok.Data;

@Data
public class Similarity {
	private List<Image> results;
	private String original;
	private String preview;
	private String view;
	private List<String> identical;
}
