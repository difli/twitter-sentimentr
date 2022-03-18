package io.flickd.twitter.reader;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import com.github.scribejava.core.model.Response;

import io.flickd.twitter.model.TweetData;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.Tweet;

@SpringBootApplication
public class TwitterReaderApplication implements CommandLineRunner {

	@Value("${reader.twitter-filtered-stream-rule}")
	String rule;
	
	@Autowired
	private Producer<TweetData> producer;

	@Value("classpath:twitter/secrets.json")
	private Resource twitterSecretsFile;

	@Autowired
	TwitterClient twitterClient;
	
	public static void main(String[] args) {
		SpringApplication.run(TwitterReaderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<StreamRule> rules = twitterClient.retrieveFilteredStreamRules();
		if (rules != null) {
			rules.forEach(rule -> {
//				twitterClient.deleteFilteredStreamRule(rule.getValue());
				twitterClient.deleteFilteredStreamRuleId(rule.getId());
			});
		}

		StreamRule nextresult = twitterClient.addFilteredStreamRule(rule, "rule");

		Consumer<Tweet> consumer = new SendTweet();
		Future<Response> future = twitterClient.startFilteredStream(consumer);
		try {
			future.get(5, TimeUnit.SECONDS);
		} catch (TimeoutException exc) {
			// It's OK
		}
	}

	public class SendTweet implements Consumer<Tweet> {
		TweetData tweetData = new TweetData();

		@Override
		public void accept(Tweet t) {

			tweetData.setLang(t.getLang());
			tweetData.setId(t.getId());
			tweetData.setCreatedAt(t.getCreatedAt().toString());
			tweetData.setTweet(t.getText());
			tweetData.setSentiment(ThreadLocalRandom.current().nextInt(0, 5));
			
			// Send a message to the topic
			try {
				producer.newMessage().value(tweetData).send();
			} catch (PulsarClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
