package io.flickd.twitter.sentimentr;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.flickd.twitter.model.TweetData;
import io.flickd.twitter.sentimentr.service.EnSentimentTextProcessing;
import io.flickd.twitter.sentimentr.service.MyMessageListener;

@Configuration
@Profile("!astra")
public class LocalConfiguration {

	private PulsarClient client;

	@Value("${pulsar.service-url}")
	String serviceUrl;
	
	@Value("${pulsar.topic.to-db}")
	String toDb;

	@Value("${pulsar.topic.to-en-sentimentr}")
	String toEnSentimentr;

	@Autowired
	private EnSentimentTextProcessing nlp;

	@Bean
	public Producer<TweetData> producer() throws PulsarClientException {
		client = PulsarClient.builder().serviceUrl(serviceUrl).build();
		return client.newProducer(Schema.AVRO(TweetData.class)).topic(toDb).create();
	}
	
	@Bean
	public Consumer<TweetData> consumer() throws PulsarClientException {
		client = PulsarClient.builder().serviceUrl(serviceUrl).build();
		return client.newConsumer(Schema.AVRO(TweetData.class)).topic(toEnSentimentr)
				.subscriptionName("en-tweet-subscription")
				.messageListener(new MyMessageListener<TweetData>(producer(),nlp)).subscribe();
	}

}
