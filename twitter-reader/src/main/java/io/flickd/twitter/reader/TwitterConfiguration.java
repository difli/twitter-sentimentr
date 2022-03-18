package io.flickd.twitter.reader;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;

@Configuration
public class TwitterConfiguration {

	@Value("${twitter-credentials.json.file}")
	private String file;

	@Bean
	public TwitterClient twitterClient() throws JsonParseException, JsonMappingException, IOException {
		return new TwitterClient(TwitterClient.OBJECT_MAPPER
                .readValue(new File(file), TwitterCredentials.class));
	}

}
