package io.flickd.twitter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetData {
	public String lang;
    public String id;
	public String tweet;
	public String createdAt;
	public int sentiment;
}
