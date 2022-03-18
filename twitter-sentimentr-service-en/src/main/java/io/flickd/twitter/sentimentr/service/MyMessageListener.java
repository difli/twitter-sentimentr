package io.flickd.twitter.sentimentr.service;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import io.flickd.twitter.model.TweetData;

public class MyMessageListener<T> implements MessageListener<T> {

	public MyMessageListener(Producer<TweetData> producer, EnSentimentTextProcessing nlp) {
		super();
		this.producer = producer;
		this.nlp = nlp;
	}

	private Producer<TweetData> producer;
	
	private EnSentimentTextProcessing nlp;

	@Override
	public void received(Consumer<T> consumer, Message<T> msg) {
		TweetData tweetData = new TweetData();

		try {
			tweetData.setCreatedAt(((TweetData) msg.getValue()).getCreatedAt());
			tweetData.setId(((TweetData) msg.getValue()).getId());
			tweetData.setLang(((TweetData) msg.getValue()).getLang());
			tweetData.setTweet(((TweetData) msg.getValue()).getTweet());
			tweetData.setSentiment(nlp.calculateSentiment(((TweetData) msg.getValue()).getTweet()));
			producer.newMessage().value(tweetData).send();
			consumer.acknowledge(msg);
		} catch (Exception e) {
			consumer.negativeAcknowledge(msg);
		}
	}
}