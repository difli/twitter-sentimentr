package io.flickd.twitter.pulsar.functions;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import io.flickd.twitter.model.TweetData;

public class TweetRouter implements Function<TweetData, Void> {
	private String toEnSentimentrTopic, toDbTopic;
	private Schema schema = Schema.AVRO(TweetData.class);

	public Void process(TweetData tweetData, Context ctx) throws Exception {

		String lang = tweetData.getLang();
		System.out.println("############ tweetData.getLang(): " + lang);
		if (lang.compareTo("en") == 0) {
			ctx.newOutputMessage(ctx.getTenant() + "/"+ ctx.getNamespace()+ "/to-en-sentimentr", schema).properties(ctx.getCurrentRecord().getProperties())
					.value((TweetData) tweetData).sendAsync();
		} else {
			ctx.newOutputMessage(ctx.getTenant() + "/"+ ctx.getNamespace()+ "/to-db", schema).properties(ctx.getCurrentRecord().getProperties())
					.value((TweetData) tweetData).sendAsync();
		}

		return null;
	}
}